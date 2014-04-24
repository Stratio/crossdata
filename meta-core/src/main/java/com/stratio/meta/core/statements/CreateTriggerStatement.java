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

package com.stratio.meta.core.statements;

import com.datastax.driver.core.Statement;
import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code CREATE TRIGGER} statement from the META language.
 */
public class CreateTriggerStatement extends MetaStatement {

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
     * @param triggerName The name of the trigger.
     * @param tableName The name of the target table.
     * @param className The name of the class.
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
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }

    @Override
    public DeepResultSet executeDeep() {
        return new DeepResultSet();
    }
    
    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        return new Tree();
    }
    
}
