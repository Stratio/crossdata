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

package com.stratio.crossdata.common.logicalplan;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.stratio.crossdata.common.metadata.Operations;

/**
 * A step of a Logical plan.
 */
public abstract class LogicalStep implements Serializable {

    private static final long serialVersionUID = -552557268640861386L;
    /**
     * Type of operation to be executed.
     */
    private final Set<Operations> operations;

    /**
     * Next step to be executed.
     */
    private LogicalStep nextStep;

    /**
     * Class constructor.
     *
     * @param operations The operations to be applied.
     */
    protected LogicalStep(Set<Operations> operations) {
        this.operations = operations;
    }

    /**
     * Get the type of operation associated with this filter.
     *
     * @return A {@link com.stratio.crossdata.common.metadata.Operations}.
     */
    public Set<Operations> getOperations() {
        return operations;
    }

    /**
     * Get the next {@link com.stratio.crossdata.common.logicalplan.LogicalStep} to be executed.
     *
     * @return The next step or null if not set.
     */
    public LogicalStep getNextStep() {
        return nextStep;
    }

    /**
     * Set the next step to be executed.
     *
     * @param nextStep A {@link com.stratio.crossdata.common.logicalplan.LogicalStep}.
     */
    public void setNextStep(LogicalStep nextStep) {
        this.nextStep = nextStep;
    }

    /**
     * Get the list of previous steps.
     *
     * @return A list of {@link com.stratio.crossdata.common.logicalplan.LogicalStep}.
     */
    public abstract List<LogicalStep> getPreviousSteps();

    /**
     * Get the first previous step.
     *
     * @return A {@link com.stratio.crossdata.common.logicalplan.LogicalStep}.
     */
    public abstract LogicalStep getFirstPrevious();

}
