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

import org.apache.log4j.Logger;

import com.stratio.meta2.core.validator.ValidationRequirements;

public class StopProcessStatement extends MetadataStatement {

    private static final Logger LOG = Logger.getLogger(StopProcessStatement.class);
    private String queryId;

    public StopProcessStatement(String queryId) {
        this.command = true;
        this.queryId = queryId;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stop process ");
        sb.append(queryId);
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements();
    }
}
