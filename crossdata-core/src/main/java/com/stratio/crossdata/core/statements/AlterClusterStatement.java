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

import java.util.HashMap;
import java.util.Map;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

/**
 * Class that models a {@code ALTER CLUSTER} statement from the CROSSDATA language.
 */
public class AlterClusterStatement extends MetadataStatement {

    /**
     * CLUSTER name given by the user.
     */
    private final ClusterName clusterName;

    private final boolean ifExists;

    /**
     * A JSON with the options of the cluster.
     */
    private final Map<Selector, Selector> options;

    /**
     * Alter an existing cluster configuration.
     *
     * @param clusterName The name of the cluster.
     * @param ifExists    whether to check if cluster already exists
     * @param options     A JSON with the cluster options.
     */
    public AlterClusterStatement(ClusterName clusterName, boolean ifExists, String options) {
        this.clusterName = clusterName;
        this.ifExists = ifExists;

        if ((options == null) || options.isEmpty()) {
            this.options = new HashMap<>();
        } else {
            this.options = StringUtils.convertJsonToOptions(null, options);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ALTER CLUSTER ");
        if (ifExists) {
            sb.append("IF EXISTS ");
        }
        sb.append(clusterName);
        sb.append(" WITH ").append(options);
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(ValidationTypes.MUST_EXIST_CLUSTER)
                .add(ValidationTypes.MUST_EXIST_PROPERTIES);
    }

    public ClusterName getClusterName() {
        return clusterName;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public Map<Selector, Selector> getOptions() {
        return options;
    }
}
