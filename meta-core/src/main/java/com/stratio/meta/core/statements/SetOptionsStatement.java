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
import com.stratio.meta.core.structures.Consistency;
import com.stratio.meta.core.utils.Tree;

import java.util.ArrayList;
import java.util.List;

public class SetOptionsStatement extends MetaStatement {

    /**
     * The consistency level.
     */
    private Consistency consistency;

    /**
     * Whether analytics should be used.
     */
    private boolean analytics;

    /**
     * The list of options present.
     */
    private List<Boolean> optionsCheck;

    /**
     * Class constructor.
     * @param analytics Whether analytics are used.
     * @param consistency The level of consistency.
     * @param optionsCheck The list of options present.
     */
    public SetOptionsStatement(boolean analytics, Consistency consistency, List<Boolean> optionsCheck) {
        this.command = true;
        this.consistency = consistency;
        this.analytics = analytics;
        this.optionsCheck = new ArrayList<>();
        this.optionsCheck.addAll(optionsCheck);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Set options ");
        if(optionsCheck.get(0)){
            sb.append("analytics=").append(analytics);            
            if(optionsCheck.get(1)){
                sb.append(" AND consistency=").append(consistency);
            }
        } else {
            if(optionsCheck.get(1)){
                sb.append("consistency=").append(consistency);
            }
        }        
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
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        return new Tree();
    }
    
}
