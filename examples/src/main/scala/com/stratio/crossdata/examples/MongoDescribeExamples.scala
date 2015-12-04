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
package com.stratio.crossdata.examples
import com.mongodb.casbah.MongoClient
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.crossdata.XDContext

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