package com.stratio.connectors

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.stratio.meta.common.connector.IConnector
import com.stratio.meta.communication.{HeartbeatSig, getConnectorName, replyConnectorName}
import com.stratio.meta2.core.query.{MetadataInProgressQuery, SelectInProgressQuery, StorageInProgressQuery}
import com.stratio.meta2.core.statements.{CreateTableStatement, SelectStatement}

object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}

object ConnectorActor {
  def props(connectorName: String, connector: IConnector): Props = Props(new ConnectorActor(connectorName, connector))
}


class ConnectorActor(connectorName: String, conn: IConnector) extends HeartbeatActor with ActorLogging {
  //class ConnectorActor(connectorName:String,conn:IConnector) extends Actor with ActorLogging {

  val connector = conn
  //TODO: test if it works with one thread and multiple threads
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

    case inProgressQuery: MetadataInProgressQuery => {
      log.info("->" + "Receiving MetadataInProgressQuery")

      val statement = inProgressQuery.getStatement()
      statement match {
        //case ms:MetadataStatement =>
        case ms: CreateTableStatement =>
          log.info("->receiving CreateTableStatement")
          //val clustername = inProgressQuery.getClusterName()
          //connector.getMetadataEngine().createTable(clustername, ms.getTableMetadata())
          sender ! "ok"
        case _ =>
          log.info("->receiving a statement of a type it shouldn't")
      }
    }

    case inProgressQuery: SelectInProgressQuery => {
      log.info("->" + "Receiving SelectInProgressQuery")
      val statement = inProgressQuery.getStatement()
      statement match {
        case ms: SelectStatement =>
          log.info("->receiving SelectStatement")
          //val clustername = inProgressQuery.getClusterName()
          //val logicalworkflow = inProgressQuery.getLogicalWorkFlow()
          //connector.getQueryEngine().execute(clustername, logicalworkflow)
          sender ! "ok"
        case _ =>
          log.info("->receiving a statement of a type it shouldn't")
      }
    }

    case inProgressQuery: StorageInProgressQuery =>
      log.info("->" + "Receiving StorageInProgressQuery")


    case msg: getConnectorName => {
      sender ! replyConnectorName(connectorName)
    }

    case s: String =>
      println("->" + "Receiving String: {}" + s)
      log.info("->" + "Receiving String: {}", s)

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
