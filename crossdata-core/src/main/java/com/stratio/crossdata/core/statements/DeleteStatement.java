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

import java.util.List;

import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;

/**
 * Class that models a {@code SELECT} statement from the META language. This class recognizes the
 * following syntax:
 * <p/>
 * DELETE ( {@literal <column>}, ( ',' {@literal <column>} )*)? FROM {@literal <tablename>} WHERE
 * {@literal <where_clause>};
 */
public class DeleteStatement extends StorageStatement implements ITableStatement {

    /**
     * The name of the targe table.
     */
    private TableName tableName = null;

    /**
     * The list of {@link com.stratio.crossdata.common.statements.structures.Relation} found
     * in the WHERE clause.
     */
    private List<Relation> whereClauses;

    public DeleteStatement(TableName tableName, List<Relation> whereClauses) {
        this.tableName = tableName;
        this.whereClauses = whereClauses;
    }

    /**
     * Add a new {@link com.stratio.crossdata.common.statements.structures.Relation} found in
     * a WHERE clause.
     *
     * @param relation The relation.
     */
    public void addRelation(Relation relation) {
        whereClauses.add(relation);
    }

    public List<Relation> getWhereClauses() {
        return whereClauses;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        if (catalogInc) {
            sb.append(catalog).append(".");
        }
        sb.append(tableName);
        if (!whereClauses.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(StringUtils.stringList(whereClauses, " AND "));
        }
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(ValidationTypes.MUST_EXIST_CATALOG).add(ValidationTypes.MUST_EXIST_TABLE)
                .add(ValidationTypes.MUST_EXIST_COLUMN);
    }

    public TableName getTableName() {
        return tableName;
    }

    /**
     * Set the name of the table.
     *
     * @param tableName The name of the table.
     */
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
