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
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

/**
 * Storage Statement Class.
 */
public abstract class StorageStatement extends CrossdataStatement implements ITableStatement{

    /**
     * A table statement that affect the storage operation.
     */
    protected TableStatement tableStatement=new TableStatement();
    
    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(ValidationTypes.MUST_EXIST_CATALOG)
                        .add(ValidationTypes.MUST_EXIST_TABLE).add(ValidationTypes.MUST_EXIST_COLUMN)
                        .add(ValidationTypes.VALIDATE_SCOPE);
    }

    /**
     * Get the table name of the storage statement.
     * @return A {@link com.stratio.crossdata.common.data.TableName} .
     */
    public TableName getTableName() {
        return tableStatement.getTableName();
    }

    /**
     * Set the table name for the storage statement.
     * @param tablename The table name.
     */
    public void setTableName(TableName tablename) {
        tableStatement.setTableName(tablename);
    }

    @Override
    public CatalogName getEffectiveCatalog() {
        return tableStatement.getEffectiveCatalog();
    }


}
