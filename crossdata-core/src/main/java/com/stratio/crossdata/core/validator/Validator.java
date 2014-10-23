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

package com.stratio.crossdata.core.validator;

import java.util.List;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.data.ConnectorStatus;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.exceptions.validation.BadFormatException;
import com.stratio.crossdata.common.exceptions.validation.ExistNameException;
import com.stratio.crossdata.common.exceptions.validation.NotConnectionException;
import com.stratio.crossdata.common.exceptions.validation.NotExistNameException;
import com.stratio.crossdata.common.exceptions.validation.NotMatchDataTypeException;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.Name;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.statements.structures.selectors.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.selectors.Selector;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.normalizer.Normalizator;
import com.stratio.crossdata.core.normalizer.NormalizedFields;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.IValidatedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.query.MetadataValidatedQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.query.SelectValidatedQuery;
import com.stratio.crossdata.core.query.StorageParsedQuery;
import com.stratio.crossdata.core.query.StorageValidatedQuery;
import com.stratio.crossdata.core.statements.AlterCatalogStatement;
import com.stratio.crossdata.core.statements.AlterClusterStatement;
import com.stratio.crossdata.core.statements.AlterTableStatement;
import com.stratio.crossdata.core.statements.AttachClusterStatement;
import com.stratio.crossdata.core.statements.AttachConnectorStatement;
import com.stratio.crossdata.core.statements.CreateCatalogStatement;
import com.stratio.crossdata.core.statements.CreateIndexStatement;
import com.stratio.crossdata.core.statements.CreateTableStatement;
import com.stratio.crossdata.core.statements.DeleteStatement;
import com.stratio.crossdata.core.statements.DescribeStatement;
import com.stratio.crossdata.core.statements.DetachClusterStatement;
import com.stratio.crossdata.core.statements.DetachConnectorStatement;
import com.stratio.crossdata.core.statements.DropCatalogStatement;
import com.stratio.crossdata.core.statements.DropConnectorStatement;
import com.stratio.crossdata.core.statements.DropDataStoreStatement;
import com.stratio.crossdata.core.statements.DropIndexStatement;
import com.stratio.crossdata.core.statements.DropTableStatement;
import com.stratio.crossdata.core.statements.InsertIntoStatement;
import com.stratio.crossdata.core.statements.MetaStatement;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

public class Validator {
    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Validator.class);
    private Normalizator normalizator = null;

    public IValidatedQuery validate(IParsedQuery parsedQuery) throws ValidationException, IgnoreQueryException {
        IValidatedQuery validatedQuery = null;
        LOG.info("Validating MetaStatements...");
        for (ValidationTypes val : parsedQuery.getStatement().getValidationRequirements().getValidations()) {
            try {
                switch (val) {
                case MUST_NOT_EXIST_CATALOG:
                    validateCatalog(parsedQuery.getStatement(), false);
                    break;
                case MUST_EXIST_CATALOG:
                    validateCatalog(parsedQuery.getStatement(), true);
                    break;
                case MUST_EXIST_TABLE:
                    validateTable(parsedQuery.getStatement(), true);
                    break;
                case MUST_NOT_EXIST_TABLE:
                    validateTable(parsedQuery.getStatement(), false);
                    break;
                case MUST_NOT_EXIST_CLUSTER:
                    validateCluster(parsedQuery.getStatement(), false);
                    break;
                case MUST_EXIST_CLUSTER:
                    validateCluster(parsedQuery.getStatement(), true);
                    break;
                case MUST_EXIST_CONNECTOR:
                    validateConnector(parsedQuery.getStatement(), true);
                    break;
                case MUST_NOT_EXIST_CONNECTOR:
                    validateConnector(parsedQuery.getStatement(), false);
                    break;
                case MUST_EXIST_DATASTORE:
                    validateDatastore(parsedQuery.getStatement(), true);
                    break;
                case MUST_NOT_EXIST_DATASTORE:
                    validateDatastore(parsedQuery.getStatement(), false);
                    break;
                case VALID_CLUSTER_OPTIONS:
                    validateOptions(parsedQuery.getStatement());
                    break;
                case VALID_CONNECTOR_OPTIONS:
                    validateOptions(parsedQuery.getStatement());
                    break;
                case MUST_EXIST_ATTACH_CONNECTOR_CLUSTER:
                    break;
                case MUST_EXIST_PROPERTIES:
                    validateExistsProperties(parsedQuery.getStatement());
                    break;
                case MUST_NOT_EXIST_INDEX:
                    validateIndex(parsedQuery.getStatement(), false);
                    break;
                case MUST_EXIST_INDEX:
                    validateIndex(parsedQuery.getStatement(), true);
                    break;
                case MUST_EXIST_COLUMN:
                    validateColumn(parsedQuery.getStatement(), true);
                    break;
                case MUST_NOT_EXIST_COLUMN:
                    validateColumn(parsedQuery.getStatement(), false);
                    break;
                case VALIDATE_TYPES:
                    validateInsertTypes(parsedQuery.getStatement());
                    break;
                case VALIDATE_SELECT:
                    validateSelect(parsedQuery);
                    break;
                case MUST_BE_CONNECTED:
                    validateConnectorConnected(parsedQuery.getStatement());
                    break;
                default:
                    break;
                }
            }catch(ValidationException ve){
                throw ve;
            }catch(IgnoreQueryException iqe){
                throw iqe;
            }
        }

        if (parsedQuery instanceof MetadataParsedQuery) {
            validatedQuery = new MetadataValidatedQuery((MetadataParsedQuery) parsedQuery);
        } else if (parsedQuery instanceof StorageParsedQuery) {
            validatedQuery = new StorageValidatedQuery((StorageParsedQuery) parsedQuery);
        } else if (parsedQuery instanceof SelectParsedQuery) {
            validatedQuery = new SelectValidatedQuery((SelectParsedQuery) parsedQuery);
            NormalizedFields fields = normalizator.getFields();
            ((SelectValidatedQuery) validatedQuery).setTableMetadata(fields.getTablesMetadata());
            ((SelectValidatedQuery) validatedQuery).getColumns().addAll(fields.getColumnNames());
            ((SelectValidatedQuery) validatedQuery).getTables().addAll(fields.getTableNames());
            ((SelectValidatedQuery) validatedQuery).getRelationships().addAll(fields.getWhere());
            ((SelectValidatedQuery) validatedQuery).setJoin(fields.getJoin());

        }

        return validatedQuery;
    }

    private void validateConnectorConnected(MetaStatement statement) throws ValidationException {
        if(statement instanceof AttachConnectorStatement){
            AttachConnectorStatement attachConnectorStatement = (AttachConnectorStatement) statement;
            ConnectorName connectorName = attachConnectorStatement.getConnectorName();
            if(!MetadataManager.MANAGER.checkConnectorStatus(connectorName, ConnectorStatus.ONLINE)){
                throw new NotConnectionException("Connector "+ connectorName + " is not connected.");
            }
        } else {
            throw new NotConnectionException("Invalid validation [MUST_BE_CONNECTED] for "+ statement);
        }
    }

    private void validateName(boolean exist, Name name, boolean hasIfExist)
            throws IgnoreQueryException, NotExistNameException, ExistNameException {
        if (exist) {
            validateExist(name, hasIfExist);
        } else {
            validateNotExist(name, hasIfExist);
        }

    }

    private void validateSelect(IParsedQuery parsedQuery) throws ValidationException {
        SelectParsedQuery selectParsedQuery = (SelectParsedQuery) parsedQuery;
        normalizator = new Normalizator(selectParsedQuery);
        normalizator.execute();
    }

    private void validateConnector(MetaStatement stmt, boolean exist) throws IgnoreQueryException,
            ExistNameException, NotExistNameException {
        Name name = null;
        boolean hasIfExist = false;

        if (stmt instanceof AttachConnectorStatement) {
            AttachConnectorStatement attachConnectorStatement = (AttachConnectorStatement) stmt;
            name = attachConnectorStatement.getConnectorName();
        }

        if (stmt instanceof DetachConnectorStatement) {
            DetachConnectorStatement detachConnectorStatement = (DetachConnectorStatement) stmt;
            name = detachConnectorStatement.getConnectorName();
        }

        if (stmt instanceof DropConnectorStatement) {
            DropConnectorStatement dropConnectorStatement = (DropConnectorStatement) stmt;
            name = new ConnectorName(dropConnectorStatement.getName());
        }
        validateName(exist, name, hasIfExist);
    }

    private void validateCluster(MetaStatement stmt, boolean exist) throws IgnoreQueryException,
            NotExistNameException, ExistNameException {
        Name name = null;
        boolean hasIfExists = false;

        if (stmt instanceof AlterClusterStatement) {
            name = new ClusterName(((AlterClusterStatement) stmt).getClusterName());
            hasIfExists = ((AlterClusterStatement) stmt).isIfExists();
        }

        if (stmt instanceof AttachClusterStatement) {
            name = (((AttachClusterStatement) stmt).getClusterName());
            hasIfExists = ((AttachClusterStatement) stmt).isIfNotExists();
        }

        if (stmt instanceof DetachClusterStatement) {
            name = new ClusterName(((DetachClusterStatement) stmt).getClusterName());
        }

        if (stmt instanceof AttachConnectorStatement) {
            name = (((AttachConnectorStatement) stmt).getClusterName());
        }

        if (stmt instanceof CreateTableStatement) {
            CreateTableStatement createTableStatement = (CreateTableStatement) stmt;
            name = createTableStatement.getClusterName();
            hasIfExists = createTableStatement.isIfNotExists();
        }
        if (stmt instanceof DetachConnectorStatement) {
            DetachConnectorStatement detachConnectorStatement = (DetachConnectorStatement) stmt;
            name = detachConnectorStatement.getClusterName();
        }

        validateName(exist, name, hasIfExists);
    }

    private void validateExist(Name name, boolean hasIfExists)
            throws NotExistNameException, IgnoreQueryException {
        if (!MetadataManager.MANAGER.exists(name)) {
            if (hasIfExists) {
                throw new IgnoreQueryException("[" + name + "] doesn't exist");
            } else {
                throw new NotExistNameException(name);
            }
        }
    }

    private void validateNotExist(Name name, boolean hasIfExists)
            throws ExistNameException, IgnoreQueryException {
        if (MetadataManager.MANAGER.exists(name)) {
            if (hasIfExists) {
                throw new IgnoreQueryException("[" + name + "] doesn't exist");
            } else {
                throw new ExistNameException(name);
            }
        }
    }

    private void validateDatastore(MetaStatement statement, boolean exist) throws IgnoreQueryException,
            NotExistNameException, ExistNameException {
        Name name = null;
        boolean hasIfExists = false;
        if (statement instanceof AttachClusterStatement) {
            name = (((AttachClusterStatement) statement).getDatastoreName());
            hasIfExists = ((AttachClusterStatement) statement).isIfNotExists();
        }
        if (statement instanceof DropDataStoreStatement) {
            name = new DataStoreName(((DropDataStoreStatement) statement).getName());
        }
        validateName(exist, name, hasIfExists);
    }

    private void validateColumn(MetaStatement stmt, boolean exist)
            throws NotExistNameException, IgnoreQueryException, ExistNameException {
        ColumnName columnName = null;
        if (stmt instanceof AlterTableStatement) {
            columnName = ((AlterTableStatement) stmt).getColumn();
            validateName(exist, columnName, false);
        }

        if (stmt instanceof CreateIndexStatement) {
            CreateIndexStatement createIndexStatement = (CreateIndexStatement) stmt;
            for(ColumnName column:createIndexStatement.getTargetColumns()){
                validateName(exist, column, false);
            }
        }
    }

    private void validateExistsProperties(MetaStatement stmt) throws ValidationException {

        if (stmt instanceof AlterCatalogStatement) {
            AlterCatalogStatement alterCatalogStatement = (AlterCatalogStatement) stmt;
            if (alterCatalogStatement.getOptions() == null || alterCatalogStatement.getOptions().isEmpty()) {
                throw new BadFormatException("AlterCatalog options can't be empty");
            }

        }
        if (stmt instanceof AlterClusterStatement) {
            AlterClusterStatement alterClusterStatement = (AlterClusterStatement) stmt;
            if (alterClusterStatement.getOptions() == null || alterClusterStatement.getOptions().isEmpty()) {
                throw new BadFormatException("AlterCluster options can't be empty");
            }
        }

    }

    private void validateTable(MetaStatement stmt, boolean exist)
            throws NotExistNameException, IgnoreQueryException, ExistNameException {
        Name name = null;
        boolean hasIfExists = false;
        if (stmt instanceof AlterTableStatement) {
            name = ((AlterTableStatement) stmt).getTableName();
        }
        if (stmt instanceof DropTableStatement) {
            name = ((DropTableStatement) stmt).getTableName();
            hasIfExists = ((DropTableStatement) stmt).isIfExists();
        }

        if (stmt instanceof CreateTableStatement) {
            CreateTableStatement createTableStatement = (CreateTableStatement) stmt;
            name = createTableStatement.getTableName();
            hasIfExists = createTableStatement.isIfNotExists();
        }

        if (stmt instanceof DescribeStatement) {
            DescribeStatement describeStatement = (DescribeStatement) stmt;
            name = describeStatement.getTableName();
        }

        if (stmt instanceof InsertIntoStatement) {
            InsertIntoStatement insertIntoStatement = (InsertIntoStatement) stmt;
            name = insertIntoStatement.getTableName();
            hasIfExists = insertIntoStatement.isIfNotExists();
        }

        if (stmt instanceof DeleteStatement) {
            DeleteStatement deleteStatement = (DeleteStatement) stmt;
            name = deleteStatement.getTableName();
        }

        if (stmt instanceof DetachClusterStatement) {
            DetachClusterStatement detachClusterStatement = (DetachClusterStatement) stmt;
            name =detachClusterStatement.getTableMetadata().getName();
        }

        if (stmt instanceof AttachClusterStatement) {
            AttachClusterStatement attachClusterStatement = (AttachClusterStatement) stmt;
            name = attachClusterStatement.getTableMetadata().getName();
            hasIfExists = attachClusterStatement.isIfNotExists();
        }

        if (stmt instanceof CreateIndexStatement) {
            CreateIndexStatement createIndexStatement = (CreateIndexStatement) stmt;
            name = createIndexStatement.getTableName();
            hasIfExists = createIndexStatement.isCreateIfNotExists();
        }


        validateName(exist, name, hasIfExists);
    }

    private void validateCatalog(MetaStatement stmt, boolean exist)
            throws IgnoreQueryException, ExistNameException, NotExistNameException {
        Name name = null;
        boolean validate=true;
        boolean hasIfExists = false;
        if (stmt instanceof AlterCatalogStatement) {
            AlterCatalogStatement alterCatalogStatement = (AlterCatalogStatement) stmt;
            name = alterCatalogStatement.getCatalogName();
        } else if (stmt instanceof CreateCatalogStatement) {
            CreateCatalogStatement createCatalogStatement = (CreateCatalogStatement) stmt;
            hasIfExists = createCatalogStatement.isIfNotExists();
            name = createCatalogStatement.getCatalogName();
        } else if (stmt instanceof DropCatalogStatement) {
            DropCatalogStatement dropCatalogStatement = (DropCatalogStatement) stmt;
            hasIfExists = dropCatalogStatement.isIfExists();
            name = dropCatalogStatement.getCatalogName();
        } else if (stmt instanceof CreateTableStatement) {
            CreateTableStatement createTableStatement = (CreateTableStatement) stmt;
            name = createTableStatement.getEffectiveCatalog();
            hasIfExists = createTableStatement.isIfNotExists();
        } else if (stmt instanceof DescribeStatement) {
            DescribeStatement describeStatement = (DescribeStatement) stmt;
            name = describeStatement.getEffectiveCatalog();
        } else if (stmt instanceof DropTableStatement) {
            DropTableStatement dropTableStatement = (DropTableStatement) stmt;
            name = dropTableStatement.getCatalogName();
            hasIfExists = dropTableStatement.isIfExists();
        } else if (stmt instanceof InsertIntoStatement) {
            InsertIntoStatement insertIntoStatement = (InsertIntoStatement) stmt;
            name = insertIntoStatement.getCatalogName();
            hasIfExists = insertIntoStatement.isIfNotExists();
        } else{
            //TODO: ¿should through exception?
            //Correctness - Method call passes null for nonnull parameter
            validate=false;
        }

        if(validate){
            validateName(exist, name, hasIfExists);
        }
    }

    private void validateOptions(MetaStatement stmt) throws ValidationException {

        if (stmt instanceof AttachClusterStatement) {
            AttachClusterStatement myStmt = (AttachClusterStatement) stmt;
            if (myStmt.getOptions().isEmpty()) {
                throw new BadFormatException("AttachClusterStatement options can't be empty");
            }
        } else {
            if (stmt instanceof AttachConnectorStatement) {
                AttachConnectorStatement myStmt = (AttachConnectorStatement) stmt;
                if (myStmt.getOptions() == null || myStmt.getOptions().isEmpty()) {
                    throw new BadFormatException("AttachConnectorStatement options can't be empty");
                }
            }
        }

    }

    private void validateIndex(MetaStatement stmt, boolean exist)
            throws IgnoreQueryException, ExistNameException, NotExistNameException {
        Name name = null;
        boolean hasIfExist = false;
        if (stmt instanceof CreateIndexStatement) {
            CreateIndexStatement createIndexStatement = (CreateIndexStatement) stmt;
            name = createIndexStatement.getName();
            hasIfExist = createIndexStatement.isCreateIfNotExists();
        }

        if (stmt instanceof DropIndexStatement) {
            DropIndexStatement dropIndexStatement = (DropIndexStatement) stmt;
            name = dropIndexStatement.getName();
            hasIfExist = dropIndexStatement.isDropIfExists();
        }
        validateName(exist, name, hasIfExist);
    }

    private void validateInsertTypes(MetaStatement stmt)
            throws BadFormatException, NotMatchDataTypeException {
        if (stmt instanceof InsertIntoStatement) {
            InsertIntoStatement insertIntoStatement = (InsertIntoStatement) stmt;
            List<ColumnName> columnNameList = insertIntoStatement.getIds();
            List<Selector> selectorList = insertIntoStatement.getCellValues();

            for (int i = 0; i < columnNameList.size(); i++) {
                ColumnName columnName = columnNameList.get(i);
                Selector valueSelector = selectorList.get(i);
                ColumnMetadata columnMetadata = MetadataManager.MANAGER.getColumn(columnName);

                validateColumnType(columnMetadata, valueSelector);
            }
        }
    }

    public void validateColumnType(ColumnMetadata columnMetadata, Selector right)
            throws BadFormatException, NotMatchDataTypeException {
        NotMatchDataTypeException notMatchDataTypeException=null;
        BadFormatException badFormatException=null;
        switch (right.getType()) {
        case COLUMN:
            ColumnName rightColumnName = ((ColumnSelector) right).getName();
            ColumnMetadata rightColumnMetadata = MetadataManager.MANAGER.getColumn(rightColumnName);
            if (columnMetadata.getColumnType() != rightColumnMetadata.getColumnType()) {
                notMatchDataTypeException=new NotMatchDataTypeException(rightColumnName);
            }
            break;
        case ASTERISK:
            badFormatException=new BadFormatException("Asterisk not supported in relations.");
            break;
        case BOOLEAN:
            if (columnMetadata.getColumnType() != ColumnType.BOOLEAN) {
                notMatchDataTypeException=new NotMatchDataTypeException(columnMetadata.getName());
            }
            break;
        case STRING:
            if (columnMetadata.getColumnType() != ColumnType.TEXT) {
                notMatchDataTypeException=new NotMatchDataTypeException(columnMetadata.getName());
            }
            break;
        case INTEGER:
            if (columnMetadata.getColumnType() != ColumnType.INT &&
                    columnMetadata.getColumnType() != ColumnType.BIGINT) {
                notMatchDataTypeException=new NotMatchDataTypeException(columnMetadata.getName());
            }
            break;
        case FLOATING_POINT:
            if (columnMetadata.getColumnType() != ColumnType.FLOAT &&
                    columnMetadata.getColumnType() != ColumnType.DOUBLE) {
                notMatchDataTypeException=new NotMatchDataTypeException(columnMetadata.getName());
            }
            break;
        case RELATION:
            badFormatException=new BadFormatException("Operation not supported in where.");
            break;
        }
        if(notMatchDataTypeException!=null){
            throw notMatchDataTypeException;
        }else if (badFormatException!=null){
            throw badFormatException;
        }
    }

}
