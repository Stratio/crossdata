package com.stratio.crossdata.connector.elasticsearch

import org.apache.spark.sql.{DataFrame, SaveMode, SQLContext}
import org.apache.spark.sql.sources.{CreatableRelationProvider, SchemaRelationProvider, BaseRelation, RelationProvider}
import org.apache.spark.sql.types.StructType
import org.elasticsearch.hadoop.{EsHadoopIllegalArgumentException, EsHadoopIllegalStateException}
import org.elasticsearch.hadoop.cfg.ConfigurationOptions
import org.elasticsearch.spark.sql.ElasticSearchXDRelation
import org.apache.spark.sql.SaveMode.Append
import org.apache.spark.sql.SaveMode.ErrorIfExists
import org.apache.spark.sql.SaveMode.Ignore
import org.apache.spark.sql.SaveMode.Overwrite

class DefaultSource extends RelationProvider with SchemaRelationProvider with CreatableRelationProvider {

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

  private def params(parameters: Map[String, String]) = {
    // . seems to be problematic when specifying the options
    val params = parameters.map { case (k, v) => (k.replace('_', '.'), v) }.map { case (k, v) =>
      if (k.startsWith("es.")) (k, v)
      else if (k == "path") ("es.resource", v)
      else if (k == "pushdown") (DATA_SOURCE_PUSH_DOWN, v)
      else if (k == "strict") (DATA_SOURCE_PUSH_DOWN_STRICT, v)
      else ("es." + k, v)
    }
    params.getOrElse(ConfigurationOptions.ES_RESOURCE, throw new EsHadoopIllegalArgumentException("resource must be specified for Elasticsearch resources."))
    params
  }

  val DATA_SOURCE_PUSH_DOWN: String = "es.internal.spark.sql.pushdown"
  val DATA_SOURCE_PUSH_DOWN_STRICT: String = "es.internal.spark.sql.pushdown.strict"
}
