/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.statements;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.DescribeType;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;
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
      //sb.append(" ").append(getEffectiveCatalog()).append(".");
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

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree steps = new Tree();
    steps.setNode(new MetaStep(MetaPath.COMMAND, this));
    return steps;
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
  public List<String> getTables() {
    List<String> result = new ArrayList<>();
    if(DescribeType.TABLE.equals(type)){
      result.add(tableName.getTableName());
    }
    return result;
  }
}
