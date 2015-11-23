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
package com.stratio.crossdata.connector.elasticsearch

import java.util

import com.sksamuel.elastic4s.{ElasticsearchClientUri, ElasticClient}
import com.stratio.crossdata.connector.TableInventory.Table
import com.stratio.crossdata.connector.elasticsearch.DefaultSource._
import org.apache.spark.sql.types._
import org.elasticsearch.client.IndicesAdminClient
import org.elasticsearch.cluster.metadata.MappingMetaData
import org.elasticsearch.common.collect.ImmutableOpenMap
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._

import scala.collection.mutable

object ElasticSearchConnectionUtils {


  def buildClient(parameters: Map[String, String]): ElasticClient = {
    val host: String = parameters.getOrElse(ES_NODES, ES_NODES_DEFAULT) //TODO support for multiple host, no documentation found with expected format.
    val port: Int = parameters.getOrElse(ElasticNativePort, "9300").toInt
 //   val clusterName = parameters(ElasticCluster)

    val uri = ElasticsearchClientUri(s"elasticsearch://$host:$port")

   // val settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build()
    ElasticClient.remote(uri)
  }

  def extractIndexAndType(options: Map[String, String]): (String, String) = {
    val resource = options.get(ES_RESOURCE).get.split("/")
    (resource(0), resource(1))
  }

  def listTypes(options: Map[String, String]): Seq[Table] = {

    val adminClient = buildClient(options).admin.indices()
    options.get(ElasticIndex).fold(listAllIndexTypes(adminClient))(indexName => listIndexTypes(adminClient, indexName))
  }

  import collection.JavaConversions._
  private def listAllIndexTypes(adminClient: IndicesAdminClient): Seq[Table] = {

    val mappings: ImmutableOpenMap[String, ImmutableOpenMap[String, MappingMetaData]]  = adminClient.prepareGetIndex().get().mappings
    mappings.keys().flatMap { index =>
      getIndexDetails(index.value, mappings.get(index.value))
    } toSeq

  }

  private def listIndexTypes(adminClient: IndicesAdminClient, indexName: String): Seq[Table] = {

    val mappings: ImmutableOpenMap[String, ImmutableOpenMap[String, MappingMetaData]]  = adminClient.prepareGetIndex().addIndices(indexName).get().mappings
    getIndexDetails(indexName, mappings.get(indexName))

  }

  private def getIndexDetails(indexName:String, indexData: ImmutableOpenMap[String, MappingMetaData]): Seq[Table] ={
    indexData.keys().map(typeES => new Table(typeES.value, Some(indexName), Some(buildStructType(indexData.get(typeES.value))))).toSeq
  }


  private def convertType(typeName:String): DataType = {

    typeName match {
      case "string"=> StringType
      case "integer" => IntegerType
      case "date" => DateType
      case "boolean" => BooleanType
      case "double" => DoubleType
      case "long" => LongType
      case "float" => FloatType
      case "null" => NullType
      case _ => throw new RuntimeException (s"The type $typeName isn't supported yet in Elasticsearch connector.")
    }

  }

  private def buildStructType(mapping: MappingMetaData): StructType ={

    val esFields = mapping.sourceAsMap().get("properties").asInstanceOf[java.util.LinkedHashMap[String,java.util.LinkedHashMap[String, String]]].toMap;

    val fields: Seq[StructField] = esFields.map {
          case (colName, propertyValueMap) => StructField(colName, convertType(propertyValueMap.get("type")), false)
    }(collection.breakOut)

    StructType(fields)
  }
}
