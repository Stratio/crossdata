package com.stratio.meta.server.utilities

import com.stratio.meta.core.engine.{Engine, EngineConfig}

/**
 * To generate unit test of proxy actor
 */
object createEngine {
  def create():Engine={
  val engineConfig: EngineConfig = {
    val result=new EngineConfig
    result.setCassandraHosts(Array[String]("127.0.0.1"))
    result.setCassandraPort(9042)
    //result.setKafkaServer("127.0.0.1")
    //result.setKafkaPort(9092)
    //result.setZookeeperServer("127.0.0.1")
    //result.setZookeeperPort(2181)
    result.setSparkMaster("local[2]")
    result
  }
  new Engine(engineConfig)
  }
}
