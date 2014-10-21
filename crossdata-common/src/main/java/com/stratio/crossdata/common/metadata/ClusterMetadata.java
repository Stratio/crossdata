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

package com.stratio.crossdata.common.metadata;

import java.util.Map;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.statements.structures.selectors.Selector;

public class ClusterMetadata implements IMetadata {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -418489079432003461L;

    private final ClusterName name;
    private final DataStoreName dataStoreRef;
    private final Map<Selector, Selector> options;
    private Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs;

    public ClusterMetadata(ClusterName name, DataStoreName dataStoreRef, Map<Selector, Selector> options,
            Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs) {
        this.name = name;
        this.options = options;
        this.dataStoreRef = dataStoreRef;
        this.connectorAttachedRefs = connectorAttachedRefs;
    }

    public ClusterName getName() {
        return name;
    }

    public Map<Selector, Selector> getOptions() {
        return options;
    }

    public DataStoreName getDataStoreRef() {
        return dataStoreRef;
    }

    public Map<ConnectorName, ConnectorAttachedMetadata> getConnectorAttachedRefs() {
        return connectorAttachedRefs;
    }

    public void setConnectorAttachedRefs(
            Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs) {
        this.connectorAttachedRefs = connectorAttachedRefs;
    }

}
