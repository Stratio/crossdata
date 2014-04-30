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

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code DROP TRIGGER} statement from the META language.
 */
public class DropTriggerStatement extends MetaStatement{

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
    public String translateToCQL() {
        return this.toString();
    }

    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        return new Tree();
    }
    
}
