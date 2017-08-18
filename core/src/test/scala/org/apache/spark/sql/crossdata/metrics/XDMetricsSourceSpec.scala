/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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
