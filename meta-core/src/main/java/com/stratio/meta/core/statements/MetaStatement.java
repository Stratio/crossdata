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

package com.stratio.meta.core.statements;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.metadata.structures.TableType;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.Tree;
import com.stratio.streaming.api.IStratioStreamingAPI;

/**
 * Class that models a generic Statement supported by the META language.
 */
public abstract class MetaStatement {

  /**
   * Whether the query is an internal command or it returns a
   * {@link com.stratio.meta.common.data.ResultSet}.
   */
  protected boolean command;

  /**
   * Whether the keyspace has been specified in the statement or it should be taken from the
   * environment.
   */
  private boolean keyspaceInc = false;

  /**
   * Keyspace specified from the statement.
   */
  private String keyspace = null;

  /**
   * The current keyspace in the user session.
   */
  private String sessionKeyspace = null;

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
   * @param effectiveKeyspace The target keyspace where the query will be executed.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  protected Result validateKeyspaceAndTable(MetadataManager metadata, String effectiveKeyspace,
      String tableName) {

    Result result = QueryResult.createSuccessQueryResult();
    // Get the effective keyspace based on the user specification during the create
    // sentence, or taking the keyspace in use in the user session.

    // Check that the keyspace and table exists.
    if (effectiveKeyspace == null || effectiveKeyspace.length() == 0) {
      result =
          Result
              .createValidationErrorResult("Target keyspace missing or no keyspace has been selected.");
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
          result =
              Result.createValidationErrorResult("Table '" + tableName + "' does not exist in "
                  + effectiveKeyspace + ".");
        } else {
          if (tableMetadata.getType() == TableType.EPHEMERAL) {
            result = CommandResult.createCommandResult("streaming");
          }
        }
      }
    }

    return result;
  }

  public String getEffectiveKeyspace() {
    return keyspaceInc ? keyspace : sessionKeyspace;
  }

  public void setKeyspace(String keyspace) {
    this.keyspace = keyspace;
    this.keyspaceInc = true;
  }

  public String getKeyspace() {
    return keyspace;
  }

  public boolean isKeyspaceIncluded() {
    return this.keyspaceInc;
  }

  /**
   * Translate the statement into the CQL equivalent when possible.
   * 
   * @return The CQL equivalent.
   */
  public abstract String translateToCQL(MetadataManager metadataManager);

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
   * Get a tree that contains the planning for executing the query. The plan will be executed
   * starting from the leaves and finishing at the tree root.
   * 
   * @param metadataManager The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
   *        the required information.
   * @param targetKeyspace The target keyspace where the query will be executed.
   * @return A {@link com.stratio.meta.core.utils.Tree} with the execution plan.
   */
  public abstract Tree getPlan(MetadataManager metadataManager, String targetKeyspace);

  public void setSessionKeyspace(String targetKeyspace) {
    sessionKeyspace = targetKeyspace;
  }

}
