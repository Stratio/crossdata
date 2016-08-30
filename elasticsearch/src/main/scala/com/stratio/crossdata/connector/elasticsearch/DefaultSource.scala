/**
  * Licensed to Elasticsearch under one or more contributor
  * license agreements. See the NOTICE file distributed with
  * this work for additional information regarding copyright
  * ownership. Elasticsearch licenses this file to you under
  * the Apache License, Version 2.0 (the "License"); you may
  * not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  **
  *http://www.apache.org/licenses/LICENSE-2.0
  **
  *Unless required by applicable law or agreed to in writing,
  *software distributed under the License is distributed on an
  *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  *KIND, either express or implied.  See the License for the
  *specific language governing permissions and limitations
  *under the License.
  **
  *Modifications and adaptations - Copyright (C) 2015 Stratio (http://stratio.com)
*/
package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.IndexAndTypes
import com.sksamuel.elastic4s.mappings._
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.connector.TableInventory.Table
import com.stratio.crossdata.connector.{TableInventory, TableManipulation}
import org.apache.spark.sql.SaveMode.{Append, ErrorIfExists, Ignore, Overwrite}
import org.apache.spark.sql.sources.{BaseRelation, CreatableRelationProvider, DataSourceRegister, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.{BooleanType, DateType, DoubleType, FloatType, IntegerType, LongType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.elasticsearch.hadoop.cfg.ConfigurationOptions
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._
import org.elasticsearch.hadoop.util.Version
import org.elasticsearch.hadoop.{EsHadoopIllegalArgumentException, EsHadoopIllegalStateException}
import org.elasticsearch.spark.sql.ElasticsearchXDRelation

import scala.util.Try


object DefaultSource {
  val DataSourcePushDown: String = "es.internal.spark.sql.pushdown"
  val DataSourcePushDownStrict: String = "es.internal.spark.sql.pushdown.strict"
  val ElasticNativePort = "es.nativePort"
  val ElasticCluster = "es.cluster"
  val ElasticIndex = "es.index"
}

/**
 * This class is used by Spark to create a new  [[ElasticsearchXDRelation]]
 */
class DefaultSource extends RelationProvider with SchemaRelationProvider
                                              with CreatableRelationProvider
                                              with TableInventory
                                              with DataSourceRegister
                                              with TableManipulation
                                              with SparkLoggerComponent {

  import DefaultSource._

  Version.logVersion()

  override def shortName(): String = "elasticsearch"

  override def createRelation(@transient sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    new ElasticsearchXDRelation(params(parameters), sqlContext)
  }

  override def createRelation(@transient sqlContext: SQLContext, parameters: Map[String, String], schema: StructType): BaseRelation = {
    new ElasticsearchXDRelation(params(parameters), sqlContext, Some(schema))
  }

  override def createRelation(@transient sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String],
                              data: DataFrame): BaseRelation = {

    val relation = new ElasticsearchXDRelation(params(parameters), sqlContext, Some(data.schema))
    mode match {
      case Append =>
        relation.insert(data, overwrite = false)
      case Overwrite =>
        relation.insert(data, overwrite = true)
      case ErrorIfExists =>
        if (relation.isEmpty()) relation.insert(data, overwrite = false)
        else throw new EsHadoopIllegalStateException(s"Index ${relation.cfg.getResourceWrite} already exists")
      case Ignore =>
        if (relation.isEmpty()) {
          relation.insert(data, overwrite = false)
        }
    }
    relation
  }

  /**
   * Validates the input parameters, defined in https://www.elastic.co/guide/en/elasticsearch/hadoop/current/configuration.html
    *
    * @param parameters a Map with the configurations parameters
   * @return the validated map.
   */
  private def params(parameters: Map[String, String], resourceValidation: Boolean = true) = {

    // '.' seems to be problematic when specifying the options
    val params = parameters.map { case (k, v) => (k.replace('_', '.'), v) }.map { case (k, v) =>
      if (k.startsWith("es.")) (k, v)
      else if (k == "path") (ConfigurationOptions.ES_RESOURCE, v)
      else if (k == "pushdown") (DataSourcePushDown, v)
      else if (k == "strict") (DataSourcePushDownStrict, v)
      else ("es." + k, v)
    }

    if (resourceValidation) {
      if (params.get(ConfigurationOptions.ES_RESOURCE_READ).orElse(params.get(ConfigurationOptions.ES_RESOURCE)).isEmpty)
        throw new EsHadoopIllegalArgumentException("resource must be specified for Elasticsearch resources.")
    }

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

    ElasticSearchConnectionUtils.listTypes(params(options, resourceValidation = false))
  }

  override def createExternalTable(context: SQLContext,
                                   tableName: String,
                                   databaseName: Option[String],
                                   schema: StructType,
                                   options: Map[String, String]): Option[Table] = {

    val (index, typeName) = ElasticSearchConnectionUtils.extractIndexAndType(options).orElse(databaseName.map((_, tableName))).
      getOrElse(throw new RuntimeException(s"$ES_RESOURCE is required when running CREATE EXTERNAL TABLE"))

    // TODO specified mapping is not the same that the resulting mapping inferred once some data is indexed
    val elasticSchema = schema.map { field =>
      field.dataType match {
        case IntegerType => new IntegerFieldDefinition(field.name)
        case StringType => new StringFieldDefinition(field.name)
        case DateType => new DateFieldDefinition(field.name)
        case BooleanType => new BooleanFieldDefinition(field.name)
        case DoubleType => new DoubleFieldDefinition(field.name)
        case LongType => new StringFieldDefinition(field.name)
        case FloatType => new FloatFieldDefinition(field.name)
      }
    }

    val indexType = IndexAndTypes(index, typeName)

    try {
      ElasticSearchConnectionUtils.withClientDo(options) { client =>
        if(!client.execute(indexExists(index)).await.isExists){
          client.execute {
            createIndex(index).mappings()
          }.await
        }
      }
    }

    try {
      ElasticSearchConnectionUtils.withClientDo(options) { client =>
        client.execute {
          putMapping(indexType) fields elasticSchema
        }.await
      }
      Option(Table(typeName, Option(index), Option(schema)))
    } catch {
      case e: Exception =>
        logError(e.getMessage, e)
        None
    }
  }

  override def dropExternalTable(context: SQLContext,
                                 options: Map[String, String]): Try[Unit] = {

    if(ElasticSearchConnectionUtils.numberOfTypes(options) == 1) {
      val (index, _) = ElasticSearchConnectionUtils.extractIndexAndType(options).get

      Try {
        ElasticSearchConnectionUtils.withClientDo(options){ client =>
          client.execute {
            deleteIndex(index)
          }.await
        }
      }
    } else {
      sys.error("Cannot remove table from ElasticSearch if more than one table is persisted in the same index. Please remove it natively")
    }

  }

}
