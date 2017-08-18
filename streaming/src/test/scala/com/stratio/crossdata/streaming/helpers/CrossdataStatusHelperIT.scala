package com.stratio.crossdata.streaming.helpers

import com.stratio.crossdata.streaming.test.{CommonValues, BaseStreamingXDTest}
import org.apache.curator.test.TestingServer
import org.apache.curator.utils.CloseableUtils
import org.apache.spark.sql.crossdata.models.EphemeralQueryModel
import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.apache.spark.{SparkContext, SparkConf}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class CrossdataStatusHelperIT extends BaseStreamingXDTest with CommonValues {

  var zkTestServer: TestingServer = _
  var zookeeperConnection: String = _

  override def beforeAll: Unit = {
    zkTestServer = new TestingServer()
    zkTestServer.start()
    zookeeperConnection = zkTestServer.getConnectString
  }

  override def afterAll: Unit = {
    CloseableUtils.closeQuietly(zkTestServer)
    zkTestServer.stop()
  }

  "CrossdataStatusHelperIT" should "create a StatusActor without errors" in {

    val sparkConf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
    val sc = SparkContext.getOrCreate(sparkConf)
    val ssc = new StreamingContext(sc, Milliseconds(1000))
    val result = CrossdataStatusHelper.initStatusActor(ssc, Map("connectionString" -> zookeeperConnection), TableName)
    val expected = true

    result.isDefined should be(expected)

    ssc.stop(stopSparkContext = true, stopGracefully = true)
    ssc.awaitTerminationOrTimeout(1000)
  }

  "CrossdataStatusHelperIT" should "create a QueryActor without errors" in {

    val result = CrossdataStatusHelper.createEphemeralQueryActor(Map("connectionString" -> zookeeperConnection))
    val expected = true

    result.isDefined should be(expected)
  }

  "CrossdataStatusHelperIT" should "create a QueryActor and return the queries" in {

    val result =
      CrossdataStatusHelper.queriesFromEphemeralTable(Map("connectionString" -> zookeeperConnection), TableName)
    val expected = Seq.empty[EphemeralQueryModel]

    result should be(expected)
  }

  "CrossdataStatusHelperIT" should "close the actor system correctly" in {

    CrossdataStatusHelper.close()
    val result = CrossdataStatusHelper.actorSystem.isTerminated
    val expected = true

    result should be(expected)
  }

}
