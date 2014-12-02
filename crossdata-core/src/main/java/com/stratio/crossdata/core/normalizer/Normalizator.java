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

package com.stratio.crossdata.core.normalizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.exceptions.validation.AmbiguousNameException;
import com.stratio.crossdata.common.exceptions.validation.BadFormatException;
import com.stratio.crossdata.common.exceptions.validation.NotExistNameException;
import com.stratio.crossdata.common.exceptions.validation.NotMatchDataTypeException;
import com.stratio.crossdata.common.exceptions.validation.NotValidColumnException;
import com.stratio.crossdata.common.exceptions.validation.YodaConditionException;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.IndexType;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.FunctionSelector;
import com.stratio.crossdata.common.statements.structures.SelectExpression;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.SelectorType;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.statements.SelectStatement;
import com.stratio.crossdata.core.structures.GroupByClause;
import com.stratio.crossdata.core.structures.InnerJoin;
import com.stratio.crossdata.common.statements.structures.OrderByClause;

/**
 * Normalizator Class.
 */
public class Normalizator {

    private NormalizedFields fields = new NormalizedFields();
    private IParsedQuery parsedQuery;

    /**
     * Class Constructor
     * @param parsedQuery The parsed query.
     */
    public Normalizator(SelectParsedQuery parsedQuery) {
        this.parsedQuery = parsedQuery;
    }

    /**
     * Get the obtained fields normalized.
     * @return com.stratio.crossdata.core.normalizer.NormalizerFields
     */
    public NormalizedFields getFields() {
        return fields;
    }

    /**
     * Get the parsed query to Normalize.
     * @return com.stratio.crossdata.core.query.IParsedQuery;
     */
    public IParsedQuery getParsedQuery() {
        return parsedQuery;
    }

    /**
     * execute the normalization of a parsed query.
     * @throws ValidationException
     */
    public void execute() throws ValidationException {
        normalizeTables();
        normalizeSelectExpression();
        normalizeJoins();
        normalizeWhere();
        normalizeOrderBy();
        normalizeGroupBy();
    }

    /**
     * Normalize the tables of a query.
     * @throws ValidationException
     */
    public void normalizeTables() throws ValidationException {
        List<TableName> tableNames = parsedQuery.getStatement().getFromTables();
        if (tableNames != null && !tableNames.isEmpty()) {
            normalizeTables(tableNames);
        }
    }

    /**
     * Normalize the "from" tables of a parsed query.
     * @param fromTables List of the from clause tables of a parsed query
     * @throws ValidationException
     */
    public void normalizeTables(List<TableName> fromTables) throws ValidationException {
        for (TableName tableName : fromTables) {
            checkTable(tableName);
            fields.getCatalogNames().add(tableName.getCatalogName());
            fields.addTableName(tableName);
        }
    }

    /**
     * Normalize all the joins of a parsed query.
     * @throws ValidationException
     */
    public void normalizeJoins()
            throws ValidationException {
        InnerJoin innerJoin = ((SelectStatement) parsedQuery.getStatement()).getJoin();
        if (innerJoin != null) {
            normalizeJoins(innerJoin);
            fields.setJoin(innerJoin);
        }
    }

    /**
     * Normalize a specific inner join of a parsed query
     * @param innerJoin The inner join
     * @throws ValidationException
     */
    public void normalizeJoins(InnerJoin innerJoin)
            throws ValidationException {
        checkJoinRelations(innerJoin.getRelations());
    }

    private void normalizeWhere()
            throws ValidationException {
        List<Relation> where = ((SelectStatement) parsedQuery.getStatement()).getWhere();
        if (where != null && !where.isEmpty()) {
            normalizeWhere(where);
            fields.setWhere(where);
        }
    }

    private void normalizeWhere(List<Relation> where)
            throws ValidationException {
        checkWhereRelations(where);
    }

    /**
     * Normalize the order by clause of a parsed query.
     * @throws ValidationException
     */
    public void normalizeOrderBy()
            throws ValidationException {

        List<OrderByClause> orderByClauseClauses = ((SelectStatement) parsedQuery.getStatement()).getOrderByClauses();

        if (orderByClauseClauses != null) {
            normalizeOrderBy(orderByClauseClauses);
            fields.setOrderByClauseClauses(orderByClauseClauses);
        }
    }

    /**
     * Normalize an specific order by of a parsed query.
     * @param orderByClauseClauses The order by
     * @throws ValidationException
     */
    public void normalizeOrderBy(List<OrderByClause> orderByClauseClauses)
            throws ValidationException {
        for (OrderByClause orderBy: orderByClauseClauses) {
            Selector selector = orderBy.getSelector();
            switch (selector.getType()) {
            case COLUMN:
                checkColumnSelector((ColumnSelector) selector);
                break;
            case FUNCTION:
            case ASTERISK:
            case BOOLEAN:
            case STRING:
            case INTEGER:
            case FLOATING_POINT:
                throw new BadFormatException("Order by only accepts columns");
            }

        }
    }

    /**
     * Normalize de select clause of a parsed query.
     * @throws ValidationException
     */
    public void normalizeSelectExpression()
            throws ValidationException {
        SelectExpression selectExpression = ((SelectStatement) parsedQuery.getStatement()).getSelectExpression();
        if (selectExpression != null) {
            normalizeSelectExpression(selectExpression);
        }
    }

    /**
     * Normalize an specific select expression.
     * @param selectExpression The select expression
     * @throws ValidationException
     */
    public void normalizeSelectExpression(SelectExpression selectExpression)
            throws ValidationException {
        fields.setDistinctSelect(selectExpression.isDistinct());
        List<Selector> normalizeSelectors = checkListSelector(selectExpression.getSelectorList());
        fields.getSelectors().addAll(normalizeSelectors);
    }

    /**
     * Normalize the group by of a parsed query.
     * @throws ValidationException
     */
    public void normalizeGroupBy() throws ValidationException {
        GroupByClause groupByClause = ((SelectStatement) parsedQuery.getStatement()).getGroupByClause();
        if (groupByClause != null) {
            normalizeGroupBy(groupByClause);
            fields.setGroupByClause(groupByClause);
        }
    }

    private void checkFormatBySelectorIdentifier(Selector selector, Set<ColumnName> columnNames)
            throws ValidationException {
        switch (selector.getType()) {
        case FUNCTION:
            throw new BadFormatException("Function include into groupBy is not valid");
        case COLUMN:
            checkColumnSelector((ColumnSelector) selector);
            if (!columnNames.add(((ColumnSelector) selector).getName())) {
                throw new BadFormatException("COLUMN into group by is repeated");
            }
            break;
        case ASTERISK:
            throw new BadFormatException("Asterisk include into groupBy is not valid");
        }
    }

    private void checkColumns(Selector selector, Set<ColumnName> columnNames) throws BadFormatException {
        switch (selector.getType()) {
        case FUNCTION:
            break;
        case COLUMN:
            ColumnName name = ((ColumnSelector) selector).getName();
            if (!columnNames.contains(name)) {
                throw new BadFormatException(
                        "All columns in the select clause must be in the group by or it must be aggregation function.");
            }
            break;
        case ASTERISK:
            throw new BadFormatException("Asterisk is not valid with group by statements");
        }
    }

    /**
     * Normalize an specific group by of a parsed query.
     * @param groupByClause
     * @throws ValidationException
     */
    public void normalizeGroupBy(GroupByClause groupByClause) throws ValidationException {
        Set<ColumnName> columnNames = new HashSet<>();
        for (Selector selector : groupByClause.getSelectorIdentifier()) {
            checkFormatBySelectorIdentifier(selector, columnNames);
        }
        // Check if all columns are correct
        for (Selector selector : fields.getSelectors()) {
            checkColumns(selector, columnNames);
        }
    }

    /**
     * Validate the joins of a parsed query.
     * @param relations A list of Relation to check.
     * @throws ValidationException
     */
    public void checkJoinRelations(List<Relation> relations) throws ValidationException {
        for (Relation relation : relations) {
            checkRelation(relation);
            switch (relation.getOperator()) {
            case EQ:
                if (relation.getLeftTerm().getType() == SelectorType.COLUMN
                        && relation.getRightTerm().getType() == SelectorType.COLUMN) {
                    checkColumnSelector((ColumnSelector) relation.getRightTerm());
                    checkColumnSelector((ColumnSelector) relation.getLeftTerm());
                } else {
                    throw new BadFormatException("You must compare between columns");
                }
                break;
            default:
                throw new BadFormatException("Only equal operation are just valid");
            }
        }
    }

    /**
     * Validate the where clause of a parsed query.
     * @param relations The list of Relation that contains the where clause to check
     * @throws ValidationException
     */
    public void checkWhereRelations(List<Relation> relations) throws ValidationException {
        for (Relation relation : relations) {
            checkRelation(relation);
        }
    }

    /**
     * Validate the relation of a parsed query.
     * @param relation The relation of the query.
     * @throws ValidationException
     */
    public void checkRelation(Relation relation)
            throws ValidationException {
        if (relation.getOperator().isInGroup(Operator.Group.ARITHMETIC)) {
            throw new BadFormatException("Compare operations are just valid");
        }
        checkRelationFormatLeft(relation);
        checkRelationFormatRight(relation);
    }

    private void checkRelationFormatLeft(Relation relation)
            throws ValidationException {
        switch (relation.getLeftTerm().getType()) {
        case FUNCTION:
            throw new BadFormatException("Functions not supported yet");
        case COLUMN:
            checkColumnSelector((ColumnSelector) relation.getLeftTerm());
            break;
        case ASTERISK:
            throw new BadFormatException("Asterisk not supported in relations.");
        case STRING:
        case FLOATING_POINT:
        case BOOLEAN:
        case INTEGER:
            throw new YodaConditionException();
        }
    }

    private void checkRelationFormatRight(Relation relation)
            throws ValidationException {
        switch (relation.getRightTerm().getType()) {
        case COLUMN:
        case STRING:
        case FLOATING_POINT:
        case BOOLEAN:
        case INTEGER:
            ColumnSelector columnSelector = (ColumnSelector) relation.getLeftTerm();
            checkRightSelector(columnSelector.getName(), relation.getOperator(), relation.getRightTerm());
            break;
        case RELATION:
        case FUNCTION:
        case ASTERISK:
            throw new BadFormatException("Not supported yet.");
        }
    }

    /**
     * Validate the table of a parsed query.
     * @param tableName The table name to validate
     * @throws ValidationException
     */
    public void checkTable(TableName tableName) throws ValidationException {
        if (!tableName.isCompletedName()) {
            tableName.setCatalogName(parsedQuery.getDefaultCatalog());
        }
        if (!MetadataManager.MANAGER.exists(tableName)) {
            throw new NotExistNameException(tableName);
        }
    }

    /**
     * Validate the column selectors.
     * @param selector The selector
     * @throws ValidationException
     */
    public void checkColumnSelector(ColumnSelector selector) throws ValidationException {
        ColumnName columnName = applyAlias(selector.getName());
        if (columnName.isCompletedName()) {
            if (!MetadataManager.MANAGER.exists(columnName)) {
                throw new NotValidColumnException(columnName);
            }
        } else {
            TableName searched = this.searchTableNameByColumn(columnName);
            columnName.setTableName(searched);
        }
        fields.addColumnName(columnName, selector.getAlias());
        selector.setName(columnName);

    }

    private ColumnName applyAlias(ColumnName columnName){
        ColumnName result = columnName;
        if(columnName.getTableName() != null && fields.existTableAlias(columnName.getTableName().getName())){
            columnName.setTableName(fields.getTableName(columnName.getTableName().getName()));
        }

        if(fields.existColumnAlias(columnName.getName())){
            result = fields.getColumnName(columnName.getName());
        }
        return result;
    }

    /**
     * Search a table using a column name.
     * @param columnName The column name
     * @return com.stratio.crossdata.common.data.ColumnName
     * @throws ValidationException
     */
    public TableName searchTableNameByColumn(ColumnName columnName) throws ValidationException {
        TableName selectTableName = null;
        if (columnName.isCompletedName()) {
            if (MetadataManager.MANAGER.exists(columnName)) {
                selectTableName = columnName.getTableName();
            }
        } else {
            if (columnName.getTableName() == null) {
                boolean tableFind = false;
                for (TableName tableName : fields.getTableNames()) {
                    columnName.setTableName(tableName);
                    if (MetadataManager.MANAGER.exists(columnName)) {
                        if (tableFind) {
                            throw new AmbiguousNameException(columnName);
                        }
                        selectTableName = tableName;
                        tableFind = true;
                    } else {
                        columnName.setTableName(null);

                    }
                }
            } else {
                for (TableName tableName : fields.getTableNames()) {
                    if (tableName.getName().equalsIgnoreCase(columnName.getTableName().getName())) {
                        columnName.setTableName(tableName);
                        selectTableName = tableName;
                    }
                }
                if (!columnName.isCompletedName()) {
                    throw new NotValidColumnException(columnName);
                }
            }
        }
        if (selectTableName == null) {
            throw new NotExistNameException(columnName);
        }
        return selectTableName;
    }

    /**
     * Validate the conditions that have an asterisk.
     * @return List of ColumnSelector
     */
    public List<ColumnSelector> checkAsteriskSelector() {
        List<ColumnSelector> columnSelectors = new ArrayList<>();
        for (TableName table : fields.getTableNames()) {
            TableMetadata tableMetadata = MetadataManager.MANAGER.getTable(table);
            for (ColumnName columnName : tableMetadata.getColumns().keySet()) {
                ColumnSelector selector = new ColumnSelector(columnName);
                columnSelectors.add(selector);
                fields.getColumnNames().add(columnName);
            }
        }
        return columnSelectors;
    }

    /**
     * Obtain a list of selectors that were validated.
     * @param selectors The list of selectors to validate.
     * @return List of Selectors
     * @throws ValidationException
     */
    public List<Selector> checkListSelector(List<Selector> selectors) throws ValidationException {
        List<Selector> result = new ArrayList<>();
        for (Selector selector : selectors) {
            switch (selector.getType()) {
            case FUNCTION:
                FunctionSelector functionSelector = (FunctionSelector) selector;
                checkFunctionSelector(functionSelector);
                result.add(functionSelector);
                break;
            case COLUMN:
                ColumnSelector columnSelector = (ColumnSelector) selector;
                checkColumnSelector(columnSelector);
                result.add(columnSelector);
                break;
            case ASTERISK:
                result.addAll(checkAsteriskSelector());
                break;
            default:
                break;
            }
        }
        return result;
    }

    /**
     * Validate the Functions Selectors of a parsed query.
     * @param functionSelector The function Selector to validate.
     * @throws ValidationException
     */
    public void checkFunctionSelector(FunctionSelector functionSelector)
            throws ValidationException {
        // Check columns
        List<Selector> normalizeSelector = checkListSelector(functionSelector.getFunctionColumns());
        functionSelector.getFunctionColumns().clear();
        functionSelector.getFunctionColumns().addAll(normalizeSelector);
        // Check returning type
        //TODO: This should be updated with information from the MetadataManager
        functionSelector.setReturningType(ColumnType.INT);
    }

    private void checkRightSelector(ColumnName name, Operator operator, Selector rightTerm)
            throws ValidationException {
        // Get column type from MetadataManager
        ColumnMetadata columnMetadata = MetadataManager.MANAGER.getColumn(name);

        SelectorType rightTermType = rightTerm.getType();

        if (rightTerm.getType() == SelectorType.COLUMN) {
            ColumnSelector columnSelector = (ColumnSelector) rightTerm;
            ColumnName columnName = applyAlias(columnSelector.getName());
            columnSelector.setName(columnName);

            TableName foundTableName = this.searchTableNameByColumn(columnSelector.getName());
            columnSelector.getName().setTableName(foundTableName);

            ColumnMetadata columnMetadataRightTerm = MetadataManager.MANAGER.getColumn(columnSelector.getName());
            rightTermType = convertMetadataTypeToSelectorType(columnMetadataRightTerm.getColumnType());
        }

        // Create compatibilities table for ColumnType, Operator and SelectorType
        checkCompatibility(columnMetadata, operator, rightTermType);
    }

    private SelectorType convertMetadataTypeToSelectorType(ColumnType columnType) throws ValidationException {
        SelectorType selectorType = null;
        switch (columnType) {
        case INT:
        case BIGINT:
            selectorType = SelectorType.INTEGER;
            break;
        case DOUBLE:
        case FLOAT:
            selectorType = SelectorType.FLOATING_POINT;
            break;
        case TEXT:
        case VARCHAR:
            selectorType = SelectorType.STRING;
            break;
        case BOOLEAN:
            selectorType = SelectorType.BOOLEAN;
            break;
        case NATIVE:
        case SET:
        case LIST:
        case MAP:
            throw new BadFormatException("Type " + columnType + " not supported yet.");
        }
        return selectorType;
    }

    private void checkCompatibility(ColumnMetadata column, Operator operator, SelectorType valueType)
            throws ValidationException {
        switch (column.getColumnType()) {
        case BOOLEAN:
            checkBooleanCompatibility(column,operator,valueType);
            break;
        case INT:
        case BIGINT:
        case DOUBLE:
        case FLOAT:
            checkNumericCompatibility(column,valueType);
            break;
        case TEXT:
        case VARCHAR:
            checkStringCompatibility(column,operator,valueType);
            break;
        case NATIVE:
        case SET:
        case LIST:
        case MAP:
            throw new BadFormatException("Native and Collections not supported yet.");
        }
    }

    private void checkBooleanCompatibility(ColumnMetadata column, Operator operator, SelectorType valueType)
            throws ValidationException {
        if (operator != Operator.EQ) {
            throw new BadFormatException("Boolean relations only accept equal operator.");
        }
        if (valueType != SelectorType.BOOLEAN) {
            throw new NotMatchDataTypeException(column.getName());
        }
    }

    private void checkNumericCompatibility(ColumnMetadata column, SelectorType valueType)
            throws ValidationException {
        if ((valueType != SelectorType.INTEGER) && (valueType != SelectorType.FLOATING_POINT)) {
            throw new NotMatchDataTypeException(column.getName());
        }
    }

    private void checkStringCompatibility(ColumnMetadata column, Operator operator, SelectorType valueType)
            throws ValidationException {
        if (valueType != SelectorType.STRING) {
            throw new NotMatchDataTypeException(column.getName());
        }

        if(operator == Operator.MATCH){
            if(valueType != SelectorType.STRING){
                throw new BadFormatException("MATCH operator only accepts comparison with string literals.");
            }

            TableMetadata tableMetadata = MetadataManager.MANAGER.getTable(column.getName().getTableName());
            Map<IndexName, IndexMetadata> indexes = tableMetadata.getIndexes();
            if(indexes == null || indexes.isEmpty()){
                throw new BadFormatException("Table "+column.getName().getTableName() + " doesn't contain any index.");
            }

            boolean indexFound = false;
            Collection<IndexMetadata> indexesMetadata = indexes.values();
            Iterator<IndexMetadata> iter = indexesMetadata.iterator();
            while(iter.hasNext() && !indexFound){
                IndexMetadata indexMetadata = iter.next();
                if(indexMetadata.getColumns().containsKey(column.getName())){
                    if(indexMetadata.getType() != IndexType.FULL_TEXT){
                        throw new BadFormatException("MATCH operator can be only applied to FULL_TEXT indexes.");
                    } else {
                        indexFound = true;
                    }
                }
            }
            if(!indexFound){
                throw new BadFormatException("No index was found for the MATCH operator.");
            }
        } else if (operator != Operator.EQ && operator != Operator.GT && operator != Operator.GET
                && operator != Operator.LT && operator != Operator.LET && operator != Operator.DISTINCT) {
            throw new BadFormatException("String relations only accept equal operator.");
        }

    }
}
