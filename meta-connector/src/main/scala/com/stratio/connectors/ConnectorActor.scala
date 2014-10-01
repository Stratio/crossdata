package com.stratio.connectors

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.stratio.meta.common.connector.IConnector
import com.stratio.meta.common.result.{CommandResult, MetadataResult, Result}
import com.stratio.meta.communication._

import scala.collection.mutable.{ListMap, Map}

object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}

object ConnectorActor {
  def props(connectorName: String, connector: IConnector): Props = Props(new ConnectorActor(connectorName, connector))
}


class ConnectorActor(connectorName: String, conn: IConnector) extends HeartbeatActor with ActorLogging {
//class ConnectorActor(connectorName:String,conn:IConnector) extends Actor with ActorLogging {

  //TODO: test if it works with one thread and multiple threads
  val connector = conn
  var state = State.Stopped
  var supervisorActorRef: ActorRef = null
  var runningJobs:Map[String,ActorRef]=new ListMap[String,ActorRef]()


  //val cluster = Cluster(context.system)
  //import cluster.{ scheduler }
  //val heartbeatTask = scheduler.schedule(PeriodicTasksInitialDelay max HeartbeatInterval, HeartbeatInterval, self, HeartbeatTick)


  // subscribe to cluster changes, re-subscribe when restart

  override def handleHeartbeat(heartbeat: HeartbeatSig) = {
    runningJobs.foreach{
      keyval=>
        keyval._2 ! IAmAlive(keyval._1)
        log.debug("ConnectorActor sends an ImAlive message (job="+keyval._1+")") //TODO: delete line after testing
    }
  }

  override def preStart(): Unit = {
    //#subscribe
    Cluster(context.system).subscribe(self, classOf[MemberEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  //override def receive = super.receive orElse{
  override def receive = {


    case _: com.stratio.meta.communication.Start => {
      //context.actorSelection(RootActorPath(mu.member.address) / "user" / "coordinatorActor")
      supervisorActorRef = sender
    }

    case connectRequest: com.stratio.meta.communication.Connect => {
      log.info("->" + "Receiving MetadataRequest")
      //connector.connect(connectRequest.credentials,connectRequest.connectorClusterConfig)
      this.state = State.Started //if it doesn't connect, an exception will be thrown and we won't get here
      sender ! "ok"
    }

    case _: com.stratio.meta.communication.Shutdown => {
      log.info("->" + "Receiving Shutdown")
      this.shutdown()
    }

    case ex:Execute=>{
      try {
        runningJobs.put(ex.queryId,sender)
        val result=connector.getQueryEngine().execute(ex.workflow)
        result.setQueryId(ex.queryId)
        sender ! result
      } catch {
        case ex: Exception => {
          val result=Result.createExecutionErrorResult(ex.getStackTraceString)
          sender ! result
        }
        case err: Error =>
          log.error("error in ConnectorActor( receiving LogicalWorkflow )")
      } finally {
        runningJobs.remove(ex.queryId)
      }
    }

    case metadataOp: MetadataOperation => {
      var qId:String=null
      try {
        val eng = connector.getMetadataEngine()
        metadataOp match {
          case CreateCatalog(queryId, clustername, metadata) => {
            qId=queryId
            eng.createCatalog(clustername, metadata)
          }
          case CreateIndex(queryId, clustername, metadata) => {
            qId=queryId
            eng.createIndex(clustername, metadata)
          }
          case CreateTable(queryId, clustername, metadata) => {
            qId=queryId
            eng.createTable(clustername, metadata)
          }
          case DropCatalog(queryId, clustername, metadata) => {
            qId=queryId
            eng.dropCatalog(clustername, metadata)
          }
          case DropIndex(queryId, clustername, metadata) => {
            qId=queryId
            eng.dropIndex(clustername, metadata)
          }
          case DropTable(queryId, clustername, metadata) => {
            qId=queryId
            eng.dropTable(clustername, metadata)
          }
        }
        val result = MetadataResult.createSuccessMetadataResult()
        result.setQueryId(qId)
        sender ! result
      } catch {
        case ex: Exception => {
          val result=com.stratio.meta.common.result.Result.createExecutionErrorResult(ex.getStackTraceString)
          sender ! result
        }
        case err: Error =>
          log.error("error in ConnectorActor( receiving MetaOperation)")
      }
      val result=MetadataResult.createSuccessMetadataResult()
      result.setQueryId(qId)
      sender ! result
    }

    case _:Result =>
      //TODO:  ManagementWorkflow

    case storageOp: StorageOperation => {
      var qId:String=null
      try {
        val eng = connector.getStorageEngine()
        storageOp match {
          case Insert(queryId, clustername, table, row) => {
            qId=queryId
            eng.insert(clustername, table, row)
          }
          case InsertBatch(queryId, clustername, table, rows) => {
            qId=queryId
            eng.insert(clustername, table, rows)
          }
        }
        val result = CommandResult.createCommandResult("ok")
        result.setQueryId(qId)
        sender ! result
      } catch {
        case ex: Exception => {
          log.debug(ex.getStackTraceString)
          val result=com.stratio.meta.common.result.Result.createExecutionErrorResult(ex.getStackTraceString)
          sender ! result
        }
        case err: Error =>
          log.error("error in ConnectorActor( receiving StorageOperation)")
      }
      val result=CommandResult.createCommandResult("ok") //TODO: why does MetadataResult have
      //TODO: createSuccessfulMetaResult and CommandResult doesn't have createSuccessfulCommandResult?
      result.setQueryId(qId)
      sender ! result
    }



    case msg: getConnectorName => {
      sender ! replyConnectorName(connectorName)
    }

    case MemberUp(member) =>
      println("member up")
      log.info("*******Member is Up: {} {}!!!!!", member.toString, member.getRoles)
    //val actorRefe=context.actorSelection(RootActorPath(member.address) / "user" / "connectoractor" )
    //actorRefe ! "hola "+member.address+ "  "+RootActorPath(member.address)

    case state: CurrentClusterState =>
      log.info("Current members: {}", state.members.mkString(", "))

    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)

    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)

    case _: MemberEvent =>
      log.info("Receiving anything else")

  }

  def shutdown() = {
    println("ConnectorActor is shutting down")
    //connector.close(new ClusterName(""))
    this.state = State.Stopping
    connector.shutdown()
    this.state = State.Stopped
  }
}
