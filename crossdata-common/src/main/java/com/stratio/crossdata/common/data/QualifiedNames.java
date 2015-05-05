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

package com.stratio.crossdata.common.data;

import java.io.Serializable;

/**
 * Helper class to provide qualified names for cluster, tables, and columns.
 */
public final class QualifiedNames implements Serializable {

    private static final long serialVersionUID = 3409400422750005391L;

    /**
     * Private constructor as all methods are static.
     */
    private QualifiedNames() {
    }

    /**
     * Get the qualified name for a catalog.
     *
     * @param name The catalog name.
     * @return The name of the catalog in lower-case.
     */
    public static String getCatalogQualifiedName(String name) {
        return name;
    }

    /**
     * Get the catalogName from a qualified Name.
     * @param qualifiedName The qualified name.
     * @return A {@link com.stratio.crossdata.common.data.CatalogName} .
     */
    public static CatalogName getCatalogNameFromQualifiedName(String qualifiedName) {
        CatalogName catalogName = null;
        String[] arrNames = qualifiedName.split("\\.");
        if (arrNames.length == 2) {
            catalogName = new CatalogName(arrNames[1]);
        }
        return catalogName;
    }

    /**
     * Get the qualified name for a table.
     *
     * @param catalog The catalog name.
     * @param name    The table name.
     * @return A lower-case string with the elements separated by the dot character.
     */
    public static String getTableQualifiedName(String catalog, String name) {
        return catalog + "." + name;
    }

    /**
     * Get the qualified name for a column.
     *
     * @param catalog The catalog name.
     * @param table   The table name.
     * @param name    The column name.
     * @return A lower-case string with the elements separated by the dot character.
     */
    public static String getColumnQualifiedName(String catalog, String table, String name) {
        return getTableQualifiedName(catalog, table) + "." + name;
    }

    /**
     * Get the qualified name for a cluster.
     *
     * @param name The cluster name.
     * @return The name of cluster preceded by {@code cluster.}.
     */
    public static String getClusterQualifiedName(String name) {
        return "cluster." + name;
    }

    /**
     * Get the qualified name for a connector.
     *
     * @param name The connector name.
     * @return The name of the connector preceded by {@code connector.}.
     */
    public static String getConnectorQualifiedName(String name) {
        return "connector." + name;
    }

    /**
     * Get the qualified name for a node.
     *
     * @param name The node name.
     * @return The name of the node preceded by {@code node.}.
     */
    public static String getNodeQualifiedName(String name) {
        return "node." + name;
    }

    /**
     * Get the qualified name of a function.
     * @param name The name of the function.
     * @return A string.
     */
    public static String getFunctionQualifiedName(String name) {
        return "function." + name;
    }

    /**
     * Get the qualified name for a datastore.
     *
     * @param name The datastore name.
     * @return The name of the datastore preceded by {@code datastore.}.
     */
    public static String getDataStoreQualifiedName(String name) {
        return "datastore." + name;
    }

    /**
     * Get the qualified name for an index.
     *
     * @param catalog The catalog name.
     * @param table   The table name.
     * @param name    The index name.
     * @return A name in the form of {@code catalog.table.INDEX[index_name]}.
     */
    public static String getIndexQualifiedName(String catalog, String table,
            String name) {
        return getTableQualifiedName(catalog, table) + ".INDEX[" + name + "]";
    }


}
