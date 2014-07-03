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

import java.util.Iterator;
import java.util.List;

import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.Property;
import com.stratio.meta.core.structures.PropertyNameValue;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.CoreUtils;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models an {@code ALTER TABLE} statement from the META language.
 */
public class AlterTableStatement extends MetaStatement {

  /**
   * The name of the target table.
   */
  private String tableName;

  /**
   * Type of alter. Accepted values are:
   * <ul>
   * <li>1: Alter a column data type using {@code ALTER}.</li>
   * <li>2: Add a new column using {@code ADD}.</li>
   * <li>3: Drop a column using {@code DROP}.</li>
   * <li>4: Establish a set of options using {@code WITH}.</li>
   * </ul>
   */
  private int option;

  /**
   * Target column name.
   */
  private String column;

  /**
   * Target column datatype used with {@code ALTER} or {@code ADD}.
   */
  private String type;

  /**
   * The list of {@link com.stratio.meta.core.structures.Property} of the table.
   */
  private List<Property> properties = null;

  /**
   * Class constructor.
   * 
   * @param tableName The name of the table.
   * @param column The name of the column.
   * @param type The data type of the column.
   * @param properties The type of modification.
   * @param option The map of options.
   */
  public AlterTableStatement(String tableName, String column, String type,
      List<Property> properties, int option) {
    this.command = false;
    if (tableName.contains(".")) {
      String[] ksAndTableName = tableName.split("\\.");
      this.setKeyspace(ksAndTableName[0]);
      this.tableName = ksAndTableName[1];
    } else {
      this.tableName = tableName;
    }
    this.column = column;
    this.type = type;
    this.properties = properties;
    this.option = option;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Alter table ");
    if (this.isKeyspaceIncluded()) {
      sb.append(this.getEffectiveKeyspace()).append(".");
    }
    sb.append(tableName);
    switch (option) {
      case 1:
        sb.append(" alter ").append(column);
        sb.append(" type ").append(type);
        break;
      case 2:
        sb.append(" add ");
        sb.append(column).append(" ");
        sb.append(type);
        break;
      case 3:
        sb.append(" drop ");
        sb.append(column);
        break;
      case 4:
        sb.append(" WITH ").append(ParserUtils.stringList(properties, " AND "));
        break;
      default:
        sb.append("bad option");
        break;
    }
    return sb.toString();
  }

  @Override
  public String translateToCQL(MetadataManager metadataManager) {
    return this.toString();
  }

  /**
   * Validate the semantics of the current statement. This method checks the existing metadata to
   * determine that all referenced entities exists in the {@code targetKeyspace} and the types are
   * compatible with the assignations or comparisons.
   * 
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides the
   *        required information.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result = validateKeyspaceAndTable(metadata, this.getEffectiveKeyspace(), tableName);
    if (!result.hasError()) {
      String effectiveKeyspace = getEffectiveKeyspace();

      TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tableName);

      switch (option) {
        case 1:
          result = validateAlter(tableMetadata);
          break;
        case 2:
          result = validateAdd(tableMetadata);
          break;
        case 3:
          result = validateDrop(tableMetadata);
          break;
        case 4:
          result = validateProperties(tableMetadata);
          break;
        default:
      }
    }
    return result;
  }

  private boolean existsColumn(TableMetadata tableMetadata) {
    return tableMetadata.getColumn(column) != null;
  }

  private boolean existsType(TableMetadata tableMetadata) {
    return CoreUtils.supportedTypes.contains(type.toLowerCase());
  }

  private Result validateAlter(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    // Validate target column name
    if (!existsColumn(tableMetadata)) {
      result = Result.createValidationErrorResult("Column '" + this.column + "' not found.");
    } else if (!existsType(tableMetadata)) { // Validate type
      result = Result.createValidationErrorResult("Type '" + this.type + "' not found.");
    }
    // TODO: validate that conversion is compatible as for current type and target type
    return result;
  }

  private Result validateAdd(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    // Validate target column name
    if (existsColumn(tableMetadata)) {
      result = Result.createValidationErrorResult("Column '" + this.column + "' already exists.");
    } else if (!existsType(tableMetadata)) { // Validate type
      result = Result.createValidationErrorResult("Type '" + this.type + "' not found.");
    }
    return result;
  }

  private Result validateDrop(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    // Validate target column name
    if (!existsColumn(tableMetadata)) {
      result = Result.createValidationErrorResult("Column '" + this.column + "' not found.");
    }
    return result;
  }

  private Result validateProperties(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    Iterator<Property> props = properties.iterator();
    boolean exit = false;
    while (!exit && props.hasNext()) {
      Property property = props.next();
      if (property.getType() == Property.TYPE_NAME_VALUE) {
        PropertyNameValue propertyNameValue = (PropertyNameValue) property;
        if ("ephemeral".equalsIgnoreCase(propertyNameValue.getName())
            && propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN) {
          // If property ephemeral is present, it must be a boolean type
          result = Result.createValidationErrorResult("Property 'ephemeral' must be a boolean");
          exit = true;
        } else if ("ephemeral_tuples".equalsIgnoreCase(propertyNameValue.getName())
            && propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN) {
          // If property ephemeral_tuples is present, it must be a integer type
          result = Result.createValidationErrorResult("Property 'ephemeral' must be a boolean");
          exit = true;
        } else if ("ephemeral_persist_on".equalsIgnoreCase(propertyNameValue.getName())
            && propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN) {
          // If property ephemeral_persist_on is present, it must be a string type
          result =
              Result
                  .createValidationErrorResult("Property 'ephemeral_persist_on' must be a string");
          exit = true;
        }
      }
    }
    return result;
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
