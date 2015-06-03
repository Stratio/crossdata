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

package com.stratio.crossdata.server.config

import com.typesafe.config.Config


object NumberActorConfig {

  val API_ACTOR_NUM = "config.akka.number.api-actor "
  val COORDINATOR_ACTOR_NUM = "config.akka.number.coordinator-actor"
  val EXECUTOR_ACTOR_NUM = "config.akka.number.executor-actor"
  val NORMALIZER_ACTOR_NUM = "config.akka.number.normalizer-actor"
  val PARSER_ACTOR_NUM = "config.akka.number.parser-actor"
  val PLANNER_ACTOR_NUM = "config.akka.number.planner-actor"
  val QUERY_ACTOR_NUM = "config.akka.number.query-actor"
  val SERVER_ACTOR_NUM = "config.akka.number.server-actor"
  val VALIDATOR_ACTOR_NUM = "config.akka.number.validator-actor"

}

trait NumberActorConfig {

  lazy val num_api_actor: Int = config.getString(NumberActorConfig.API_ACTOR_NUM).toInt
  lazy val num_coordinator_actor: Int = config.getString(NumberActorConfig.COORDINATOR_ACTOR_NUM).toInt
  lazy val num_executor_actor: Int = config.getString(NumberActorConfig.EXECUTOR_ACTOR_NUM).toInt
  lazy val num_normalizer_actor: Int = config.getString(NumberActorConfig.NORMALIZER_ACTOR_NUM).toInt
  lazy val num_parser_actor: Int = config.getString(NumberActorConfig.PARSER_ACTOR_NUM).toInt
  lazy val num_planner_actor: Int = config.getString(NumberActorConfig.PLANNER_ACTOR_NUM).toInt
  lazy val num_query_actor: Int = config.getString(NumberActorConfig.QUERY_ACTOR_NUM).toInt
  lazy val num_server_actor: Int = config.getString(NumberActorConfig.SERVER_ACTOR_NUM).toInt
  lazy val num_validator_actor: Int = config.getString(NumberActorConfig.VALIDATOR_ACTOR_NUM).toInt

  def config: Config = ???

}
