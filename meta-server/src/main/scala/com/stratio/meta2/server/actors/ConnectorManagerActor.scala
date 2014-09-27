package com.stratio.meta2.server.actors

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.stratio.meta.communication._
import com.stratio.meta2.common.data.{ClusterName, ConnectorName}
import com.stratio.meta2.core.connector.ConnectorManager
import com.stratio.meta2.core.metadata.MetadataManager
import com.stratio.meta2.core.query._
import com.stratio.meta2.core.statements.{CreateCatalogStatement, CreateTableStatement}
import org.apache.log4j.Logger


object ConnectorManagerActor {
  def props(connectorManager: ConnectorManager): Props = Props(new ConnectorManagerActor(connectorManager))
}

class ConnectorManagerActor(connectorManager: ConnectorManager) extends Actor with ActorLogging {

  lazy val logger = Logger.getLogger(classOf[ConnectorManagerActor])
  log.info("Lifting connector actor")
  val coordinatorActorRef = context.actorSelection("../CoordinatorActor")
  //coordinatorActorRef ! "hola"

  override def preStart(): Unit = {
    //#subscribe
    Cluster(context.system).subscribe(self, classOf[MemberEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit =
    Cluster(context.system).unsubscribe(self)

  def receive = {

    /**
     * A new actor connects to the cluster. If the new actor is a connector, we requests its name.
     */
    case mu: MemberUp => {
      log.info("Member is Up: " + mu.toString + mu.member.getRoles)
      val it = mu.member.getRoles.iterator()
      while (it.hasNext()) {
        val rol = it.next()
        rol match {
          case "connector" =>
            val connectorActorRef = context.actorSelection(RootActorPath(mu.member.address) / "user" / "meta-connector")
            val id = java.util.UUID.randomUUID.toString()

            connectorActorRef ! getConnectorName()
            connectorActorRef ! Start()

        }
      }
    }

    /**
     * Connector answers its name.
     */
    case msg: replyConnectorName => {
      val connectorRef = sender;
      MetadataManager.MANAGER.addConnectorRef(new ConnectorName(msg.name), connectorRef)
    }

      /*
    case query: StorageInProgressQuery => {
      log.info("storage in progress query")
      //connectorsMap(query.getConnectorName()) ! query
      query.getExecutionStep.getActorRef.asInstanceOf[ActorRef] ! query
    }

    case query: SelectInProgressQuery => {
      val clustername = new ClusterName("//TODO:") //TODO: the query should give me the cluster's name
      val executionStep = query.getExecutionStep
      log.info("select in progress query")
      //connectorsMap(query.getConnectorName()) ! Execute(clustername,workflow)
      query.getExecutionStep.getActorRef.asInstanceOf[ActorRef] ! Execute(null, executionStep)
    }

    case query: MetadataInProgressQuery => {

      val statement = query.getStatement()
      //val messagesender=connectorsMap(query.getConnectorName())
      val messagesender = query.getExecutionStep.getActorRef

      statement match {
        case createCatalogStatement: CreateCatalogStatement => {
          println("Createcatalog statement")
          //messagesender ! CreateCatalog(query.getClusterName,query.getDefaultCatalog)
          //createCatalogStatement
        }
        case createTableStatement: CreateTableStatement => {
          println("CreateTableStatement")
        }
        case _ =>
          println("Unidentified MetadataInProgressQuery Received")
      }

      log.info("metadata in progress query")
      //connectorsMap(query.getConnectorName()) ! query
      query.getExecutionStep.getActorRef.asInstanceOf[ActorRef] ! query
    }
    */

    //pass the message to the connectorActor to extract the member in the cluster
    case state: CurrentClusterState => {
      logger.info("Current members: " + state.members.mkString(", "))
      //TODO Process CurrentClusterState
    }
    case member: UnreachableMember => {
      logger.info("Member detected as unreachable: " + member)
      //TODO Process UnreachableMember
    }
    case member: MemberRemoved => {
      logger.info("Member is Removed: " + member.member.address)
      //TODO Process MemberRemoved
    }
    case _: MemberEvent => {
      logger.info("Receiving anything else")
      //TODO Process MemberEvent
    }
    case _: ClusterDomainEvent => {
      logger.debug("ClusterDomainEvent")
      //TODO Process ClusterDomainEvent
    }
    case ReceiveTimeout => {
      logger.warn("ReceiveTimeout")
      //TODO Process ReceiveTimeout
    }

    /*
    case toConnector: MetadataStruct =>
      connectorsMap(toConnector.connectorName) ! toConnector

    case toConnector: StorageQueryStruct =>
      connectorsMap(toConnector.connectorName) ! toConnector

    case toConnector: WorkflowStruct =>
      connectorsMap(toConnector.connectorName) ! toConnector

    case response: Response =>
      //connectorsMap += (response.msg -> sender)

    case query: SelectPlannedQuery => {
      log.info("ConnectorManagerActor received SelectPlannedQuery")
      sender ! "Ok"
    }
    */

    case other =>
      println("connector actor receives event")
    //      sender ! "OK"
    //memberActorRef.tell(objetoConWorkflow, context.sender)
  }

}
