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

import java.util.Map;

import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

/**
 * Class that models a {@code CREATE CATALOG} statement from the META language. CATALOG
 * information will be stored internally as part of the existing metadata. CATALOG creation
 * in the underlying datastore is done when a table is created in a catalog.
 */
public class CreateCatalogStatement extends MetadataStatement {

    /**
     * Whether the CATALOG should be created only if it not exists.
     */
    private final boolean ifNotExists;

    /**
     * A JSON with the options specified by the user.
     */
    private final Map<Selector, Selector> options;

    /**
     * Class constructor.
     *
     * @param catalogName The name of the catalog.
     * @param ifNotExists Whether it should be created only if it not exists.
     * @param options     A JSON with the storage options.
     */
    public CreateCatalogStatement(CatalogName catalogName, boolean ifNotExists,
            String options) {
        this.catalog = catalogName;
        this.catalogInc = true;
        this.command = false;
        this.ifNotExists = ifNotExists;
        this.options = StringUtils.convertJsonToOptions(options);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CREATE CATALOG ");
        if (ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }
        sb.append(catalog);
        if ((options != null) && (!options.isEmpty())) {
            sb.append(" WITH ").append(options);
        }
        return sb.toString();
    }

    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(Validation.MUST_NOT_EXIST_CATALOG);
    }

    public CatalogName getCatalogName() {
        return catalog;
    }

    public Map<Selector, Selector> getOptions() {
        return options;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }
}

