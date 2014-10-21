/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.utils;

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
