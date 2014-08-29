package com.stratio.connector

import akka.actor.{ActorSystem,Props}
import com.typesafe.config.ConfigFactory
<<<<<<< HEAD
import com.stratio.connector.cassandra.CassandraConnector
import com.stratio.meta.common.connector.IConnector
import java.io.File
=======
import akka.actor.ActorSystem
import akka.actor.Props
//import com.stratio.connector.cassandra.CassandraConnector
>>>>>>> 74602793e4b6c396387a6559e1cb0dcef738fccc

object ConnectorApp {
    val usage = """Usage: 
      connectorApp [--port <port number>] [--connectortype <connector type name>] 
    """

  def main(args: Array[String]): Unit = {
    //if (args.length == 0) println(usage)
    val options = nextOption(Map(),args.toList)
    var connectortype:Option[String]=options.get( Symbol("connectortype"))
    var port:Option[String]=options.get(Symbol("port"))
    if(port==None)port=Some("2551")
    if(connectortype==None)connectortype=Some("cassandra")
    val c=getConnector(connectortype.get.asInstanceOf[String])
    startup(c,Seq(port.get.asInstanceOf[String]) )
  }

  def getConnector(connectortype:String):IConnector={
    connectortype match {
      case "cassandra" => new CassandraConnector
      case _ => new CassandraConnector
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

<<<<<<< HEAD
  def startup(connector:IConnector,ports: Seq[String]): Unit = {
    println("using connector with datastorename="+connector.getDatastoreName())
=======
  def startup(ports: Seq[String]): Unit = {
	   //TODO: Compile problems
    /*val connector=new CassandraConnector()
    println("datastorename="+connector.getDatastoreName())*/
>>>>>>> 74602793e4b6c396387a6559e1cb0dcef738fccc

    ports foreach { port =>
      // Override the configuration of the port
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
      	withFallback(ConfigFactory.load())

      // Create an Akka system
      val system = ActorSystem("MetaServerCluster", config)
      // Create an actor that handles cluster domain events
      val actorClusterNode=system.actorOf(Props[ClusterListener], name = "clusterListener")
      
      actorClusterNode ! "I'm in!!!"
    }
  }

}
