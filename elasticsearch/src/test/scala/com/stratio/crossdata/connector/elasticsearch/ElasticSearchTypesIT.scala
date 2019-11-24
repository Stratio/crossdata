/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.elasticsearch

import java.util.Date

import org.apache.spark.sql.crossdata.ExecutionType.Native
import org.joda.time.DateTime
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
//  "ageInMillis" typed LongType
  "A ElasticSearchQueryProcessor " should "Return types in correct format" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT id, age, description, enrolled, name, birthday, salary, ageInMillis FROM $Type where id = 2")
    val result = dataframe.collect(Native)

    //Expectations
    result(0).get(0).isInstanceOf[Integer] should be (true)
    result(0).get(1).isInstanceOf[Integer] should be (true)
    result(0).get(2).isInstanceOf[String] should be (true)
    result(0).get(3).isInstanceOf[Boolean] should be (true)
    result(0).get(4).isInstanceOf[String] should be (true)

    result(0).get(5).isInstanceOf[Date] should be (true)
    result(0).get(6).isInstanceOf[Double] should be (true)
    result(0).get(7).isInstanceOf[Long] should be (true)

    result(0).getInt(0) should be (2)
    result(0).getInt(1) should be (12)
    result(0).getString(2) should be ("A 2description about the Name2")
    result(0).getBoolean(3) should be (true)
    result(0).getString(4) should be ("Name 2")
    result(0).getDate(5) should be (DateTime.parse("1982-01-01T10:00:00-00:00").toDate)
    result(0).getDouble(6) should be (2001.0)
    result(0).getLong(7) should be (DateTime.parse("1982-01-01T10:00:00-00:00").getMillis)
  }
}
