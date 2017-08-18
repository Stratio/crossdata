/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.metrics

import com.codahale.metrics.{Gauge, MetricRegistry}
import org.apache.spark.metrics.source.Source

class XDMetricsSource extends Source{

  override val sourceName = "XDMetricsSource"
  override val metricRegistry = new MetricRegistry()

  // Simple metric registered
  metricRegistry.register(MetricRegistry.name("executionType"), new Gauge[String] {
    override def getValue: String = "valueOfExecutionType"
  })

  def registerGauge(name: String): Unit = {
    metricRegistry.register(MetricRegistry.name("metricName", name), new Gauge[String] {
      override def getValue: String = name
    })
  }
}
