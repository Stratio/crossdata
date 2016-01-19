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

package org.apache.spark.sql.crossdata.daos

import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import com.stratio.common.utils.components.dao.DAOComponent
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.common.utils.components.repository.impl.ZookeeperRepositoryComponent
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.models.TableModel
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.json4s.jackson.Serialization._

trait TableDAOComponent extends DAOComponent[String, Array[Byte], TableModel]
with ZookeeperRepositoryComponent with TypesafeConfigComponent with SparkLoggerComponent with CrossdataSerializer {

  val dao: DAO = new TableDAO {}

  trait TableDAO extends DAO {

    def fromVtoM(v: Array[Byte])(implicit manifest: Manifest[TableModel]): TableModel =
      read[TableModel](new String(v))

    def fromMtoV(m: TableModel)(implicit manifest: Manifest[TableModel]): Array[Byte] =
      write(m).getBytes

    def entity = TablesPath
  }

}