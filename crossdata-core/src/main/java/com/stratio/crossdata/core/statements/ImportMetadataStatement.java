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

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

/**
 * ImportMetadataStatement class that contain all metadata necessary to import data from a external datastore.
 */
public class ImportMetadataStatement extends MetadataStatement {

    private final ClusterName clusterName;
    private CatalogName catalogName;
    private TableName tableName;
    private boolean discover;

    /**
     * Constructor class.
     * @param clusterName The cluster name.
     * @param catalogName The catalog name.
     * @param tableName The table name.
     * @param discover Boolean that allow to discover data.
     */
    public ImportMetadataStatement(
            ClusterName clusterName,
            CatalogName catalogName,
            TableName tableName,
            boolean discover) {
        this.clusterName = clusterName;
        this.catalogName = catalogName;
        this.tableName = tableName;
        this.discover = discover;
    }

    public ClusterName getClusterName() {
        return clusterName;
    }

    public CatalogName getCatalogName() {
        return catalogName;
    }

    public TableName getTableName() {
        return tableName;
    }

    public boolean isDiscover() {
        return discover;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        if(discover){
            sb.append("DISCOVER METADATA ON CLUSTER ");
        } else {
            sb.append("IMPORT ");
            if(tableName != null){
                sb.append("TABLE ").append(tableName);
            } else if (catalogName != null){
                sb.append("CATALOG ").append(catalogName);
            } else {
                sb.append("CATALOGS");
            }
            sb.append(" FROM CLUSTER ");
        }
        sb.append(clusterName);
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        ValidationRequirements validations = new ValidationRequirements().add(ValidationTypes.MUST_EXIST_CLUSTER);
        if(catalogName != null){
            validations = validations.add(ValidationTypes.MUST_NOT_EXIST_CATALOG);
        } else if (tableName != null){
            validations = validations.add(ValidationTypes.MUST_NOT_EXIST_TABLE);
        }
        return validations;
    }
}
