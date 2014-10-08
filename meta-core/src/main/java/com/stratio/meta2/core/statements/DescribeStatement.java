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

package com.stratio.meta2.core.statements;

import java.util.ArrayList;
import java.util.List;

import com.stratio.meta.core.structures.DescribeType;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

/**
 * Class that models a {@code DESCRIBE} statement from the META language.
 */
public class DescribeStatement extends MetadataStatement implements ITableStatement {

    /**
     * Type of description required: {@code CATALOG} or {@code TABLE}.
     */
    private DescribeType type;

    /**
     * The target table.
     */
    private TableName tableName;

    private ClusterName clusterName;
    private DataStoreName dataStoreName;
    private ConnectorName connectorName;

    /**
     * Class constructor.
     *
     * @param type Type of element to be described.
     */
    public DescribeStatement(DescribeType type) {
        this.type = type;
        this.command = true;
    }

    /**
     * Get the type of element to be described.
     *
     * @return A {@link com.stratio.meta.core.structures.DescribeType}.
     */
    public DescribeType getType() {
        return type;
    }

    public void setType(DescribeType type) {
        this.type = type;
    }

    public ClusterName getClusterName() {
        return clusterName;
    }

    public void setClusterName(ClusterName clusterName) {
        this.clusterName = clusterName;
    }

    public DataStoreName getDataStoreName() {
        return dataStoreName;
    }

    public void setDataStoreName(DataStoreName dataStoreName) {
        this.dataStoreName = dataStoreName;
    }

    public ConnectorName getConnectorName() {
        return connectorName;
    }

    public void setConnectorName(ConnectorName connectorName) {
        this.connectorName = connectorName;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("DESCRIBE ");
        sb.append(type.name());

        if (type == DescribeType.CATALOG && catalog != null) {
            sb.append(" ").append(catalog);
        } else if (type == DescribeType.TABLE) {
            sb.append(" ").append(tableName);
        } else if (type == DescribeType.CLUSTER && clusterName != null) {
            sb.append(" ").append(clusterName);
        } else if (type == DescribeType.CONNECTOR && connectorName != null) {
            sb.append(" ").append(connectorName);
        } else if (type == DescribeType.DATASTORE && dataStoreName != null) {
            sb.append(" ").append(dataStoreName);
        }
        return sb.toString();
    }

    public List<CatalogName> getCatalogs() {
        List<CatalogName> result = new ArrayList<>();
        if (DescribeType.CATALOG.equals(type)) {
            result.add(getEffectiveCatalog());
        }
        return result;
    }

    public List<TableName> getTables() {
        List<TableName> result = new ArrayList<>();
        if (DescribeType.TABLE.equals(type)) {
            result.add(tableName);
        }
        return result;
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        ValidationRequirements validationRequirements = new ValidationRequirements();
        if (catalog != null) {
            validationRequirements.add(Validation.MUST_EXIST_CATALOG);
        }
        if (tableName != null) {
            validationRequirements.add(Validation.MUST_EXIST_CATALOG).add(Validation.MUST_EXIST_TABLE);
        }

        return validationRequirements;

    }

    public TableName getTableName() {
        return tableName;
    }

    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    @Override
    public CatalogName getEffectiveCatalog() {
        CatalogName effective;
        if (tableName != null) {
            effective = tableName.getCatalogName();
        } else {
            effective = catalog;
        }
        if (sessionCatalog != null) {
            effective = sessionCatalog;
        }
        return effective;
    }

}
