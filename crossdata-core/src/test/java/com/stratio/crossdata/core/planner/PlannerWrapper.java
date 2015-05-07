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

package com.stratio.crossdata.core.planner;

import java.util.List;

import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.executionplan.ExecutionPath;
import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.core.query.SelectValidatedQuery;

/**
 * Planner wrapper to test protected methods
 */
public class PlannerWrapper extends Planner {

    public PlannerWrapper(String host) {
        super(host);
    }

    @Override
    public ExecutionPath defineExecutionPath(LogicalStep initial,
            List<ConnectorMetadata> availableConnectors, SelectValidatedQuery svq) throws PlanningException {
        return super.defineExecutionPath(initial, availableConnectors, svq);
    }
}
