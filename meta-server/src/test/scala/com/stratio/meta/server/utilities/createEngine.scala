package com.stratio.meta.server.utilities

import com.stratio.meta.core.engine.{Engine, EngineConfig}

/**
 * Created by aalcocer on 4/10/14.
 * To generate unit test of proxy actor
 */
object createEngine {
  def create():Engine={
  val engineConfig: EngineConfig = {
    val result=new EngineConfig
    result.setCassandraHosts(Array[String]("127.0.0.1"))
    result.setCassandraPort(9042)
    result
  }
  new Engine(engineConfig)
  }
}
