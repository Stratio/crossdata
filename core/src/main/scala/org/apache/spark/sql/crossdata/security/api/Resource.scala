package org.apache.spark.sql.crossdata.security.api

case class Resource(service: String, instances: Seq[String], resourceType: String, name: String)

object Resource {
  val wildCardAll = Resource("*", Seq("*"), "*", "*")
}