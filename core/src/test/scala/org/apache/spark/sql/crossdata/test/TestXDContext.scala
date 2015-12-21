/*
 * Modifications and adaptations - Copyright (C) 2015 Stratio (http://stratio.com)
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.crossdata.test

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.{SQLConf, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Try

object TestXDContext {

  def buildSparkContext(externalJars: Option[Seq[String]], externalFiles: Seq[String]): TestXDContext = {
    val defaultSparkConf = new SparkConf().set("spark.sql.testkey", "true").set("spark.io.compression.codec", "org.apache.spark.io.LZ4CompressionCodec")
    val externalMaster = Try(ConfigFactory.load().getString("spark.master"))

    def buildPath( relativePath: String) = s"${new java.io.File(".").getCanonicalPath}/../$relativePath"

    val (sparkMaster, sparkConf) = externalMaster.map { master =>
      (master, externalJars.map( jarList => defaultSparkConf.setJars( jarList.map(buildPath))).getOrElse(defaultSparkConf) )
    } getOrElse (("local[2]", defaultSparkConf))

    val sparkContext = new SparkContext(sparkMaster, "test-xdcontext", sparkConf)
    externalFiles.foreach(sparkContext.addFile)
    new TestXDContext(sparkContext)
  }

  def apply (jarPath: String) :TestXDContext =
    buildSparkContext(Some(Seq(jarPath)), Seq.empty )


  def apply (jarPathList: Seq[String], filePathList: Seq[String]) :TestXDContext =
    buildSparkContext(Some(jarPathList), filePathList)

  def apply (jarPathList: Seq[String]) :TestXDContext =
    buildSparkContext(Some(jarPathList), Seq.empty)


  def apply () = buildSparkContext( None, Seq.empty)


}

/**
 * A special [[SQLContext]] prepared for testing.
 */
private[sql] class TestXDContext private(sc: SparkContext) extends XDContext(sc) { self =>

  // Use fewer partitions to speed up testing
  protected[sql] override def createSession(): SQLSession = new this.SQLSession()


  protected[sql] class SQLSession extends super.SQLSession {
    protected[sql] override lazy val conf: SQLConf = new SQLConf {
      override def numShufflePartitions: Int = 3
    }
  }

}
