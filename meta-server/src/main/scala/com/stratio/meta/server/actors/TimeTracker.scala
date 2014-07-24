/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.server.actors

import com.codahale.metrics.Timer
import com.stratio.meta.common.utils.Metrics

/**
 * Trait to be able to time the operations inside an actor.
 */
trait TimeTracker {

  /**
   * Name of the timer.
   */
  lazy val timerName: String = ???

  /**
   * Timer gauge.
   */
  lazy val timerMetrics: Timer = Metrics.getRegistry.timer(timerName)

  /**
   * Initialize the timer.
   */
  def initTimer(): Timer.Context = timerMetrics.time()

  /**
   * Stop the timer.
   * @param context The timing context.
   * @return Whether it has stop.
   */
  def finishTimer(context: Timer.Context) = {
    context.stop()
  }
}
