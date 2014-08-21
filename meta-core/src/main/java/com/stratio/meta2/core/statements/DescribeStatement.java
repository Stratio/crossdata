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

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.common.statements.structures.TableName;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.DescribeType;
import com.stratio.streaming.api.IStratioStreamingAPI;

/**
 * Class that models a {@code DESCRIBE} statement from the META language.
 */
public class DescribeStatement extends TableStatement {

  /**
   * Type of description required: {@code CATALOG} or {@code TABLE}.
   */
  private DescribeType type;

  /**
   * Class constructor.
   * 
   * @param type Type of element to be described.
   */
  public DescribeStatement(DescribeType type) {
    this.type = type;
    this.command = true;
  }

  /**
   * Get the type of element to be described.
   * 
   * @return A {@link com.stratio.meta.core.structures.DescribeType}.
   */
  public DescribeType getType() {
    return type;
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder("DESCRIBE ");
    sb.append(type.name());

    if (type == DescribeType.CATALOG && catalog != null) {
      sb.append(" ").append(catalog);
    } else if (type == DescribeType.TABLE) {
      sb.append(" ").append(tableName);
    }

    return sb.toString();
  }

  /** {@inheritDoc} */
  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {

    Result result = QueryResult.createSuccessQueryResult();

    if (this.catalog != null) {
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(this.catalog);
      if (ksMetadata == null) {
        result =
            Result.createValidationErrorResult("Keyspace " + this.catalog + " does not exist.");
      }
    }

    if (this.tableName != null) {
      result =
          validateKeyspaceAndTable(metadata, sessionCatalog, tableName.containsCatalog(), tableName.getCatalog(), tableName.getTableName());
    }

    return result;
  }

  @Override
  public String translateToCQL() {
    return this.toString().replace("CATALOG", "KEYSPACE");
  }

  /**
   * Execute the describe command.
   * 
   * @param session The {@link com.datastax.driver.core.Session} used to retrieve the medatada.
   * @return A {@link com.stratio.meta.common.result.Result}.
   */
  public Result execute(Session session, IStratioStreamingAPI stratioStreamingAPI) {
    MetadataManager mm = new MetadataManager(session, stratioStreamingAPI);
    mm.loadMetadata();
    Result result = null;
    if (type == DescribeType.CATALOG) {

      KeyspaceMetadata ksInfo = mm.getKeyspaceMetadata(super.getEffectiveCatalog());
      if (ksInfo == null) {
        result = Result.createExecutionErrorResult("KEYSPACE " + catalog + " was not found");
      } else {
        result = CommandResult.createCommandResult(ksInfo.exportAsString());
      }
    } else if (type == DescribeType.CATALOGS) {
      List<String> keyspacesNames = mm.getKeyspacesNames();
      if (keyspacesNames == null) {
        result = Result.createExecutionErrorResult("No keyspaces found");
      } else {
        result = CommandResult.createCommandResult(keyspacesNames.toString());
      }
    } else if (type == DescribeType.TABLE) {
      com.stratio.meta.common.metadata.structures.TableMetadata tableInfo =
          mm.getTableGenericMetadata(getEffectiveCatalog(), tableName.getTableName());
      if (tableInfo == null) {
        result = Result.createExecutionErrorResult("TABLE " + tableName + " was not found");
      } else {
        result = CommandResult.createCommandResult(tableInfo.exportAsString());
      }
    } else {
      List<String> tablesNames = mm.getTablesNames(this.getEffectiveCatalog());
      if (tablesNames == null) {
        result = Result.createExecutionErrorResult("No tables found");
      } else {
        result = CommandResult.createCommandResult(tablesNames.toString());
      }
    }
    return result;
  }

  @Override
  public List<String> getCatalogs() {
    List<String> result = new ArrayList<>();
    if(DescribeType.CATALOG.equals(type)){
      result.add(getEffectiveCatalog());
    }
    return result;
  }

  @Override
  public List<TableName> getTables() {
    List<TableName> result = new ArrayList<>();
    if(DescribeType.TABLE.equals(type)){
      result.add(tableName);
    }
    return result;
  }
}
