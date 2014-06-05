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

import java.util.List;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.DescribeType;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code DESCRIBE} statement from the META language.
 */
public class DescribeStatement extends MetaStatement {

  /**
   * Type of description required: {@code KEYSPACE} or {@code TABLE}.
   */
  private DescribeType type;

  /**
   * The target table.
   */
  private String tableName;

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

  /**
   * Get the keyspace to be described.
   * 
   * @return The name or null if not set.
   */
  public String getKeyspace() {
    return keyspace;
  }

  /**
   * Set the keyspace to be described.
   * 
   * @param keyspace The name.
   */
  public void setKeyspace(String keyspace) {
    this.keyspace = keyspace;
    keyspaceInc = true;
  }

  /**
   * Get the table to be described.
   * 
   * @return The name or null if not set.
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * Set the name of the table to be described.
   * 
   * @param tableName The name.
   */
  public void setTableName(String tableName) {
    if (tableName.contains(".")) {
      String[] ksAndTableName = tableName.split("\\.");
      keyspaceInc = true;
      keyspace = ksAndTableName[0];
      this.tableName = ksAndTableName[1];
    } else {
      this.tableName = tableName;
    }
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder("DESCRIBE ");
    sb.append(type.name());

    if (type == DescribeType.KEYSPACE && keyspace != null) {
      sb.append(" ").append(keyspace);
    } else if (type == DescribeType.TABLE) {
      sb.append(" ").append(getEffectiveKeyspace()).append(".");
      sb.append(tableName);
    }

    return sb.toString();
  }

  /** {@inheritDoc} */
  @Override
  public Result validate(MetadataManager metadata) {

    Result result = QueryResult.createSuccessQueryResult();

    if (this.keyspace != null) {
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(this.keyspace);
      if (ksMetadata == null) {
        result =
            QueryResult.createFailQueryResult("Keyspace " + this.keyspace + " does not exist.");
      }
    }

    if (this.tableName != null) {
      result =
          validateKeyspaceAndTable(metadata, sessionKeyspace, keyspaceInc, keyspace, tableName);
    }

    return result;
  }

  @Override
  public String translateToCQL() {
    return this.toString();
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
  public Result execute(Session session) {
    MetadataManager mm = new MetadataManager(session);
    mm.loadMetadata();
    Result result = null;
    if (type == DescribeType.KEYSPACE) {
      KeyspaceMetadata ksInfo = mm.getKeyspaceMetadata(this.getEffectiveKeyspace());
      if (ksInfo == null) {
        result = CommandResult.createFailCommandResult("KEYSPACE " + keyspace + " was not found");
      } else {
        result = CommandResult.createSuccessCommandResult(ksInfo.exportAsString());
      }
    } else if (type == DescribeType.KEYSPACES) {
      List<String> keyspacesNames = mm.getKeyspacesNames();
      if (keyspacesNames == null) {
        result = CommandResult.createFailCommandResult("No keyspaces found");
      } else {
        result = CommandResult.createSuccessCommandResult(keyspacesNames.toString());
      }
    } else if (type == DescribeType.TABLE) {
      TableMetadata tableInfo = mm.getTableMetadata(this.getEffectiveKeyspace(), tableName);
      if (tableInfo == null) {
        result = CommandResult.createFailCommandResult("TABLE " + tableName + " was not found");
      } else {
        result = CommandResult.createSuccessCommandResult(tableInfo.exportAsString());
      }
    } else {
      List<String> tablesNames = mm.getTablesNames(this.getEffectiveKeyspace());
      if (tablesNames == null) {
        result = CommandResult.createFailCommandResult("No tables found");
      } else {
        result = CommandResult.createSuccessCommandResult(tablesNames.toString());
      }
    }
    return result;
  }
}
