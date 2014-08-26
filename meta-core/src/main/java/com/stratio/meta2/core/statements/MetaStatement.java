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

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.common.statements.structures.assignations.Assignation;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.common.statements.structures.ColumnName;
import com.stratio.meta.common.statements.structures.TableName;
import com.stratio.meta.core.utils.Tree;
import com.stratio.meta2.core.engine.validator.ValidationRequirements;
import com.stratio.meta2.metadata.CatalogName;
import com.stratio.streaming.api.IStratioStreamingAPI;


import java.util.List;

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
   * Catalog specified in the user provided statement.
   */
  protected String catalog = null;

  /**
   * The current catalog in the user session.
   */
  protected String sessionCatalog = null;

  /**
   * The type of statement to be executed.
   */
  protected StatementType type = null;

  /**
   * Default class constructor.
   */

  public MetaStatement() {}

  /**
   * Class constructor.
   * 
   * @param command Whether the query is a command or a query returning a
   *        {@link com.stratio.meta.common.data.ResultSet}.
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
   * Validate the semantics of the current statement. This method checks the existing metadata to
   * determine that all referenced entities exists in the {@code targetKeyspace} and the types are
   * compatible with the assignations or comparisons.
   * 
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides the
   *        required information.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  public Result validate(MetadataManager metadata, EngineConfig config) {
    return Result.createValidationErrorResult("Statement not supported");
  }

  /**
   * Validate that a valid keyspace and table is present.
   * 
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides the
   *        required information.
   * @param targetKeyspace The target keyspace where the query will be executed.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  protected Result validateKeyspaceAndTable(MetadataManager metadata, String targetKeyspace,
      boolean keyspaceInc, String stmtKeyspace, String tableName) {
    Result result = QueryResult.createSuccessQueryResult();
    // Get the effective keyspace based on the user specification during the create
    // sentence, or taking the keyspace in use in the user session.
    String effectiveKeyspace = targetKeyspace;
    if (keyspaceInc) {
      effectiveKeyspace = stmtKeyspace;
    }

    // Check that the keyspace and table exists.
    if (effectiveKeyspace == null || effectiveKeyspace.length() == 0) {
      result =
          Result
              .createValidationErrorResult("Target catalog missing or no catalog has been selected.");
    } else {
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
      if (ksMetadata == null) {
        result =
            Result
                .createValidationErrorResult("Keyspace " + effectiveKeyspace + " does not exist.");
      } else {
        com.stratio.meta.common.metadata.structures.TableMetadata tableMetadata =
            metadata.getTableGenericMetadata(effectiveKeyspace, tableName);
        if (tableMetadata == null) {
          if (!metadata.checkStream(effectiveKeyspace + "_" + tableName)) {
            result =
                Result.createValidationErrorResult("Table " + tableName + " does not exist in "
                    + effectiveKeyspace + ".");
          } else {
            result = CommandResult.createCommandResult("streaming");
          }
        }
      }
    }
    return result;
  }

  /**
   * Get the effective catalog to execute the statement.
   * @return The catalog specified in the statement or the session catalog otherwise.
   */
  public String getEffectiveCatalog() {
    return catalogInc ? catalog : sessionCatalog;
  }

  /**
   * Translate the statement into the CQL equivalent when possible.
   * 
   * @return The CQL equivalent.
   */
  public abstract String translateToCQL();

  public String translateToSiddhi(IStratioStreamingAPI stratioStreamingAPI, String streamName,
      String outgoing) {
    return null;
  }

  /**
   * Get the {@link Statement} equivalent of the current query.
   * 
   * @return The Statement or null if the driver translation cannot be done.
   */
  public Statement getDriverStatement() {
    return null;
  }

  /**
   * Set the catalog to be described.
   *
   * @param catalog The name.
   */
  public void setCatalog(String catalog) {
    this.catalog = catalog;
    catalogInc = true;
  }

  /**
   * Set the session catalog.
   * @param targetCatalog The target catalog for executing the statement.
   */
  public void setSessionCatalog(String targetCatalog) {
    sessionCatalog = targetCatalog;
  }

  @Deprecated
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace){
    return null;
  }


  //IStatement Methods
  public List<CatalogName> getCatalogs(){
    throw new UnsupportedOperationException();
  }


  public List<TableName> getTables(){
    throw new UnsupportedOperationException();
  }


  public List<ColumnName> getColumns(){
    throw new UnsupportedOperationException();
  }


  public List<Assignation> getAssignations(){
    throw new UnsupportedOperationException();
  }


  public boolean getIfExists(){
    throw new UnsupportedOperationException();
  }

  public boolean getIfNotExists(){
    throw new UnsupportedOperationException();
  }

  public ValidationRequirements getValidationRequirements(){
    throw new UnsupportedOperationException();
  }
}

