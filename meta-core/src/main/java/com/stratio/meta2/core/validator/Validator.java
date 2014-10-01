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

package com.stratio.meta2.core.validator;

import java.util.List;

import org.apache.log4j.Logger;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.exceptions.validation.BadFormatException;
import com.stratio.meta.common.exceptions.validation.ExistNameException;
import com.stratio.meta.common.exceptions.validation.NotExistNameException;
import com.stratio.meta.common.exceptions.validation.NotMatchDataTypeException;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.Name;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.normalizer.Normalizator;
import com.stratio.meta2.core.normalizer.NormalizedFields;
import com.stratio.meta2.core.query.MetadataParsedQuery;
import com.stratio.meta2.core.query.MetadataValidatedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.query.SelectParsedQuery;
import com.stratio.meta2.core.query.SelectValidatedQuery;
import com.stratio.meta2.core.query.StorageParsedQuery;
import com.stratio.meta2.core.query.StorageValidatedQuery;
import com.stratio.meta2.core.query.ValidatedQuery;
import com.stratio.meta2.core.statements.AlterCatalogStatement;
import com.stratio.meta2.core.statements.AlterClusterStatement;
import com.stratio.meta2.core.statements.AlterTableStatement;
import com.stratio.meta2.core.statements.AttachClusterStatement;
import com.stratio.meta2.core.statements.AttachConnectorStatement;
import com.stratio.meta2.core.statements.CreateCatalogStatement;
import com.stratio.meta2.core.statements.CreateIndexStatement;
import com.stratio.meta2.core.statements.CreateTableStatement;
import com.stratio.meta2.core.statements.DescribeStatement;
import com.stratio.meta2.core.statements.DetachClusterStatement;
import com.stratio.meta2.core.statements.DetachConnectorStatement;
import com.stratio.meta2.core.statements.DropCatalogStatement;
import com.stratio.meta2.core.statements.DropConnectorStatement;
import com.stratio.meta2.core.statements.DropDataStoreStatement;
import com.stratio.meta2.core.statements.DropIndexStatement;
import com.stratio.meta2.core.statements.DropTableStatement;
import com.stratio.meta2.core.statements.InsertIntoStatement;
import com.stratio.meta2.core.statements.MetaStatement;

public class Validator {
    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Validator.class);
    private Normalizator normalizator=null;


    public ValidatedQuery validate(ParsedQuery parsedQuery)
            throws ValidationException, IgnoreQueryException {
        ValidatedQuery validatedQuery = null;
        LOG.info("Validating MetaStatements...");
        for (Validation val : parsedQuery.getStatement().getValidationRequirements().getValidations()) {
            switch (val) {
            case MUST_NOT_EXIST_CATALOG:
                validateNotExistCatalog(parsedQuery.getStatement());
                break;
            case MUST_EXIST_CATALOG:
                validateExistCatalog(parsedQuery.getStatement());
                break;
            case MUST_EXIST_TABLE:
                validateExistTable(parsedQuery.getStatement());
                break;
            case MUST_NOT_EXIST_TABLE:
                validateNotExistTable(parsedQuery.getStatement());
                break;
            case MUST_NOT_EXIST_CLUSTER:
                validateNotExistCluster(parsedQuery.getStatement());
                break;
            case MUST_EXIST_CLUSTER:
                validateExistCluster(parsedQuery.getStatement());
                break;
            case MUST_EXIST_CONNECTOR:
                validateExistConnector(parsedQuery.getStatement());
                break;
            case MUST_NOT_EXIST_CONNECTOR:
                validateNotExistConnector(parsedQuery.getStatement());
                break;
            case MUST_EXIST_DATASTORE:
                validateExistDatastore(parsedQuery.getStatement());
                break;
            case MUST_NOT_EXIST_DATASTORE:
                validateNotExistDatastore(parsedQuery.getStatement());
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
                validateNotExistIndex(parsedQuery.getStatement());
                break;
            case MUST_EXIST_INDEX:
                validateExistIndex(parsedQuery.getStatement());
                break;
            case MUST_EXIST_COLUMN:
                validateExistColumn(parsedQuery.getStatement());
                break;
            case MUST_NOT_EXIST_COLUMN:
                validateNotExistColumn(parsedQuery.getStatement());
                break;
            case VALIDATE_TYPES:
                validateInsertTypes(parsedQuery.getStatement());
                break;
            case VALIDATE_SELECT:
                validateSelect(parsedQuery);
                break;
            default:
                break;
            }
        }

        if (parsedQuery instanceof MetadataParsedQuery) {
            validatedQuery = new MetadataValidatedQuery((MetadataParsedQuery) parsedQuery);
        } else if (parsedQuery instanceof StorageParsedQuery) {
            validatedQuery = new StorageValidatedQuery((StorageParsedQuery) parsedQuery);
        } else if (parsedQuery instanceof SelectParsedQuery) {
            validatedQuery = new SelectValidatedQuery((SelectParsedQuery) parsedQuery);
            NormalizedFields fields= normalizator.getFields();


        }

        return validatedQuery;
    }

    private void validateSelect(ParsedQuery parsedQuery) throws ValidationException {
        SelectParsedQuery selectParsedQuery = (SelectParsedQuery) parsedQuery;
        normalizator = new Normalizator(selectParsedQuery);
        normalizator.execute();
    }

    private void validateNotExistConnector(MetaStatement stmt) throws IgnoreQueryException, ExistNameException {
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

        validateNotExist(name, hasIfExist);

    }

    private void validateExistConnector(MetaStatement stmt) throws IgnoreQueryException, NotExistNameException {
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

        validateExist(name, hasIfExist);
    }

    private void validateExistCluster(MetaStatement stmt) throws IgnoreQueryException, NotExistNameException {
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

        validateExist(name, hasIfExists);
    }

    private void validateNotExistCluster(MetaStatement stmt) throws IgnoreQueryException, ExistNameException {
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

        if (stmt instanceof CreateTableStatement) {
            CreateTableStatement createTableStatement = (CreateTableStatement) stmt;
            name = createTableStatement.getClusterName();
            hasIfExists = createTableStatement.isIfNotExists();
        }
        if (stmt instanceof DetachConnectorStatement) {
            DetachConnectorStatement detachConnectorStatement = (DetachConnectorStatement) stmt;
            name = detachConnectorStatement.getClusterName();
        }

        validateNotExist(name, hasIfExists);
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

    private void validateExistDatastore(MetaStatement statement) throws IgnoreQueryException, NotExistNameException {
        Name name = null;
        boolean hasIfExists = false;
        if (statement instanceof AttachClusterStatement) {
            name = (((AttachClusterStatement) statement).getDatastoreName());
            hasIfExists = ((AttachClusterStatement) statement).isIfNotExists();
        }
        if (statement instanceof DropDataStoreStatement) {
            name = new DataStoreName(((DropDataStoreStatement) statement).getName());
        }
        validateExist(name, hasIfExists);
    }

    private void validateNotExistDatastore(MetaStatement statement) throws IgnoreQueryException, ExistNameException {
        Name name = null;
        boolean hasIfExists = false;
        if (statement instanceof AttachClusterStatement) {
            name = (((AttachClusterStatement) statement).getDatastoreName());
            hasIfExists = ((AttachClusterStatement) statement).isIfNotExists();
        }
        validateNotExist(name, hasIfExists);
    }

    private void validateExistColumn(MetaStatement stmt)
            throws NotExistNameException, IgnoreQueryException {
        ColumnName columnName = null;
        if (stmt instanceof AlterTableStatement) {
            columnName = ((AlterTableStatement) stmt).getColumn();
        }
        validateExist(columnName, false);

    }

    private void validateNotExistColumn(MetaStatement stmt)
            throws ExistNameException, IgnoreQueryException {
        ColumnName columnName = null;
        if (stmt instanceof AlterTableStatement) {
            columnName = ((AlterTableStatement) stmt).getColumn();
        }
        validateNotExist(columnName, false);

    }

    private void validateExistsProperties(MetaStatement stmt) throws ValidationException {

        if (stmt instanceof AlterCatalogStatement) {
            AlterCatalogStatement alterCatalogStatement = (AlterCatalogStatement) stmt;
            if (alterCatalogStatement.getOptions() == null || alterCatalogStatement.getOptions().isEmpty()) {
                throw new ValidationException("AlterCatalog options can't be empty");
            }

        }
        if (stmt instanceof AlterClusterStatement) {
            AlterClusterStatement alterClusterStatement = (AlterClusterStatement) stmt;
            if (alterClusterStatement.getOptions() == null || alterClusterStatement.getOptions().isEmpty()) {
                throw new ValidationException("AlterCluster options can't be empty");
            }
        }

    }

    private void validateExistTable(MetaStatement stmt)
            throws NotExistNameException, IgnoreQueryException {
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

        validateExist(name, hasIfExists);
    }

    private void validateNotExistTable(MetaStatement stmt)
            throws ExistNameException, IgnoreQueryException {
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

        if (stmt instanceof DetachClusterStatement) {
            DetachClusterStatement detachClusterStatement = (DetachClusterStatement) stmt;
            name = new ClusterName(detachClusterStatement.getClusterName());
        }

        if (stmt instanceof AttachClusterStatement) {
            AttachClusterStatement attachClusterStatement = (AttachClusterStatement) stmt;
            name = (attachClusterStatement.getClusterName());
            hasIfExists = attachClusterStatement.isIfNotExists();
        }

        validateNotExist(name, hasIfExists);
    }

    private void validateNotExistCatalog(MetaStatement stmt) throws IgnoreQueryException, ExistNameException {
        Name name = null;
        boolean hasIfExists = false;
        if (stmt instanceof AlterCatalogStatement) {
            AlterCatalogStatement alterCatalogStatement = (AlterCatalogStatement) stmt;
            name = alterCatalogStatement.getCatalogName();
        }

        if (stmt instanceof CreateCatalogStatement) {
            CreateCatalogStatement createCatalogStatement = (CreateCatalogStatement) stmt;
            hasIfExists = createCatalogStatement.isIfNotExists();
            name = ((CreateCatalogStatement) stmt).getCatalogName();
        }

        if (stmt instanceof DropCatalogStatement) {
            DropCatalogStatement dropCatalogStatement = (DropCatalogStatement) stmt;
            hasIfExists = dropCatalogStatement.isIfExists();
            name = ((DropCatalogStatement) stmt).getCatalogName();
        }

        if (stmt instanceof CreateTableStatement) {
            CreateTableStatement createTableStatement = (CreateTableStatement) stmt;
            name = createTableStatement.getEffectiveCatalog();
            hasIfExists = createTableStatement.isIfNotExists();
        }
        validateNotExist(name, hasIfExists);
    }

    private void validateExistCatalog(MetaStatement stmt) throws IgnoreQueryException, NotExistNameException {
        Name name = null;
        boolean hasIfExists = false;
        if (stmt instanceof AlterCatalogStatement) {
            AlterCatalogStatement alterCatalogStatement = (AlterCatalogStatement) stmt;
            name = alterCatalogStatement.getCatalogName();
        }

        if (stmt instanceof CreateCatalogStatement) {
            CreateCatalogStatement createCatalogStatement = (CreateCatalogStatement) stmt;
            name = createCatalogStatement.getCatalogName();
            hasIfExists = createCatalogStatement.isIfNotExists();

        }

        if (stmt instanceof DropCatalogStatement) {
            DropCatalogStatement dropCatalogStatement = (DropCatalogStatement) stmt;
            name = dropCatalogStatement.getCatalogName();
            hasIfExists = dropCatalogStatement.isIfExists();
        }

        if (stmt instanceof DescribeStatement) {
            DescribeStatement describeStatement = (DescribeStatement) stmt;
            name = describeStatement.getEffectiveCatalog();
        }

        if (stmt instanceof CreateTableStatement) {
            CreateTableStatement createTableStatement = (CreateTableStatement) stmt;
            name = createTableStatement.getEffectiveCatalog();
            hasIfExists = createTableStatement.isIfNotExists();
        }

        if (stmt instanceof DropTableStatement) {
            DropTableStatement dropTableStatement = (DropTableStatement) stmt;
            name = dropTableStatement.getCatalogName();
            hasIfExists = ((DropTableStatement) stmt).isIfExists();
        }

        if (stmt instanceof InsertIntoStatement) {
            InsertIntoStatement insertIntoStatement = (InsertIntoStatement) stmt;
            name = insertIntoStatement.getCatalogName();
            hasIfExists = insertIntoStatement.isIfNotExists();
        }
        validateExist(name, hasIfExists);
    }

    private void validateOptions(MetaStatement stmt) throws ValidationException {

        if (stmt instanceof AttachClusterStatement) {
            AttachClusterStatement myStmt = (AttachClusterStatement) stmt;
            if (myStmt.getOptions().isEmpty()) {
                throw new ValidationException("AttachClusterStatement options can't be empty");
            }
        } else {
            if (stmt instanceof AttachConnectorStatement) {
                AttachConnectorStatement myStmt = (AttachConnectorStatement) stmt;
                if ((myStmt.getOptions() == null || myStmt.getOptions().isEmpty())) {
                    throw new ValidationException("AttachConnectorStatement options can't be empty");
                }
            }
        }

    }

    private void validateNotExistIndex(MetaStatement stmt) throws IgnoreQueryException, ExistNameException {
        Name name = null;
        boolean hasIfExist = false;
        if (stmt instanceof CreateIndexStatement) {
            CreateIndexStatement createIndexStatement = (CreateIndexStatement) stmt;
            name = createIndexStatement.getName();
            hasIfExist = ((CreateIndexStatement) stmt).isCreateIfNotExists();
        }

        if (stmt instanceof DropIndexStatement) {
            DropIndexStatement dropIndexStatement = (DropIndexStatement) stmt;
            name = dropIndexStatement.getName();
            hasIfExist = ((CreateIndexStatement) stmt).isCreateIfNotExists();
        }
        validateNotExist(name, hasIfExist);
    }

    private void validateExistIndex(MetaStatement stmt) throws IgnoreQueryException, NotExistNameException {
        Name name = null;
        boolean hasIfExist = false;
        if (stmt instanceof CreateIndexStatement) {
            CreateIndexStatement createIndexStatement = (CreateIndexStatement) stmt;
            name = createIndexStatement.getName();
            hasIfExist = ((CreateIndexStatement) stmt).isCreateIfNotExists();
        }

        if (stmt instanceof DropIndexStatement) {
            DropIndexStatement dropIndexStatement = (DropIndexStatement) stmt;
            name = dropIndexStatement.getName();
            hasIfExist = ((CreateIndexStatement) stmt).isCreateIfNotExists();
        }
        validateExist(name, hasIfExist);
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

        switch (right.getType()) {
        case COLUMN:
            ColumnName rightColumnName = ((ColumnSelector) right).getName();
            ColumnMetadata rightColumnMetadata = MetadataManager.MANAGER.getColumn(rightColumnName);
            if (columnMetadata.getColumnType() != rightColumnMetadata.getColumnType()) {
                throw new NotMatchDataTypeException(rightColumnName);
            }
            break;
        case ASTERISK:
            throw new BadFormatException("Asterisk not supported in relations.");
        case BOOLEAN:
            if (columnMetadata.getColumnType() != ColumnType.BOOLEAN) {
                throw new NotMatchDataTypeException(columnMetadata.getName());
            }
            break;
        case STRING:
            if (columnMetadata.getColumnType() != ColumnType.TEXT) {
                throw new NotMatchDataTypeException(columnMetadata.getName());
            }
            break;
        case INTEGER:
            if (columnMetadata.getColumnType() != ColumnType.INT &&
                    columnMetadata.getColumnType() != ColumnType.BIGINT) {
                throw new NotMatchDataTypeException(columnMetadata.getName());
            }
            break;
        case FLOATING_POINT:
            if (columnMetadata.getColumnType() != ColumnType.FLOAT &&
                    columnMetadata.getColumnType() != ColumnType.DOUBLE) {
                throw new NotMatchDataTypeException(columnMetadata.getName());
            }
            break;
        case RELATION:
            throw new BadFormatException("Operation not supported in where.");
        }
    }

}
