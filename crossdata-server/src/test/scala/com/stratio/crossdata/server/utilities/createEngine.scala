/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.server.utilities

import java.util.Random

import com.stratio.crossdata.core.engine.{Engine, EngineConfig}

/**
 * To generate unit test of proxy actor
 */
object createEngine {
  val port:Int=5900
  val timeout:Int=3000
  val nextInt:Int=100000

  def create(): Engine = {
    val engineConfig: EngineConfig = {
      val result = new EngineConfig
      result.setGridListenAddress("localhost")
      result.setGridContactHosts(Array[String]())
      result.setGridMinInitialMembers(1)
      result.setGridPort(port)
      result.setGridJoinTimeout(timeout)
      result.setGridPersistencePath("/tmp/com.stratio.crossdata-test-" + new Random().nextInt(nextInt))
      result
    }
    new Engine(engineConfig)
  }
}
