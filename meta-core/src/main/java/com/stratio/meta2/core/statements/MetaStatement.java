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

import java.util.List;

import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.core.validator.ValidationRequirements;

/**
 * Class that models a generic Statement supported by the META language.
 */
public abstract class MetaStatement implements IStatement {

    /**
     * Whether the query is an internal command or it returns a
     * {@link com.stratio.meta.common.data.ResultSet}.
     */
    protected boolean command;

    /**
     * Whether the catalog has been specified in the statement or it should be taken from the
     * environment.
     */
    protected boolean catalogInc = false;

    /**
     * CATALOG specified in the user provided statement.
     */
    protected CatalogName catalog = null;

    /**
     * The current catalog in the user session.
     */
    protected CatalogName sessionCatalog = null;

    /**
     * The type of statement to be executed.
     */
    protected StatementType type = null;

    /**
     * Default class constructor.
     */

    public MetaStatement() {
    }

    /**
     * Class constructor.
     *
     * @param command Whether the query is a command or a query returning a
     *                {@link com.stratio.meta.common.data.ResultSet}.
     */
    public MetaStatement(boolean command) {
        this.command = command;
    }

    /**
     * Whether the query is an internal command or not.
     *
     * @return The boolean value.
     */
    public boolean isCommand() {
        return command;
    }

    /**
     * Set whether the query is a command or not.
     *
     * @param command The boolean value.
     */
    public void setAsCommand(boolean command) {
        this.command = command;
    }

    @Override
    public abstract String toString();

    /**
     * Get the effective catalog to execute the statement.
     *
     * @return The catalog specified in the statement or the session catalog otherwise.
     */
    public CatalogName getEffectiveCatalog() {
        return catalogInc ? catalog : sessionCatalog;
    }

    /**
     * Set the catalog to be described.
     *
     * @param catalog The name.
     */
    public void setCatalog(CatalogName catalog) {
        this.catalog = catalog;
        catalogInc = true;
    }

    /**
     * Set the session catalog.
     *
     * @param targetCatalog The target catalog for executing the statement.
     */
    public void setSessionCatalog(CatalogName targetCatalog) {
        sessionCatalog = targetCatalog;
    }

    public List<TableName> getFromTables() {
        return null;
    }

    //TODO: This method should be abstract
    public abstract ValidationRequirements getValidationRequirements();

    /**
     * Get the list of columns involved in the current statement.
     *
     * @return The list or null if no columns are available.
     */
    public List<ColumnName> getColumns() {
        return null;
    }

}

