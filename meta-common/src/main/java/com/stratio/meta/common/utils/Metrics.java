package com.stratio.meta.common.utils;

import com.codahale.metrics.MetricRegistry;

public class Metrics {
    private static final MetricRegistry registry= new MetricRegistry();

    public static MetricRegistry getRegistry(){
        return registry;
    }
}
