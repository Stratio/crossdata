package com.stratio.connectors

import akka.actor.{ActorRef, ActorSystem}
import akka.routing.RoundRobinRouter
import com.stratio.connectors.config.ConnectConfig
import com.stratio.meta.common.connector.IConnector
import com.typesafe.config.ConfigFactory
import org.apache.log4j.{Logger, BasicConfigurator}

//import com.stratio.connector.cassandra.CassandraConnector

object ConnectorApp extends ConnectorApp{
  def main(args: Array[String]): Unit = {
    BasicConfigurator.configure()
    //if (args.length == 0) println(usage)
    val options = nextOption(Map(),args.toList)
    var connectortype:Option[String]=options.get( Symbol("connectortype"))
    var port:Option[String]=options.get(Symbol("port"))
    if(port==None)port=Some("2551")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    if(connectortype==None)connectortype=Some("cassandra")
    val c=getConnector(connectortype.get.asInstanceOf[String])
    startup(c,Seq(port.get.asInstanceOf[String]),config)
  }
}

class ConnectorApp  extends ConnectConfig {

  private var connector=null

  val usage = """Usage:
      connectorApp [--port <port number>] [--connector-type <connector type name>]
  """
  lazy val system = ActorSystem(clusterName, config)
  override lazy val logger =Logger.getLogger(classOf[ConnectorApp])
  var actorClusterNode:ActorRef=null

  def getConnector(connectortype:String):IConnector={
    connectortype match {
      //case "cassandra" => new CassandraConnector
      case _ => null //new CassandraConnector
    }
  }

  type OptionMap = Map[Symbol, String]
  def nextOption(map : OptionMap, list: List[String]) : OptionMap = {
       def isSwitch(s : String) = (s(0) == '-')
       list match {
        case Nil => map
        case "--port" :: value :: tail =>
                               nextOption(map ++ Map('port -> value), tail)
        case "--connector-type" :: value :: tail =>
                               nextOption(map ++ Map('connectortype -> value), tail)
        case option :: tail => 
            println("Unknown option "+option) 
        	println(usage)
            exit(1) 
       }
  }

  def startup(connector:IConnector,port:String,config:com.typesafe.config.Config): ActorRef= {
    startup(connector,Array(port),config)
  }

  def startup(connector:IConnector,ports:Array[String],config:com.typesafe.config.Config): ActorRef= {
    startup(connector,ports.toList,config)
  }

  def startup(connector:IConnector,ports: Seq[String],config:com.typesafe.config.Config): ActorRef= {
    ports foreach { port =>
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
      actorClusterNode=system.actorOf(ConnectorActor.props(connector.getConnectorName,connector).withRouter(RoundRobinRouter(nrOfInstances=num_connector_actor)),"ConnectorActor")
      actorClusterNode ! "I'm in!!!"
    }
    actorClusterNode
  }

  def shutdown()= {
  }

  def stop()= {
    actorClusterNode ! shutdown()
    shutdown() //will be implemented by the concrete connector
    system.shutdown()
  }


  def startup(connector:IConnector):ActorRef={
    actorClusterNode=system.actorOf(ConnectorActor.props(connector.getConnectorName,connector).withRouter(RoundRobinRouter(nrOfInstances=num_connector_actor)),"ConnectororActor")
    actorClusterNode ! "I'm in!!!"
    actorClusterNode

  }
  def startup(connector:IConnector, port:String):ActorRef={
    startup(connector,Array(port),config)
  }

}
