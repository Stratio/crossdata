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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Metadata information of a cluster.
 */
public class ClusterMetadata implements IMetadata, UpdatableMetadata {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = -418489079432003461L;

    /**
     * Name of the cluster.
     */
    private final ClusterName name;

    /**
     * Name of the datastore associated with the cluster.
     */
    private final DataStoreName dataStoreRef;

    /**
     * Map of {@link com.stratio.crossdata.common.statements.structures.Selector} options.
     */
    private Map<Selector, Selector> options;

    /**
     * Map associating attached connector by name with their associated metadata.
     */
    private Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs;

    /**
     * Set of catalog that has been created in the cluster.
     */
    private Set<CatalogName> persistedCatalogs;

    /**
     * Class constructor.
     *
     * @param name                  The cluster name.
     * @param dataStoreRef          The associated datastore.
     * @param options               The map of options.
     * @param connectorAttachedRefs The map of attached connectors.
     * @throws ManifestException If the provided manifest is not valid.
     */
    public ClusterMetadata(ClusterName name, DataStoreName dataStoreRef, Map<Selector, Selector> options,
            Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs) throws ManifestException {

        if (name.getName().isEmpty()) {
            throw new ManifestException(new ExecutionException("Tag name cannot be empty"));
        } else {
            this.name = name;
        }

        if (options == null) {
            this.options = new HashMap<>();
        } else {
            this.options = options;
        }

        this.dataStoreRef = dataStoreRef;

        if (connectorAttachedRefs == null) {
            this.connectorAttachedRefs = new HashMap<>();
        } else {
            this.connectorAttachedRefs = connectorAttachedRefs;
        }

        this.persistedCatalogs = new HashSet<>();
    }

    /**
     * Get the cluster name.
     *
     * @return A {@link com.stratio.crossdata.common.data.ClusterName}.
     */
    public ClusterName getName() {
        return name;
    }

    /**
     * Get the map of options.
     *
     * @return A map of {@link com.stratio.crossdata.common.statements.structures.Selector} options.
     */
    public Map<Selector, Selector> getOptions() {
        return options;
    }

    /**
     * Get the name of the associated datastore.
     *
     * @return A {@link com.stratio.crossdata.common.data.DataStoreName}.
     */
    public DataStoreName getDataStoreRef() {
        return dataStoreRef;
    }

    /**
     * Get the map of attached connectors.
     *
     * @return A map associating {@link com.stratio.crossdata.common.data.ConnectorName} with
     * {@link com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata}.
     */
    public Map<ConnectorName, ConnectorAttachedMetadata> getConnectorAttachedRefs() {
        return connectorAttachedRefs;
    }

    /**
     * Set the map of attached connectors.
     *
     * @param connectorAttachedRefs A map associating {@link com.stratio.crossdata.common.data.ConnectorName} with
     *                              {@link com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata}.
     */
    public void setConnectorAttachedRefs(
            Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs) {
        this.connectorAttachedRefs = connectorAttachedRefs;
    }

    /**
     * Set the map of options.
     *
     * @param options A map of {@link com.stratio.crossdata.common.statements.structures.Selector}.
     */
    public void setOptions(Map<Selector, Selector> options) {
        this.options = options;
    }

    /**
     * Get the set of persisted catalogs.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.CatalogName}.
     */
    public Set<CatalogName> getPersistedCatalogs() {
        return persistedCatalogs;
    }

    /**
     * Add a new persisted catalog.
     *
     * @param persistedCatalog The {@link com.stratio.crossdata.common.data.CatalogName}.
     */
    public void addPersistedCatalog(CatalogName persistedCatalog) {
        this.persistedCatalogs.add(persistedCatalog);
    }

    /**
     * Remove a persisted catalog from the cluster set.
     *
     * @param catalog A {@link com.stratio.crossdata.common.data.CatalogName}.
     */
    public void removePersistedCatalog(CatalogName catalog) {
        this.persistedCatalogs.remove(catalog);
    }
}
