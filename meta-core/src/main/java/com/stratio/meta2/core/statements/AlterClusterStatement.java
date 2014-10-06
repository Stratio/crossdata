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

import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

/**
 * Class that models a {@code ALTER CLUSTER} statement from the META language.
 */
public class AlterClusterStatement extends MetadataStatement {

    /**
     * CLUSTER name given by the user.
     */
    private final String clusterName;

    private final boolean ifExists;

    /**
     * A JSON with the options specified by the user.
     */
    private final String options;

    /**
     * Alter an existing cluster configuration.
     *
     * @param clusterName The name of the cluster.
     * @param options     A JSON with the cluster options.
     */
    public AlterClusterStatement(String clusterName, boolean ifExists, String options) {
        this.clusterName = clusterName;
        this.ifExists = ifExists;
        this.options = options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ALTER CLUSTER ");
        if (ifExists) {
            sb.append("IF EXISTS ");
        }
        sb.append(clusterName);
        sb.append(" WITH OPTIONS ").append(options);
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(Validation.MUST_EXIST_CLUSTER).add(Validation.MUST_EXIST_PROPERTIES);
    }

    public String getClusterName() {
        return clusterName;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public String getOptions() {
        return options;
    }
}
