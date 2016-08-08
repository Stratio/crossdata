/*
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
package org.apache.spark.sql.crossdata.daos.impl

import org.apache.spark.sql.crossdata.daos.{EphemeralTableStatusMapDAO => EphTableStatusMapDAO}
class EphemeralTableStatusMapDAO(opts: Map[String, String], subPath: Option[String] = None)
    extends EphTableStatusMapDAO {

  val memoryMap = opts
  override lazy val config: Config = new DummyConfig(subPath)
}
