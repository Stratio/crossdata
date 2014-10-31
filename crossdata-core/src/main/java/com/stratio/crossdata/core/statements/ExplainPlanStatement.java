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

package com.stratio.crossdata.core.statements;

import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;

/**
 * Class that models an {@code EXPLAIN PLAN} statement from the META language.
 */
public class ExplainPlanStatement extends MetadataStatement {

    /**
     * The {@link CrossdataStatement} to be analyzed.
     */
    private CrossdataStatement crossdataStatement;

    /**
     * Class constructor.
     *
     * @param crossdataStatement The {@link CrossdataStatement} to be analyzed.
     */
    public ExplainPlanStatement(CrossdataStatement crossdataStatement) {
        this.command = true;
        this.crossdataStatement = crossdataStatement;
    }

    public CrossdataStatement getCrossdataStatement() {
        return crossdataStatement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Explain plan for ");
        sb.append(crossdataStatement.toString());
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements();
    }
}
