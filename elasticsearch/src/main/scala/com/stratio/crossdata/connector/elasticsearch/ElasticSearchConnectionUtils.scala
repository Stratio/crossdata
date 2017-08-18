/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import com.stratio.crossdata.connector.TableInventory.Table
import com.stratio.crossdata.connector.elasticsearch.DefaultSource._
import org.elasticsearch.client.IndicesAdminClient
import org.elasticsearch.cluster.metadata.MappingMetaData
import org.elasticsearch.common.collect.ImmutableOpenMap
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._

object ElasticSearchConnectionUtils {

  def withClientDo[T](parameters: Map[String, String])(f: ElasticClient => T): T = {
    val client = buildClient(parameters)
    try {
      f(client)
    } finally {
      client.close()
    }
  }


  private def buildClient(parameters: Map[String, String]): ElasticClient = {
    val host: String = parameters.getOrElse(ES_NODES, ES_NODES_DEFAULT)
    // TODO support for multiple host, no documentation found with expected format.
    val port: Int = parameters.getOrElse(ElasticNativePort, "9300").toInt
    val clusterName = parameters(ElasticCluster)

    val uri = ElasticsearchClientUri(s"elasticsearch://$host:$port")

    val settings = Settings.settingsBuilder().put("cluster.name", clusterName).build()
    ElasticClient.transport(settings, uri)
  }

  def extractIndexAndType(options: Map[String, String]): Option[(String, String)] = {
    options.get(ES_RESOURCE).map{ indexType =>
      val indexTypeArray = indexType.split("/")
      require(indexTypeArray.size==2, s"$ES_RESOURCE option has an invalid format")
      (indexTypeArray(0), indexTypeArray(1))
    }
  }

  def listTypes(options: Map[String, String]): Seq[Table] = {

    val adminClient = buildClient(options).admin.indices()

    val indexType: Option[(String, String)] =  extractIndexAndType(options)
    val index = indexType.map(_._1).orElse(options.get(ElasticIndex))

    index.fold(listAllIndexTypes(adminClient)){indexName =>
      listIndexTypes(adminClient, indexName, indexType.map(_._2))
    }

  }

  import collection.JavaConversions._
  private def listAllIndexTypes(adminClient: IndicesAdminClient): Seq[Table] = {

    val mappings: ImmutableOpenMap[String, ImmutableOpenMap[String, MappingMetaData]]  = adminClient.prepareGetIndex().get().mappings
    mappings.keys().flatMap { index =>
      getIndexDetails(index.value, mappings.get(index.value))
    } toSeq

  }

  def numberOfTypes(options: Map[String, String]): Int = {
    val adminClient = buildClient(options).admin.indices()

    val indexType: Option[(String, String)] =  extractIndexAndType(options)
    val index = indexType.map(_._1).orElse(options.get(ElasticIndex)) getOrElse sys.error("Index not found")

    adminClient.prepareGetIndex().addIndices(index).get().mappings().get(index).size()
  }

  private def listIndexTypes(adminClient: IndicesAdminClient, indexName: String, typeName: Option[String] = None): Seq[Table] = {

    val elasticBuilder = adminClient.prepareGetIndex().addIndices(indexName)
    val elasticBuilderWithTypes = typeName.fold(elasticBuilder)(elasticBuilder.addTypes(_))
    val mappings: ImmutableOpenMap[String, ImmutableOpenMap[String, MappingMetaData]] =  elasticBuilderWithTypes.get().mappings
    getIndexDetails(indexName, mappings.get(indexName))

  }

  private def getIndexDetails(indexName: String, indexData: ImmutableOpenMap[String, MappingMetaData]): Seq[Table] = {
    val schema = None // Elasticsearch 'datasource' is already able to infer the schema
    indexData.keys().map(typeES => new Table(typeES.value, Some(indexName), schema)).toSeq
  }

}
