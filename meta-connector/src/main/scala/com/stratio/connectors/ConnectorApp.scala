package com.stratio.connector

import akka.actor.{ActorRef, ActorSystem, Props}
import com.stratio.connectors.config.ConnectConfig
import com.stratio.meta.common.connector.IConnector
import com.typesafe.config.ConfigFactory
import org.apache.log4j.{BasicConfigurator, Logger}

//import com.stratio.connector.cassandra.CassandraConnector

class ConnectorApp  extends ConnectConfig {

    override lazy val logger = Logger.getLogger(classOf[ConnectorApp])
    val usage = """Usage: 
      connectorApp [--port <port number>] [--connectortype <connector type name>] 
    """
    lazy val system = ActorSystem(clusterName, config)
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
    logger.info("startup 1")
    return startup(connector,Array(port),config)
  }

  def startup(connector:IConnector,ports:Array[String],config:com.typesafe.config.Config): ActorRef= {
    logger.info("startup 2")
    return startup(connector,ports.toList,config)
  }

  def startup(connector:IConnector,ports: Seq[String],config:com.typesafe.config.Config): ActorRef= {
    logger.info("startup 3")
    var actorClusterNode:ActorRef=null
    ports foreach { port =>
      // Override the configuration of the port
      //val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())

      // Create an Akka system

      // Create an actor that handles cluster domain events
      actorClusterNode=system.actorOf(Props[ClusterListener], name = actorName)
      actorClusterNode ! "I'm in!!!"
    }
    actorClusterNode
  }
  def startup(connector:IConnector){
    logger.info("startup 4")


      // Create an Akka system

      // Create an actor that handles cluster domain events
     val actorClusterNode=system.actorOf(Props[ClusterListener], name = actorName)
     actorClusterNode ! "I'm in!!!"


  }

}
