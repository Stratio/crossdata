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

import com.stratio.crossdata.common.data.*;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.exceptions.validation.*;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.metadata.*;
import com.stratio.crossdata.common.statements.structures.*;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.normalizer.Normalizator;
import com.stratio.crossdata.core.normalizer.NormalizedFields;
import com.stratio.crossdata.core.query.*;
import com.stratio.crossdata.core.statements.*;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Validator Class.
 */
public class Validator {
    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Validator.class);


    /**
     * validate a parsed query.
     * @param parsedQuery The parsed query
     * @return com.stratio.crossdata.core.query.IValidatedQuery;
     * @throws ValidationException
     * @throws IgnoreQueryException
     */
    public IValidatedQuery validate(IParsedQuery parsedQuery) throws ValidationException, IgnoreQueryException {
        return validate(parsedQuery, new HashSet<TableName>());
    }

    /**
     * validate a parsed query.
     *
     * @param parsedQuery The parsed query
     * @return com.stratio.crossdata.core.query.IValidatedQuery;
     * @throws ValidationException
     * @throws IgnoreQueryException
     */
    public IValidatedQuery validate(IParsedQuery parsedQuery, Set<TableName> parentsTableNames)
            throws ValidationException, IgnoreQueryException {

        ValidatorHelper validatorHelper = new ValidatorHelper(this);

        IValidatedQuery validatedQuery = null;
        LOG.info("Validating CrossdataStatements...");
        for (ValidationTypes val : parsedQuery.getStatement().getValidationRequirements().getValidations()) {
            val.validate(parsedQuery, parentsTableNames, validatorHelper);
        }

        if (parsedQuery instanceof MetadataParsedQuery) {
            validatedQuery = new MetadataValidatedQuery((MetadataParsedQuery) parsedQuery);
        } else if (parsedQuery instanceof StorageParsedQuery) {
            validatedQuery = new StorageValidatedQuery((StorageParsedQuery) parsedQuery);
            ((StorageValidatedQuery) validatedQuery).setSqlQuery(parsedQuery.getStatement().toString());
        } else if (parsedQuery instanceof SelectParsedQuery) {

            validatedQuery = createValidatedQuery(validatorHelper.getNormalizator(), ((SelectParsedQuery) parsedQuery));
        }
        return validatedQuery;
    }

    private SelectValidatedQuery createValidatedQuery(Normalizator normalizer,SelectParsedQuery selectParsedQuery) {

        SelectValidatedQuery partialValidatedQuery = new SelectValidatedQuery(selectParsedQuery);
        NormalizedFields fields = normalizer.getFields();

        //Prepare nextQuery
        if(selectParsedQuery.getChildParsedQuery() != null) {
            partialValidatedQuery.setSubqueryValidatedQuery(createValidatedQuery(normalizer.getSubqueryNormalizator(),selectParsedQuery.getChildParsedQuery()));
        }

        partialValidatedQuery.setTableMetadata(fields.getTablesMetadata());
        partialValidatedQuery.getColumns().addAll(fields.getColumnNames());
        partialValidatedQuery.getTables().addAll(fields.getTableNames());
        partialValidatedQuery.getRelations().addAll(fields.getWhere());
        partialValidatedQuery.setJoinList(fields.getJoinList());

        return  partialValidatedQuery;
    }

}
