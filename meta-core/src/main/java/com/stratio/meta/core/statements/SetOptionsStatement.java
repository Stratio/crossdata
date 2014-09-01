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
    public String translateToCQL(MetadataManager metadataManager) {
        return this.toString();
    }

    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        return new Tree();
    }
    
}
