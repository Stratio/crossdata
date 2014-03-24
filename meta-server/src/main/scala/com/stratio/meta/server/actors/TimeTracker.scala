package com.stratio.meta.server.actors

import com.codahale.metrics.Timer
import com.stratio.meta.common.utils.Metrics

trait TimeTracker {
  def timerName:String
  lazy val timerMetrics:Timer = Metrics.getRegistry.timer(timerName)

  def initTimer():Timer.Context= timerMetrics.time()

  def finishTimer(context:Timer.Context)={
    context.stop()
  }
}
