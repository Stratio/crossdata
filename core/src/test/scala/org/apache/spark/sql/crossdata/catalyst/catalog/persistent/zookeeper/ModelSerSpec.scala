package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.catalog.{CatalogStorageFormat, CatalogTable, CatalogTableType}
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.TableModel
import org.apache.spark.sql.crossdata.serializers.XDSerializationTest.TestCase
import org.apache.spark.sql.crossdata.serializers.{CrossdataCommonSerializer, StructTypeSerializer, XDSerializationTest}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.json4s.{DefaultFormats, Formats}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ModelSerSpec extends XDSerializationTest[TableModel] with CrossdataCommonSerializer {

  override implicit val formats: Formats = json4sJacksonFormats

  lazy val table = "tableA"
  lazy val db = "databaseA"

  lazy val model = TableModel(CatalogTable(
    TableIdentifier(table, Some(db)),
    CatalogTableType.EXTERNAL,
    CatalogStorageFormat.empty,
    new StructType(Array(StructField("field1", StringType)))
  ))

  override def testCases: Seq[TestCase] = Seq(
    TestCase("marshall & unmarshall TableModel instances", model)
  )


}
