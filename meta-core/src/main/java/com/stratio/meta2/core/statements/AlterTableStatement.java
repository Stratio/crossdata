/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.core.statements;

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.CoreUtils;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.engine.EngineConfig;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

import java.util.Map;

/**
 * Class that models an {@code ALTER TABLE} statement from the META language.
 */
public class AlterTableStatement extends MetadataStatement implements ITableStatement {

  /**
   * The target table.
   */
  private TableName tableName;

  /**
   * Type of alter. Accepted values are:
   * <ul>
   *     <li>1: Alter a column data type using {@code ALTER}.</li>
   *     <li>2: Add a new column using {@code ADD}.</li>
   *     <li>3: Drop a column using {@code DROP}.</li>
   *     <li>4: Establish a set of options using {@code WITH}.</li>
   * </ul>
   */
  private int option;

  /**
   * Target column name.
   */
  private ColumnName column;

  /**
   * Target column datatype used with {@code ALTER} or {@code ADD}.
   */
  private ColumnType type;

  /**
   * The list of {@link com.stratio.meta2.core.structures.Property} of the table.
   */
  private Map<Selector, Selector> properties = null;

  /**
   * Class constructor.
   *
   * @param tableName The name of the table.
   * @param column The name of the column.
   * @param type The data type of the column.
   * @param properties The type of modification.
   * @param option The map of options.
   */
  public AlterTableStatement(TableName tableName, ColumnName column, ColumnType type,
                             String properties, int option) {
    this.command = false;
    this.tableName = tableName;
    this.column = column;
    this.type = type;
    this.properties = StringUtils.convertJsonToOptions(properties);
    this.option = option;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ALTER TABLE ");
    sb.append(tableName.getQualifiedName());
    switch(option){
      case 1:
        sb.append(" ALTER ").append(column.getQualifiedName());
        sb.append(" TYPE ").append(type);
        break;
      case 2:
        sb.append(" ADD ");
        sb.append(column.getQualifiedName()).append(" ");
        sb.append(type);
        break;
      case 3:
        sb.append(" DROP ");
        sb.append(column.getQualifiedName());
        break;
      case 4:
        sb.append(" WITH ").append(properties);
        break;
      default:
        sb.append("BAD OPTION");
        break;
    }

    return sb.toString();
  }

  /**
   * Validate the semantics of the current statement. This method checks the
   * existing metadata to determine that all referenced entities exists in the
   * {@code targetCatalog} and the types are compatible with the assignations
   * or comparisons.
   *
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
   *                 the required information.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result = validateCatalogAndTable(
        metadata,
        sessionCatalog,
        tableName.isCompletedName(), tableName.getCatalogName(), tableName);
    if(!result.hasError()) {
      CatalogName effectiveCatalog = getEffectiveCatalog();
      System.out.println("validating: " + effectiveCatalog + " table: " + tableName.getName());
      /*TableMetadata tableMetadata = metadata.getTableMetadata(effectiveCatalog, tableName);
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
      }*/
    }
    return result;
  }

  private boolean existsColumn(TableMetadata tableMetadata){
    return tableMetadata.getColumns().get(column) != null;
  }


  private boolean existsType(TableMetadata tableMetadata) {
    return CoreUtils.supportedTypes.contains(type.toString().toLowerCase());
  }

  private Result validateAlter(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    //Validate target column name
    if(!existsColumn(tableMetadata)){
      result = Result.createValidationErrorResult("Column '"+this.column+"' not found.");
    } else if(!existsType(tableMetadata)){ //Validate type
      result = Result.createValidationErrorResult("Type '"+this.type+"' not found.");
    }
    // TODO: validate that conversion is compatible as for current type and target type
    return result;
  }

  private Result validateAdd(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    //Validate target column name
    if(existsColumn(tableMetadata)){
      result = Result.createValidationErrorResult("Column '"+this.column+"' already exists.");
    } else if(!existsType(tableMetadata)){ //Validate type
      result = Result.createValidationErrorResult("Type '"+this.type+"' not found.");
    }
    return result;
  }

  private Result validateDrop(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    // Validate target column name
    if (existsColumn(tableMetadata)) {
      result = Result.createValidationErrorResult("Column '" + this.column + "' already exists.");
    } else if (!existsType(tableMetadata)) { // Validate type
      result = Result.createValidationErrorResult("Type '" + this.type + "' not found.");
    }
    return result;
  }

  private Result validateProperties(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    /*Iterator<Property> props = properties.iterator();
    boolean exit = false;
    while(!exit && props.hasNext()){
      Property property = props.next();
      if(property.getType() == Property.TYPE_NAME_VALUE){
        PropertyNameValue propertyNameValue = (PropertyNameValue) property;
        if("ephemeral".equalsIgnoreCase(propertyNameValue.getName())
           && propertyNameValue.getVp().getTermClass() != Boolean.class){
          // If property ephemeral is present, it must be a boolean type
          result = Result.createValidationErrorResult("Property 'ephemeral' must be a boolean");
          exit = true;
        } else if("ephemeral_tuples".equalsIgnoreCase(propertyNameValue.getName())
                  && propertyNameValue.getVp().getTermClass() != Boolean.class){
          // If property ephemeral_tuples is present, it must be a integer type
          result= Result.createValidationErrorResult("Property 'ephemeral' must be a boolean");
          exit = true;
        } else if("ephemeral_persist_on".equalsIgnoreCase(propertyNameValue.getName())
                  && propertyNameValue.getVp().getTermClass() != Boolean.class){
          // If property ephemeral_persist_on is present, it must be a string type
          result= Result.createValidationErrorResult(
              "Property 'ephemeral_persist_on' must be a string");
          exit = true;
        }
      }
    }*/
    return result;
  }

  @Override
  public ValidationRequirements getValidationRequirements() {
    ValidationRequirements validationRequirements;
    switch (option) {
        case 1:
            validationRequirements=new ValidationRequirements().add(Validation.MUST_EXIST_TABLE).add(Validation.MUST_EXIST_COLUMN);
            break;
        case 2:
            validationRequirements=new ValidationRequirements().add(Validation.MUST_EXIST_TABLE).add(Validation.MUST_NOT_EXIST_COLUMN);
            break;
        case 3:
            validationRequirements=new ValidationRequirements().add(Validation.MUST_EXIST_TABLE).add(Validation.MUST_EXIST_COLUMN);
            break;
        case 4:
            validationRequirements=new ValidationRequirements().add(Validation.MUST_EXIST_TABLE).add(Validation.MUST_EXIST_PROPERTIES);
            break;
        default:
            validationRequirements=new ValidationRequirements();
    }
    return validationRequirements;
  }

  public TableName getTableName() {
    return tableName;
  }

  public void setTableName(TableName tableName) {
    this.tableName = tableName;
  }

  @Override
  public CatalogName getEffectiveCatalog() {
    CatalogName effective;
    if(tableName != null){
      effective = tableName.getCatalogName();
    }else{
      effective = catalog;
    }
    if(sessionCatalog != null){
      effective = sessionCatalog;
    }
    return effective;
  }

}
