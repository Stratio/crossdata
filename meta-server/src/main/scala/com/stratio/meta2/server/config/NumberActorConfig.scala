package com.stratio.meta2.server.config

import com.typesafe.config.Config


object NumberActorConfig {

  val API_ACTOR_NUM = "config.akka.number.api-actor "
  val CONNECTOR_MANAG_ACTOR_NUM = "config.akka.number.connector-manager-actor"
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
  lazy val num_connector_manag_actor: Int = config.getString(NumberActorConfig.CONNECTOR_MANAG_ACTOR_NUM).toInt
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
