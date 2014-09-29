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

import java.util.List;

import org.apache.log4j.Logger;

import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta.core.structures.Option;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

/**
 * Class that models an {@code INSERT INTO} statement from the META language.
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
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(SelectStatement.class);
    /**
     * The name of the target table.
     */
    private TableName tableName;
    /**
     * The list of columns to be assigned.
     */
    private List<ColumnName> ids;
    /**
     * A {@link com.stratio.meta2.core.statements.SelectStatement} to retrieve data if the insert type
     * is matches {@code TYPE_SELECT_CLAUSE}.
     */
    private SelectStatement selectStatement;
    /**
     * A list of {@link com.stratio.meta2.common.statements.structures.selectors.Selector} with the
     * literal values to be assigned if the insert type matches {@code TYPE_VALUES_CLAUSE}.
     */
    private List<Selector> cellValues;
    /**
     * Indicates if exists "IF NOT EXISTS" clause.
     */
    private boolean ifNotExists;
    /**
     * Indicates if there is options in the statement..
     */
    private boolean optsInc;
    /**
     * List of options included in the statement.
     */
    private List<Option> options;
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
     * @param selectStatement a {@link com.stratio.meta2.core.statements.InsertIntoStatement}
     * @param cellValues      List of
     *                        {@link com.stratio.meta2.common.statements.structures.selectors.Selector} to insert.
     * @param ifNotExists     Boolean that indicates if IF NOT EXISTS clause is included in the query.
     * @param optsInc         Boolean that indicates if there is options in the query.
     * @param options         Query options.
     * @param typeValues      Integer that indicates if values come from insert or select.
     */
    public InsertIntoStatement(TableName tableName, List<ColumnName> ids,
            SelectStatement selectStatement, List<Selector> cellValues, boolean ifNotExists,
            boolean optsInc, List<Option> options, int typeValues) {
        this.command = false;
        this.tableName = tableName;
        this.ids = ids;
        this.selectStatement = selectStatement;
        this.cellValues = cellValues;
        this.ifNotExists = ifNotExists;
        this.optsInc = optsInc;
        this.options = options;
        this.typeValues = typeValues;
    }

    /**
     * InsertIntoStatement constructor comes from INSERT INTO .. SELECT .. with options.
     *
     * @param tableName       Tablename target.
     * @param ids             List of name of fields in the table.
     * @param selectStatement a {@link com.stratio.meta2.core.statements.InsertIntoStatement}
     * @param ifNotExists     Boolean that indicates if IF NOT EXISTS clause is included in the query.
     * @param options         Query options.
     */
    public InsertIntoStatement(TableName tableName, List<ColumnName> ids,
            SelectStatement selectStatement, boolean ifNotExists, List<Option> options) {
        this(tableName, ids, selectStatement, null, ifNotExists, true, options, 1);
    }

    /**
     * InsertIntoStatement constructor comes from INSERT INTO .. VALUES .. with options.
     *
     * @param tableName   Tablename target.
     * @param ids         List of name of fields in the table.
     * @param cellValues  List of
     *                    {@link com.stratio.meta2.common.statements.structures.selectors.Selector} to insert.
     * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
     * @param options     Query options.
     */
    public InsertIntoStatement(TableName tableName, List<ColumnName> ids, List<Selector> cellValues,
            boolean ifNotExists, List<Option> options) {
        this(tableName, ids, null, cellValues, ifNotExists, true, options, 2);
    }

    /**
     * InsertIntoStatement constructor comes from INSERT INTO .. SELECT .. without options.
     *
     * @param tableName       Tablename target.
     * @param ids             List of name of fields in the table.
     * @param selectStatement a {@link com.stratio.meta2.core.statements.InsertIntoStatement}
     * @param ifNotExists     Boolean that indicates if IF NOT EXISTS clause is included in the query.
     */
    public InsertIntoStatement(TableName tableName, List<ColumnName> ids,
            SelectStatement selectStatement, boolean ifNotExists) {
        this(tableName, ids, selectStatement, null, ifNotExists, false, null, 1);
    }

    /**
     * InsertIntoStatement constructor comes from INSERT INTO .. VALUES .. without options.
     *
     * @param tableName   Tablename target.
     * @param ids         List of name of fields in the table.
     * @param cellValues  List of
     *                    {@link com.stratio.meta2.common.statements.structures.selectors.Selector} to insert.
     * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
     */
    public InsertIntoStatement(TableName tableName, List<ColumnName> ids, List<Selector> cellValues,
            boolean ifNotExists) {
        this(tableName, ids, null, cellValues, ifNotExists, false, null, 2);
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
        if (optsInc) {
            sb.append(" USING ");
            sb.append(StringUtils.stringList(options, " AND "));
        }
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(Validation.MUST_EXIST_CATALOG).add(Validation.MUST_EXIST_TABLE);
    }

    public TableName getTableName() {
        return tableName;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public CatalogName getCatalogName(){
        return tableName.getCatalogName();
    }
}
