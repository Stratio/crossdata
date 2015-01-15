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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

/**
 * Class that models an {@code INSERT INTO} statement from the CROSSDATA language.
 */
public class InsertIntoStatement extends StorageStatement {

    /**
     * Constant to define an {@code INSERT INTO} that takes the input values from a {@code SELECT}
     * subquery.
     */
    public static final int TYPE_SELECT_CLAUSE = 1;

    /**
     * Constant to define an {@code INSERT INTO} that takes literal values as input.
     */
    public static final int TYPE_VALUES_CLAUSE = 2;

    /**
     * The name of the target table.
     */
    private TableName tableName;

    /**
     * The list of columns to be assigned.
     */
    private List<ColumnName> ids;

    /**
     * A {@link com.stratio.crossdata.core.statements.SelectStatement} to retrieve data if the insert type
     * is matches {@code TYPE_SELECT_CLAUSE}.
     */
    private SelectStatement selectStatement;

    /**
     * A list of {@link com.stratio.crossdata.common.statements.structures.Selector} with the
     * literal values to be assigned if the insert type matches {@code TYPE_VALUES_CLAUSE}.
     */
    private List<Selector> cellValues;

    /**
     * Indicates if exists "IF NOT EXISTS" clause.
     */
    private boolean ifNotExists;

    /**
     * List of options included in the statement.
     */
    private List<Relation> conditions;

    /**
     * A JSON with the options specified by the user.
     */
    private Map<Selector, Selector> options = new HashMap<>();

    /**
     * Type of Insert statement. {@link InsertIntoStatement#TYPE_SELECT_CLAUSE} or
     * {@link InsertIntoStatement#TYPE_VALUES_CLAUSE}.
     */
    private int typeValues;

    /**
     * InsertIntoStatement general constructor.
     *
     * @param tableName       Tablename target.
     * @param ids             List of name of fields in the table.
     * @param selectStatement a {@link com.stratio.crossdata.core.statements.InsertIntoStatement}
     * @param cellValues      List of
     *                        {@link com.stratio.crossdata.common.statements.structures.Selector} to insert.
     * @param ifNotExists     Boolean that indicates if IF NOT EXISTS clause is included in the query.
     * @param conditions      Query options.
     * @param typeValues      Integer that indicates if values come from insert or select.
     */
    public InsertIntoStatement(TableName tableName, List<ColumnName> ids,
            SelectStatement selectStatement, List<Selector> cellValues, boolean ifNotExists,
            List<Relation> conditions, String options, int typeValues) {
        this.command = false;
        this.tableName = tableName;
        this.ids = ids;
        this.selectStatement = selectStatement;
        this.cellValues = cellValues;
        this.ifNotExists = ifNotExists;
        if (conditions != null) {
            this.conditions = conditions;
        } else {
            this.conditions = new ArrayList<>();
        }
        if (options != null) {
            this.options = StringUtils.convertJsonToOptions(tableName, options);
        } else {
            this.options = new HashMap<>();
        }
        this.typeValues = typeValues;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        if (catalogInc) {
            sb.append(catalog).append(".");
        }
        sb.append(tableName).append(" (");
        sb.append(StringUtils.stringList(ids, ", ")).append(") ");
        if (typeValues == TYPE_SELECT_CLAUSE) {
            sb.append(selectStatement.toString());
        } else {
            sb.append("VALUES (");
            sb.append(StringUtils.stringList(cellValues, ", "));
            sb.append(")");
        }
        if (ifNotExists) {
            sb.append(" IF NOT EXISTS");
        }
        if ((conditions != null) && (!conditions.isEmpty())) {
            sb.append(" WHEN ");
            sb.append(StringUtils.stringList(conditions, " AND "));
        }
        if ((options != null) && (!options.isEmpty())) {
            sb.append(" WITH ").append(options);
        }
        return sb.toString();
    }

    public void setCellValues(List<Selector> cellValues) {
        this.cellValues = cellValues;
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(ValidationTypes.MUST_EXIST_CATALOG)
                .add(ValidationTypes.MUST_EXIST_TABLE).add
                        (ValidationTypes.VALIDATE_TYPES);
    }

    public TableName getTableName() {
        return tableName;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public CatalogName getCatalogName() {
        return tableName.getCatalogName();
    }

    public List<ColumnName> getIds() {
        return ids;
    }

    public List<Selector> getCellValues() {
        return cellValues;
    }
}
