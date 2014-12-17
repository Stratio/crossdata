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
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

/**
 * Class that models a {@code ATTACH CLUSTER} statement from the CROSSDATA language. A cluster represents
 * a logical cluster of storage machines that target the same datastore technology. To create a
 * storage, the user must specify the hosts and ports where the datastore is available in the JSON
 * options.
 */
public class AttachClusterStatement extends MetadataStatement {

    /**
     * CLUSTER name given by the user. This name will be used to refer to the cluster when creating
     * new tables.
     */
    private final ClusterName clusterName;

    /**
     * Whether the storage should be created only if not exists.
     */
    private final boolean ifNotExists;

    /**
     * Name of the datastore associated with the storage and the connectors.
     */
    private final DataStoreName datastoreName;

    /**
     * A JSON with the options of the cluster.
     */
    private final Map<Selector, Selector> options;

    /**
     * Create new cluster on the system.
     *
     * @param clusterName   The name of the cluster.
     * @param ifNotExists   Whether it should be created only if not exists.
     * @param datastoreName The name of the datastore.
     * @param options       A JSON with the options.
     */
    public AttachClusterStatement(ClusterName clusterName, boolean ifNotExists, DataStoreName datastoreName,
            String options) {
        this.clusterName = clusterName;
        this.ifNotExists = ifNotExists;
        this.datastoreName = datastoreName;

        if ((options == null) || options.isEmpty()) {
            this.options = new HashMap<>();
        } else {
            this.options = StringUtils.convertJsonToOptions(options);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ATTACH CLUSTER ");
        if (ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }
        sb.append(clusterName);
        sb.append(" ON DATASTORE ").append(datastoreName);
        sb.append(" WITH OPTIONS ").append(options);
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements()
                .add(ValidationTypes.MUST_NOT_EXIST_CLUSTER)
                .add(ValidationTypes.MUST_EXIST_DATASTORE)
                .add(ValidationTypes.MUST_BE_UNIQUE_DATASTORE)
                .add(ValidationTypes.VALID_CLUSTER_OPTIONS);
    }

    public Map<Selector, Selector> getOptions() {
        return options;
    }

    public ClusterName getClusterName() {
        return clusterName;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public DataStoreName getDatastoreName() {
        return datastoreName;
    }
}
