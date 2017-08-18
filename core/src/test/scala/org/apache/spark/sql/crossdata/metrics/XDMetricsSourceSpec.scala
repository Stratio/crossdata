package org.apache.spark.sql.crossdata.metrics

import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XDMetricsSourceSpec extends BaseXDTest {


  "A logical plan (select *) with native relation" should "return a native relation" in {

    val xdms = new XDMetricsSource()

    //Experimentation
    xdms.registerGauge("testName")

    //Expectations
    xdms.metricRegistry.getGauges.keySet() should contain ("metricName.testName")
  }


}
