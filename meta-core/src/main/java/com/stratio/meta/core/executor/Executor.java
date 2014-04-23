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

package com.stratio.meta.core.executor;

import com.datastax.driver.core.Session;
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.core.engine.Engine;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.QueryStatus;
import com.stratio.meta.core.utils.Tree;
import org.apache.log4j.Logger;

public class Executor {

    private final Session session;
    private final DeepSparkContext deepSparkContext;
    private final EngineConfig engineConfig;

    public Executor(Session session, DeepSparkContext deepSparkContext, EngineConfig engineConfig) {
        this.session = session;
        this.deepSparkContext = deepSparkContext;
        this.engineConfig = engineConfig;
    }

    public MetaQuery executeQuery(MetaQuery metaQuery) {

        metaQuery.setStatus(QueryStatus.EXECUTED);

        // Get plan
        Tree plan = metaQuery.getPlan();

        System.out.println("PLAN: "+System.lineSeparator()+plan.toStringDownTop());

        // Execute plan
        metaQuery.setResult(plan.executeTreeDownTop(session, deepSparkContext, engineConfig));

        return metaQuery;
    }

}
