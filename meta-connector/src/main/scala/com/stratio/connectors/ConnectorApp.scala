package com.stratio.connector

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
//import com.stratio.connector.cassandra.CassandraConnector

object ConnectorApp {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      startup(Seq("2551", "2552", "0"))
    else
      startup(args)
  }

  def startup(ports: Seq[String]): Unit = {
	   //TODO: Compile problems
    /*val connector=new CassandraConnector()
    println("datastorename="+connector.getDatastoreName())*/

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
