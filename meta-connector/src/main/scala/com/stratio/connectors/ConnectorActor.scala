package com.stratio.connectors

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.stratio.meta.common.connector.IConnector
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta.common.result.{CommandResult, QueryResult, MetadataResult}
import com.stratio.meta.communication._

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


  //val cluster = Cluster(context.system)
  //import cluster.{ scheduler }
  //val heartbeatTask = scheduler.schedule(PeriodicTasksInitialDelay max HeartbeatInterval, HeartbeatInterval, self, HeartbeatTick)


  // subscribe to cluster changes, re-subscribe when restart

  override def handleHeartbeat(heartbeat: HeartbeatSig) = {
    println("ConnectorActor receives a heartbeat message")
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

    case wf: LogicalWorkflow=> {
     connector.getQueryEngine().execute(wf)
      val result=QueryResult.createSuccessQueryResult()
      result.setQueryId("TODO: extract a real query ID from the logicalworkflow") //TODO
      sender ! result
    }

    case metadataOp: MetadataOperation=> {
      val eng=connector.getMetadataEngine()
      metadataOp match{
        case CreateCatalog(queryId, clustername,metadata)=>{ eng.createCatalog(clustername,metadata) }
        case CreateIndex(queryId, clustername,metadata) =>{ eng.createIndex(clustername,metadata) }
        case CreateTable(queryId, clustername,metadata) =>{ eng.createTable(clustername,metadata) }
        case DropCatalog(queryId, clustername,metadata) =>{ eng.dropCatalog(clustername,metadata) }
        case DropIndex(queryId, clustername,metadata) =>{ eng.dropIndex(clustername,metadata) }
        case DropTable(queryId, clustername,metadata) =>{ eng.dropTable(clustername,metadata) }
      }
      val result=MetadataResult.createSuccessMetadataResult()
      result.setQueryId("TODO: extract a real query ID from the metadataop") //TODO
      sender ! result
    }

    case storageOp: StorageOperation=> {
      val eng=connector.getStorageEngine()
      storageOp match{
        case Insert(queryId, clustername,table,row)=>{ eng.insert(clustername,table,row) }
        case InsertBatch(queryId, clustername,table,rows)=>{ eng.insert(clustername,table,rows) }
      }
      val result=CommandResult.createCommandResult("ok")
      result.setQueryId("TODO: extract a real query ID from the metadataop") //TODO
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
