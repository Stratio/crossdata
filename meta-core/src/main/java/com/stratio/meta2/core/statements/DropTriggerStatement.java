/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.core.statements;

import com.stratio.meta2.core.validator.ValidationRequirements;

/**
 * Class that models a {@code DROP TRIGGER} statement from the META language.
 */
public class DropTriggerStatement extends MetadataStatement {

    /**
     * The name of the trigger.
     */
    private String triggerName;

    /**
     * The name of the table.
     */
    private String tableName;

    /**
     * Class constructor.
     *
     * @param triggerName
     * @param tableName
     */
    public DropTriggerStatement(String triggerName, String tableName) {
        this.command = true;
        this.triggerName = triggerName;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Drop trigger ");
        sb.append(triggerName);
        sb.append(" on ");
        sb.append(tableName);
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements();
    }

}
