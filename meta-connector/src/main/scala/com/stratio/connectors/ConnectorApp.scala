package com.stratio.connector

import akka.actor.{ActorSystem,Props}
import com.typesafe.config.ConfigFactory
//import com.stratio.connector.cassandra.CassandraConnector
import com.stratio.meta.common.connector.IConnector
import akka.actor.ActorSystem
import akka.actor.Props
//import com.stratio.connector.cassandra.CassandraConnector

class ConnectorApp {
    val usage = """Usage: 
      connectorApp [--port <port number>] [--connectortype <connector type name>] 
    """

  def main(args: Array[String]): Unit = {

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

  def startup(connector:IConnector,port:String,config:com.typesafe.config.Config): Unit = {
    return startup(connector,Array(port),config)
  }

  def startup(connector:IConnector,ports:Array[String],config:com.typesafe.config.Config): Unit = {
    return startup(connector,ports.toList,config)
  }

  def startup(connector:IConnector,ports: Seq[String],config:com.typesafe.config.Config): Unit = {
    println("using connector with datastorename="+connector.getDatastoreName())
    ports foreach { port =>
      // Override the configuration of the port
      //val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())

      // Create an Akka system
      val system = ActorSystem("MetaServerCluster", config)
      // Create an actor that handles cluster domain events
      val actorClusterNode=system.actorOf(Props[ClusterListener], name = "clusterListener")
      
      actorClusterNode ! "I'm in!!!"
    }
  }

}
