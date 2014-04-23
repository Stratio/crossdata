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
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.deep.Bridge;
import com.stratio.meta.deep.exceptions.MetaDeepException;

import java.util.List;

public class DeepExecutor {
    public static Result execute(MetaStatement stmt, List<Result> resultsFromChildren, boolean isRoot, Session session, DeepSparkContext deepSparkContext, EngineConfig engineConfig) {
        if (stmt instanceof SelectStatement) {
            SelectStatement ss = (SelectStatement) stmt;
            Bridge bridge = new Bridge(session, deepSparkContext, engineConfig);
            ResultSet resultSet;
            try {
                resultSet = bridge.execute(ss, resultsFromChildren, isRoot);
            } catch(MetaDeepException ex){
                return QueryResult.createFailQueryResult(ex.getMessage());
            }
            return QueryResult.createSuccessQueryResult(resultSet);
        } else {
            System.out.println("EMPTY DEEP RESULT");
            return QueryResult.createSuccessQueryResult();
        }
    }
}
