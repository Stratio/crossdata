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

import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

/**
 * Class that models a {@code DROP INDEX} statement from the META language.
 */
public class DropIndexStatement extends IndexStatement {

    /**
     * Whether the index should be dropped only if exists.
     */
    private boolean dropIfExists = false;

    /**
     * The name of the index.
     */
    private IndexName name = null;

    /**
     * Class constructor.
     */
    public DropIndexStatement() {
        this.command = false;
    }

    /**
     * Set the option to drop the index only if exists.
     */
    public void setDropIfExists() {
        dropIfExists = true;
    }

    public void setName(ColumnName name){
        this.name = new IndexName(name.getTableName(), name.getName());
    }

    public boolean isDropIfExists() {
        return dropIfExists;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DROP INDEX ");
        if (dropIfExists) {
            sb.append("IF EXISTS ");
        }
        if (catalogInc) {
            sb.append(catalog).append(".");
        }
        sb.append(name);
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(Validation.MUST_EXIST_INDEX);
    }

}
