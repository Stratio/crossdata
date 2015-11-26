/**
Licensed to Elasticsearch under one or more contributor
license agreements. See the NOTICE file distributed with
this work for additional information regarding copyright
ownership. Elasticsearch licenses this file to you under
the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

Modifications and adaptations - Copyright (C) 2015 Stratio (http://stratio.com)
*/
package com.stratio.crossdata.connector.elasticsearch

import com.stratio.crossdata.connector.TableInventory
import com.stratio.crossdata.connector.TableInventory.Table
import org.apache.spark.sql.{DataFrame, SaveMode, SQLContext}
import org.apache.spark.sql.sources.{CreatableRelationProvider, SchemaRelationProvider, BaseRelation, RelationProvider}
import org.apache.spark.sql.types.StructType
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._
import org.elasticsearch.hadoop.{EsHadoopIllegalArgumentException, EsHadoopIllegalStateException}
import org.elasticsearch.hadoop.cfg.ConfigurationOptions
import org.elasticsearch.spark.sql.ElasticSearchXDRelation
import org.apache.spark.sql.SaveMode.Append
import org.apache.spark.sql.SaveMode.ErrorIfExists
import org.apache.spark.sql.SaveMode.Ignore
import org.apache.spark.sql.SaveMode.Overwrite


object DefaultSource{
  val DATA_SOURCE_PUSH_DOWN: String = "es.internal.spark.sql.pushdown"
  val DATA_SOURCE_PUSH_DOWN_STRICT: String = "es.internal.spark.sql.pushdown.strict"
  val ElasticNativePort = "es.nativePort"
  val ElasticCluster = "es.cluster"
  val ElasticIndex = "es.index"
}

/**
 * This class is used by Spark to create a new  [[ElasticSearchXDRelation]]
 */
class DefaultSource extends RelationProvider with SchemaRelationProvider with CreatableRelationProvider  with TableInventory {

  import DefaultSource._

  override def createRelation(@transient sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    new ElasticSearchXDRelation(params(parameters), sqlContext)
  }

  override def createRelation(@transient sqlContext: SQLContext, parameters: Map[String, String], schema: StructType): BaseRelation = {
    new ElasticSearchXDRelation(params(parameters), sqlContext, Some(schema))
  }

  override def createRelation(@transient sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame): BaseRelation = {
    val relation = new ElasticSearchXDRelation(params(parameters), sqlContext, Some(data.schema))
    mode match {
      case Append => relation.insert(data, false)
      case Overwrite => relation.insert(data, true)
      case ErrorIfExists => {
        if (relation.isEmpty()) relation.insert(data, false)
        else throw new EsHadoopIllegalStateException(s"Index ${relation.cfg.getResourceWrite} already exists")
      }
      case Ignore => if (relation.isEmpty()) {
        relation.insert(data, false)
      }
    }
    relation
  }

  /**
   * Validates the input parameters, defined in https://www.elastic.co/guide/en/elasticsearch/hadoop/current/configuration.html
   * @param parameters a Map with the configurations parameters
   * @return the validated map.
   */
  private def params(parameters: Map[String, String]) = {
    // . seems to be problematic when specifying the options
    val params = parameters.map { case (k, v) => (k.replace('_', '.'), v) }.map { case (k, v) =>
      if (k.startsWith("es.")) (k, v)
      else if (k == "path") ("es.resource", v)
      else if (k == "pushdown") (DATA_SOURCE_PUSH_DOWN, v)
      else if (k == "strict") (DATA_SOURCE_PUSH_DOWN_STRICT, v)
      else ("es." + k, v)
    }
    //TODO Validate required parameters


    params
  }

  /**
   * @inheritdoc
   */
  override def generateConnectorOpts(item: Table, userOpts: Map[String, String]): Map[String, String] = Map(
    ES_RESOURCE -> s"${item.database.get}/${item.tableName}"
  ) ++ userOpts

  /**
   * @inheritdoc
   */
  override def listTables(context: SQLContext, options: Map[String, String]): Seq[Table] = {

    Seq(ElasticCluster).foreach { opName =>
      if (!options.contains(opName)) sys.error( s"""Option "$opName" is mandatory for IMPORT TABLES""")
    }

    ElasticSearchConnectionUtils.listTypes(params(options))
  }
}
