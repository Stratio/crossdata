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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.utils.StringUtils;

/**
 * Class that models an {@code UPDATE} statement from the CROSSDATA language.
 */
public class UpdateTableStatement extends StorageStatement implements ITableStatement {

    /**
     * The list of assignations.
     */
    private List<Relation> assignations;

    /**
     * The list of relations.
     */
    private List<Relation> whereClauses;

    /**
     * The list of properties.
     */
    private Map<Selector, Selector> properties = new LinkedHashMap<>();

    /**
     * Class constructor.
     *
     * @param tableName    The name of the table.
     * @param assignations The list of assignations.
     * @param whereClauses The list of relations.
     * @param properties   The list of properties.
     */
    public UpdateTableStatement(TableName tableName,
            List<Relation> assignations, List<Relation> whereClauses,
            String properties) throws ParsingException {
        this.command = false;

        if (tableName.getName().isEmpty()) {
            throw new ParsingException("Table name cannot be empty");
        }
        tableStatement.setTableName(tableName);

        if (assignations == null) {
            this.assignations = new ArrayList<>();
        } else {
            this.assignations = assignations;
        }

        if (whereClauses == null) {
            this.whereClauses = new ArrayList<>();
        } else {
            this.whereClauses = whereClauses;
        }

        if (properties == null) {
            this.properties = new LinkedHashMap<>();
        } else {
            this.properties = StringUtils.convertJsonToOptions(tableName, properties);
        }
    }

    public List<Relation> getAssignations() {
        return assignations;
    }

    public List<Relation> getWhereClauses() {
        return whereClauses;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tableStatement.getTableName().getQualifiedName());
        sb.append(" ").append("SET ");
        sb.append(StringUtils.stringList(assignations, ", "));
        if ((whereClauses != null) && (!whereClauses.isEmpty())) {
            sb.append(" ").append("WHERE ");
            sb.append(StringUtils.stringList(whereClauses, " AND "));
        }
        if (hasProperties()) {
            sb.append(" WITH ").append(properties);
        }
        return sb.toString();
    }

    private boolean hasProperties() {
        return ((properties != null) && (!properties.isEmpty()));
    }



}
