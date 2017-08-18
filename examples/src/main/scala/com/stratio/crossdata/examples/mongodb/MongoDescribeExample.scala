/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.examples.mongodb

import com.mongodb.casbah.MongoClient
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkConf, SparkContext}

object MongoDescribeExample extends App with MongoDefaultConstants {
  val mongoClient = prepareEnvironment()
  withCrossdataContext { xdContext =>
    xdContext.sql(s"DESCRIBE highschool.studentsTestDataTypes").show()
  }
  private def withCrossdataContext(commands: XDContext => Unit) = {
    val sparkConf = new SparkConf().
      setAppName("MongoExample").
      setMaster("local[4]")
    val sc = new SparkContext(sparkConf)
    try {
      val xdContext = new XDContext(sc)
      commands(xdContext)
    } finally {
      sc.stop()
    }
  }
  def prepareEnvironment(): MongoClient = {
    val mongoClient = MongoClient(MongoHost,MongoPort)
    mongoClient
  }
}