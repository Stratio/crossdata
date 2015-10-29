package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s._
import com.stratio.crossdata.connector.elasticsearch.ElasticSearchConnectionUtils._

object ElasticSearchMetadataBuilder {


  def listTypes(options: Map[String, String] ): Any ={

    buildClient(options).admin.indices().getMappings(null).

  null
  }

}
