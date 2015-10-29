package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.ElasticClient
import com.stratio.crossdata.connector.elasticsearch.DefaultSource._
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._

object ElasticSearchConnectionUtils {



  def buildClient(parameters: Map[String, String]): ElasticClient = {
    val host: String = parameters.getOrElse(ES_NODES, ES_NODES_DEFAULT)
    val port: Int = parameters.getOrElse(ElasticNativePort, 9300).toString.toInt
    val clusterName: String = parameters.get(ElasticCluster).get

    val settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build()
    val client = ElasticClient.remote(settings, host, port)

    val mappingRequest: GetMappingsRequest = new GetMappingsRequest()
    client.admin.indices().getMappings(new GetMappingsRequest())
  }


}
