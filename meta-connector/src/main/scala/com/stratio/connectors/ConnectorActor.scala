package com.stratio.connectors

import akka.actor.{ActorLogging, Props}
import akka.cluster.ClusterEvent._
import com.stratio.meta.common.connector.IConnector
import com.stratio.meta.communication.{getConnectorName, replyConnectorName}
import com.stratio.meta2.core.query.{MetadataInProgressQuery, SelectInProgressQuery, StorageInProgressQuery}
import com.stratio.meta2.core.statements.{MetadataStatement, SelectStatement}


object ConnectorActor{
  def props (connectorName:String,connector:IConnector):Props = Props (new ConnectorActor(connectorName,connector) )
}

class ConnectorActor(connectorName:String,conn:IConnector) extends HeartbeatActor with ActorLogging {
//class ConnectorActor(connectorName:String,conn:IConnector) extends Actor with ActorLogging {

  val connector=conn //TODO: test if it works with one thread and multiple threads

  //val cluster = Cluster(context.system)
  //import cluster.{ scheduler }
  //val heartbeatTask = scheduler.schedule(PeriodicTasksInitialDelay max HeartbeatInterval, HeartbeatInterval, self, HeartbeatTick)


  // subscribe to cluster changes, re-subscribe when restart

  /*
  override def handleHeartbeat(heartbeat:HeartbeatSig)={
    println("ConnectorActor receives a heartbeat message")
  }
  */

  def shutdown()={
    println("ConnectorActor is shutting down")
    //connector.close(new ClusterName(""))
    connector.shutdown()
  }

  //override def receive = super.receive orElse{
  override def receive = {

    case _:com.stratio.meta.communication.Shutdown=>{
      log.info("->"+"Receiving Shutdown")
      this.shutdown()
    }
    case inProgressQuery:MetadataInProgressQuery=>{
      log.info("->"+"Receiving MetadataInProgressQuery")

      val statement=inProgressQuery.getStatement()
      statement match{
        case ms:MetadataStatement =>
          log.info("->receiving MetadataStatement")
        case _ =>
          log.info("->receiving a statement of a type it shouldn't")
      }
    }

     case inProgressQuery:SelectInProgressQuery=>{
      log.info("->"+"Receiving SelectInProgressQuery")
      println("->"+"Receiving SelectInProgressQuery")
      val statement=inProgressQuery.getStatement()
      statement match{
        case ms:SelectStatement =>
          log.info("->receiving SelectStatement")
          val clustername=inProgressQuery.getClusterName()
          val logicalworkflow=inProgressQuery.getLogicalWorkFlow()
          connector.getQueryEngine().execute(clustername,logicalworkflow)
          sender ! "ok"
        case _ =>
          log.info("->receiving a statement of a type it shouldn't")
      }
     }

    case inProgressQuery:StorageInProgressQuery=>
      log.info("->"+"Receiving StorageInProgressQuery")



    /*
    case metadataEngineRequest:MetadataEngineRequest=>
      //getMetadataEngine()
      log.info("->"+"Receiving MetadataRequest")

    case dataStoreNameRequest:DataStoreNameRequest=>
      //getDatastoreName()
      log.info("->"+"Receiving MetadataRequest")
      
    case initRequest:com.stratio.meta.communication.InitRequest=>
      //init(IConfiguration)
      log.info("->"+"Receiving MetadataRequest")
      
    case connectRequest:com.stratio.meta.communication.ConnectRequest=>
      //connect(ICredentials, ConnectorClusterConfig)
      log.info("->"+"Receiving MetadataRequest")
      
    case closeRequest:com.stratio.meta.communication.CloseRequest=>
      //close(ClusterName)
      log.info("->"+"Receiving MetadataRequest")
      
    case isConnectedRequest:com.stratio.meta.communication.IsConnectedRequest=>
      //isConnected(ClusterName)
      log.info("->"+"Receiving MetadataRequest")
      
      
    case storageEngineRequest:com.stratio.meta.communication.StorageEngineRequest=>
      //getStorageEngine()
      log.info("->"+"Receiving MetadataRequest")

    case queryEngineRequest:com.stratio.meta.communication.QueryEngineRequest=>
      //getQueryEngine()d
      log.info("->"+"Receiving MetadataRequest")
      * 
      */

    case msg:getConnectorName=>
    {
      sender ! replyConnectorName(connectorName)
    }

    case s:String=>
      println("->"+"Receiving String: {}"+s)
      log.info("->"+"Receiving String: {}",s)

    case MemberUp(member) =>
      println("member up")
      log.info("*******Member is Up: {} {}!!!!!", member.toString ,member.getRoles)
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
}
