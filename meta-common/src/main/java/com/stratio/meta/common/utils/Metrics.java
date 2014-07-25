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

package com.stratio.meta.common.utils;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

/**
 * Class that provides an interface for JMX metrics.
 */
public class Metrics {

  /**
   * Metrics registry.
   */
  private static final MetricRegistry REGISTRY = new MetricRegistry();

  /**
   * Report the measurements using JMX.
   */
  private static JmxReporter reporter = null;

  /**
   * Private class constructor as all methods are static.
   */
  private Metrics() {
  }

  /**
   * Get the metrics registry.
   *
   * @return A {@link com.codahale.metrics.MetricRegistry}.
   */
  public static MetricRegistry getRegistry() {
    if (reporter == null) {
      reporter = JmxReporter.forRegistry(REGISTRY).build();
      reporter.start();
    }
    return REGISTRY;
  }
}
