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

import com.stratio.crossdata.core.validator.ValidationRequirements;

/**
 * Class that models a {@code CREATE TRIGGER} statement from the META language.
 */
public class CreateTriggerStatement extends MetadataStatement {

    /**
     * The name of the trigger.
     */
    private String triggerName;

    /**
     * The name of the target table.
     */
    private String tableName;

    /**
     * The qualified class name that implements the trigger.
     */
    private String className;

    /**
     * Class constructor.
     *
     * @param triggerName The name of the trigger.
     * @param tableName   The name of the target table.
     * @param className   The name of the class.
     */
    public CreateTriggerStatement(String triggerName, String tableName, String className) {
        this.command = true;
        this.triggerName = triggerName;
        this.tableName = tableName;
        this.className = className;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Create trigger ");
        sb.append(triggerName);
        sb.append(" on ");
        sb.append(tableName);
        sb.append(" using ");
        sb.append(className);
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements();
    }

}
