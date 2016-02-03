package com.stratio.crossdata.connector.elasticsearch

import java.util.Date

import org.apache.spark.sql.crossdata.ExecutionType.Native
import org.elasticsearch.common.joda.time.DateTime
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ElasticSearchTypesIT extends ElasticWithSharedContext {


//  "id" typed IntegerType,
//  "age" typed IntegerType,
//  "description" typed StringType,
//  "enrolled" typed BooleanType,
//  "name" typed StringType index NotAnalyzed,
//  "birthday" typed DateType,
//  "salary" typed DoubleType,
//  "ageInMilis" typed LongType
  "A ElasticSearchQueryProcessor " should "Return types in correct format" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT * FROM $Type where id = 1")
    val result = dataframe.collect(Native)

    //Expectations
    result(0).get(0).isInstanceOf[Integer] should be (true)
    result(0).get(1).isInstanceOf[Integer] should be (true)
    result(0).get(2).isInstanceOf[String] should be (true)
    result(0).get(3).isInstanceOf[Boolean] should be (true)
    result(0).get(4).isInstanceOf[String] should be (true)

    result(0).get(6).isInstanceOf[Date] should be (true)
    result(0).get(7).isInstanceOf[Double] should be (true)
    result(0).get(8).isInstanceOf[Long] should be (true)

    result(0).getInt(0) should be (1)
    result(0).getInt(1) should be (11)
    result(0).getString(2) should be ("A 1description about the Name1")
    result(0).getBoolean(3) should be (false)
    result(0).getString(4) should be ("Name 1")

    result(0).getDate(6) should be (DateTime.parse("1981-01-01T10:00:00-00:00").toDate)
    result(0).getDouble(7) should be (1000.5)
    result(0).getLong(8) should be (DateTime.parse("1981-01-01T10:00:00-00:00").getMillis)
  }
}
