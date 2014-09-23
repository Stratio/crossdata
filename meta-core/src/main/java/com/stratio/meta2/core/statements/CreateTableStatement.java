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

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.engine.EngineConfig;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that models a {@code CREATE TABLE} statement of the META language.
 */
public class CreateTableStatement extends MetadataStatement implements ITableStatement {

  /**
   * The name of the target table.
   */
  private TableName tableName;

  private ClusterName clusterName;

  /**
   * A map with the name of the columns in the table and the associated data type.
   */
  private Map<ColumnName, ColumnType> columnsWithType;

  /**
   * The list of columns that are part of the primary key.
   */
  private List<ColumnName> primaryKey;

  /**
   * The list of columns that are part of the partition key.
   */
  private List<ColumnName> partitionKey;

  /**
   * The list of columns that are part of the clustering key.
   */
  private List<ColumnName> clusterKey;

  /**
   * The list of {@link com.stratio.meta2.core.structures.Property} of the table.
   */
  private Map<Selector, Selector> properties = new LinkedHashMap<>();

  /**
   * Whether the table should be created only if not exists.
   */
  private boolean ifNotExists;

  /**
   * Whether the table will be created.
   */
  private boolean createTable = false;

  /**
   * Class constructor.
   *
   * @param tableName The name of the table.
   * @param columns A map with the name of the columns in the table and the associated data type.
   * @param partitionKey The list of columns that are part of the primary key.
   * @param clusterKey The list of columns that are part of the clustering key.
   */
  public CreateTableStatement(TableName tableName, ClusterName clusterName,
                              Map<ColumnName, ColumnType> columns,
                              List<ColumnName> partitionKey, List<ColumnName> clusterKey) {
    this.command = false;
    this.tableName = tableName;
    this.clusterName = clusterName;
    this.columnsWithType = columns;
    this.partitionKey = partitionKey;
    this.clusterKey = clusterKey;
    this.primaryKey.addAll(partitionKey);
    this.primaryKey.addAll(clusterKey);
  }

  public Map<ColumnName, ColumnType> getColumnsWithTypes() {
    return columnsWithType;
  }

  public TableName getTableName() {
    return tableName;
  }

  public ClusterName getClusterName() {
    return clusterName;
  }

  /**
   * Set the catalog specified in the create table statement.
   *
   * @param catalog The name of the catalog.
   */
  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  /**
   * Set the list of {@link com.stratio.meta2.core.structures.Property}.
   *
   * @param properties The list.
   */
  public void setProperties(String properties) {
    this.properties = StringUtils.convertJsonToOptions(properties);
  }

  public void setIfNotExists(boolean ifNotExists) {
    this.ifNotExists = ifNotExists;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("CREATE TABLE ");
    if (ifNotExists) {
      sb.append("IF NOT EXISTS ");
    }
    sb.append(tableName.getQualifiedName());
    sb.append(" ON CLUSTER ").append(clusterName);

    sb.append("(");
    sb.append(columnsWithType);
    sb.append(", PRIMARY KEY ((").append(partitionKey).append("), ").append(clusterKey).append(")");
    sb.append(")");

    if (hasProperties()) {
      sb.append(" WITH ").append(properties);
    }
    return sb.toString();
  }

  private boolean hasProperties() {
    return ((properties != null) && (!properties.isEmpty()));
  }

  /** {@inheritDoc} */
  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result = validateCatalogAndTable(metadata);

    if (!result.hasError()){
      result=validateEphemeral(metadata);
    }

    if(!result.hasError()){
      result = validateColumns();
    }
    if (!result.hasError() && hasProperties()) {
      result = validateProperties();
    }
    return result;
  }

  private Result validateEphemeral(MetadataManager metadata) {
    Result result = QueryResult.createSuccessQueryResult();
    createTable = true;
    if (metadata.checkStream(getEffectiveCatalog()+"."+tableName)){
      result= Result.createValidationErrorResult(tableName+ " already exists in catalog "+ getEffectiveCatalog());
      createTable = false;
    }
    return result;
  }

  /**
   * Validate that a valid catalog is present, and that the table does not
   * exits unless {@code ifNotExists} has been specified.
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
   *                 the required information.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateCatalogAndTable(MetadataManager metadata){
    Result result = QueryResult.createSuccessQueryResult();
    //Get the effective catalog based on the user specification during the create
    //sentence, or taking the catalog in use in the user session.
    String effectiveCatalog = getEffectiveCatalog();

    //Check that the catalog exists, and that the table does not exits.
    if(effectiveCatalog == null || effectiveCatalog.length() == 0){
      result = Result.createValidationErrorResult("Target catalog missing or no catalog has been selected.");
    }else{
      CatalogMetadata ksMetadata = metadata.getCatalogMetadata(effectiveCatalog);
      if(ksMetadata == null){
        result = Result.createValidationErrorResult("Catalog " + effectiveCatalog + " does not exist.");
      }else {
        TableMetadata
            tableMetadata = metadata.getTableMetadata(effectiveCatalog, tableName);
        if (tableMetadata != null && !ifNotExists) {
          result = Result.createValidationErrorResult("Table " + tableName + " already exists.");
        } else if (tableMetadata == null){
          createTable = true;
        }
      }
    }
    return result;
  }

  /**
   * Validate that the primary key is created and uses a set
   * of existing columns. The same checks are applied to the clustering
   * key if it exists.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateColumns(){
    Result result = QueryResult.createSuccessQueryResult();
    //The columns in the primary key must be declared.
    for (ColumnName pk : primaryKey) {
      if(!columnsWithType.containsKey(pk)){
        result= Result.createValidationErrorResult("Missing declaration for Primary Key column " + pk);
      }
    }

    //The columns in the clustering key must be declared and not part of the primary key.
    for(ColumnName ck : clusterKey){
      if(!columnsWithType.containsKey(ck)){
        result= Result.createValidationErrorResult("Missing declaration for Clustering Key column " + ck);
      }
      if(primaryKey.contains(ck)){
        result= Result.createValidationErrorResult("Column " + ck + " found as part of primary and clustering key.");
      }
    }

    String [] supported = {"BIGINT", "BOOLEAN", "COUNTER", "DOUBLE", "FLOAT", "INT", "VARCHAR"};
    Set<String> supportedColumns = new HashSet<>(Arrays.asList(supported));
    for(ColumnName c : columnsWithType.keySet()){
      if(!supportedColumns.contains(columnsWithType.get(c).toString().toUpperCase()) || c.getName().toLowerCase().startsWith("stratio")){
        result= Result.createValidationErrorResult("Column " + c + " with datatype " + columnsWithType.get(c) + " not supported.");
      }
    }

    return result;
  }

  /**
   * Validate the semantics of the ephemeral properties.
   *
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateProperties() {
    Result result = QueryResult.createSuccessQueryResult();
    /*
    Iterator<Property> props = properties.iterator();
    boolean exit = false;
    while (!exit && props.hasNext()) {
      Property property = props.next();
      if (property.getType() == Property.TYPE_NAME_VALUE) {
        PropertyNameValue propertyNameValue = (PropertyNameValue) property;
        if ("ephemeral".equalsIgnoreCase(propertyNameValue.getName())
            && propertyNameValue.getVp().getTermClass() != Boolean.class) {
          // If property ephemeral is present, it must be a boolean type
          result = Result.createValidationErrorResult("Property 'ephemeral' must be a boolean");
          exit = true;
        } else if ("ephemeral_tuples".equalsIgnoreCase(propertyNameValue.getName())
                   && propertyNameValue.getVp().getTermClass() != Boolean.class) {
          // If property ephemeral_tuples is present, it must be a integer type
          result= Result.createValidationErrorResult("Property 'ephemeral' must be a boolean");
          exit = true;
        } else if ("ephemeral_persist_on".equalsIgnoreCase(propertyNameValue.getName())
                   && propertyNameValue.getVp().getTermClass() != Boolean.class) {
          // If property ephemeral_persist_on is present, it must be a string type
          result= Result.createValidationErrorResult("Property 'ephemeral_persist_on' must be a string");
          exit = true;
        }
      }
    }*/
    return result;
  }

  /**
   * Read splits, add single quotes (if neccesary) and group params in pair to CQL format.
   *
   * @param splits array of params.
   * @return params translated to Cql.
   */
  private String addQuotesAndClassifyParams(String[] splits) {
    StringBuilder sb = new StringBuilder();
    for (int j = 0; j < splits.length; j++) {
      String currentStr = splits[j];
      if (currentStr.matches("[0123456789.]+")) {
        sb.append(splits[j]);
      } else {
        sb.append("\'").append(splits[j]).append("\'");
      }
      if (j % 2 == 0) {
        sb.append(": ");
      } else if (j < (splits.length - 1)) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  public ValidationRequirements getValidationRequirements(){
    return new ValidationRequirements().add(Validation.MUST_EXIST_CATALOG)
        .add(Validation.MUST_EXIST_CLUSTER)
        .add(Validation.MUST_NOT_EXIST_TABLE);
  }

  public void setTableName(TableName tableName) {
    this.tableName = tableName;
  }

  @Override
  public String getEffectiveCatalog() {
    String effective;
    if(tableName != null){
      effective = tableName.getCatalogName().getName();
    }else{
      effective = catalog;
    }
    if(sessionCatalog != null){
      effective = sessionCatalog;
    }
    return effective;
  }

}
