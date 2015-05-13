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

package com.stratio.crossdata.core.validator.requirements;

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.exceptions.validation.ExistNameException;
import com.stratio.crossdata.common.exceptions.validation.NotExistNameException;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.validator.ValidatorHelper;

import java.util.Set;

/**
 * Enumeration that defines the types of validations clauses for the validator.
 */
public enum ValidationTypes {
    MUST_NOT_EXIST_CATALOG {
        /**
         * Validate if the Catalog does not exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateCatalog(parsedQuery.getStatement(), false);
        }
    },
    MUST_EXIST_CATALOG {
        /**
         * Validate if the Catalog exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateCatalog(parsedQuery.getStatement(), true);
        }
    },
    MUST_EXIST_TABLE {
        /**
         * Validate if the Connector exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateTable(parsedQuery.getStatement(), true);
        }
    },
    MUST_NOT_EXIST_TABLE {
        /**
         * Validate if the table does not exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateTable(parsedQuery.getStatement(), false);
        }
    },
    MUST_NOT_EXIST_CLUSTER {
        /**
         * Validate if the Cluster does not exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateCluster(parsedQuery.getStatement(), false);
        }
    },
    MUST_EXIST_CLUSTER {
        /**
         * Validate if the Cluster exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateCluster(parsedQuery.getStatement(), true);
        }
    },
    MUST_EXIST_CONNECTOR {
        /**
         * Validate if the Connector exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateConnector(parsedQuery.getStatement(), true);
        }
    },
    MUST_NOT_EXIST_CONNECTOR {
        /**
         * Validate if the Connector does nos exist.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateConnector(parsedQuery.getStatement(), false);
        }
    },
    MUST_EXIST_DATASTORE {
        /**
         *
         * Validate if the Datastore Exist.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateDatastore(parsedQuery.getStatement(), true);
        }
    },
    MUST_NOT_EXIST_DATASTORE {
        /**
         * Validate if the Datastore does not Exist.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            validatorHelper.validateDatastore(parsedQuery.getStatement(), false);
        }
    },
    VALID_DATASTORE_MANIFEST {
        /**
         * Does not implemented yet.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ExistNameException
         * @throws NotExistNameException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ExistNameException, NotExistNameException {
            return;
        }
    },

    VALID_CLUSTER_OPTIONS {
        /**
         * Validates the Cluster Options.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateOptions(parsedQuery.getStatement());
        }
    },
    VALID_CONNECTOR_OPTIONS {
        /**
         * Validate the connector options.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateOptions(parsedQuery.getStatement());
        }
    },
    MUST_EXIST_ATTACH_CONNECTOR_CLUSTER {

        /**
         * Validate if the Connector is attached to a cluster.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateConnectorAttachedRefs(parsedQuery.getStatement());
        }
    },


    VALID_CONNECTOR_MANIFEST {
        /**
         * Does not implemented yet.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            return;
        }
    },
    MUST_EXIST_PROPERTIES {
        /**
         * Validates if the Propieties exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateExistsProperties(parsedQuery.getStatement());
        }
    },

    MUST_NOT_EXIST_INDEX {

        /**
         * Validathe if an Index does not exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateIndex(parsedQuery.getStatement(), false);
        }
    },
    MUST_EXIST_INDEX {
        /**
         * Validathe if an Index exist.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateIndex(parsedQuery.getStatement(), true);
        }
    },
    MUST_EXIST_COLUMN {
        /**
         * Validates if the Column exist.
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateColumn(parsedQuery.getStatement(), true);
        }
    },
    MUST_NOT_EXIST_COLUMN {

        /**
         * Validates that the columns does not exist.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateColumn(parsedQuery.getStatement(), false);
        }
    },

    VALIDATE_TYPES {

        /**
         *
         * Validates the inserts types.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateInsertTypes(parsedQuery);
        }
    },

    VALIDATE_SELECT {
        /**
         *
         * Validate if the select is valid.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateSelect(parsedQuery, parentsTableNames);
        }
    },
    VALIDATE_PRIORITY {
        /**
         * Validate if the statement priority is between [1-9].
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validatePriority(parsedQuery.getStatement());
        }
    },
    VALIDATE_SCOPE {
        /**
         *
         * Checks if the columns used in the query are within the scope of the table affected by the statement.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateScope(parsedQuery.getStatement());
        }
    },

    MUST_BE_CONNECTED {

        /**
         *
         * Validates if the Connector is "Connected".
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateConnectorConnected(parsedQuery.getStatement());
        }
    },
    MUST_BE_UNIQUE_DATASTORE {

        /**
         * Validate if the DATASTORE is unique.
         *
         * @param parsedQuery the IParsedQuery to validate
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validatePreviousAttachment(parsedQuery.getStatement());
        }
    },

    MUST_NOT_EXIST_FULL_TEXT_INDEX {
        /**
         * Validates if the Full Text Index Does not Exist.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validateNotExistLuceneIndex(parsedQuery.getStatement());
        }
    }, PAGINATION_SUPPORT {

        /**
         *
         * Validate if the connector support pagination.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validatePaginationSupport(parsedQuery.getStatement());
        }
    },
    MUST_BE_A_VALID_PK {

        /**
         *
         * Validates if the Create Table Statement has a valid PK.
         *
         * @param parsedQuery the IParsedQuery to validate.
         * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
         * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
         *
         * @throws IgnoreQueryException
         * @throws ValidationException
         */
        @Override
        public void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException {
            validatorHelper.validatePKColumn(parsedQuery.getStatement());
        }
    };

    /**
     *
     *  Validate a parsed query with the ValidationTypes enum criterial.
     *
     * @param parsedQuery the IParsedQuery to validate.
     * @param parentsTableNames a java.util.Set of TableName, used to validate selects.
     * @param validatorHelper a instance of ValidatorHelper that encapsulate the logic.
     *
     * @throws IgnoreQueryException
     * @throws ValidationException
     */
    public abstract void validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames, ValidatorHelper validatorHelper) throws IgnoreQueryException, ValidationException;
}
