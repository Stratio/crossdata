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
