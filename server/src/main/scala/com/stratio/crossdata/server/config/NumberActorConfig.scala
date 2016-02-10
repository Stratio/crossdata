/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
  * Copyright (C) 2015 Stratio (http://stratio.com)
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *         http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.stratio.crossdata.server.config

import com.typesafe.config.Config


object NumberActorConfig {
  val DefaultServerExecutorInstances = 5
  val ServerActorInstancesMin = "config.akka.number.server-actor-min"
  val ServerActorInstancesMax = "config.akka.number.server-actor-max"
}

trait NumberActorConfig {

  import NumberActorConfig.ServerActorInstancesMin
  import NumberActorConfig.ServerActorInstancesMax
  import NumberActorConfig.DefaultServerExecutorInstances
  lazy val minServerActorInstances: Int = Option(config.getString(ServerActorInstancesMin)).map(_.toInt).getOrElse(DefaultServerExecutorInstances)
  lazy val maxServerActorInstances: Int = Option(config.getString(ServerActorInstancesMax)).map(_.toInt).getOrElse(minServerActorInstances*2)
  def config: Config

}
