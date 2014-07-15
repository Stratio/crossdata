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

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.BooleanProperty;
import com.stratio.meta.core.structures.Property;
import com.stratio.meta.core.structures.PropertyNameValue;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that models a {@code CREATE TABLE} statement of the META language.
 */
public class CreateTableStatement extends MetaStatement {


  /**
   * The name of the target table.
   */
  private String tableName;

  /**
   * A map with the name of the columns in the table and the associated data type.
   */
  private Map<String, String> columnsWithType;

  /**
   * The list of columns that are part of the primary key.
   */
  private List<String> primaryKey;

  /**
   * The list of columns that are part of the clustering key.
   */
  private List<String> clusterKey;

  /**
   * The list of {@link com.stratio.meta.core.structures.Property} of the table.
   */
  private List<Property> properties = new ArrayList<>();

  /**
   * The type of primary key. Accepted values are:
   * <ul>
   * <li>1: If the primary key contains a single column.</li>
   * <li>2: If the primary key is composed of several columns but it does not contain a clustering
   * key.</li>
   * <li>3: If both the primary key and clustering key are specified.</li>
   * </ul>
   */
  private int primaryKeyType;

  private static final int PRIMARY_SINGLE = 1;
  private static final int PRIMARY_COMPOSED = 2;
  private static final int PRIMARY_AND_CLUSTERING_SPECIFIED = 3;

  /**
   * Whether the table should be created only if not exists.
   */
  private boolean ifNotExists;

  /**
   * Whether the table will be created.
   */
  private boolean createTable = false;

  /**
   * The number of the column associated with the primary key. This value is only used if the type
   * of primary key is {@code 1}.
   */
  private int columnNumberPK;

  /**
   * Whether the table should be created with a set of properties.
   */
  private boolean withProperties = false;

  /**
   * Class constructor.
   *
   * @param tableName The name of the table.
   * @param columns A map with the name of the columns in the table and the associated data type.
   * @param primaryKey The list of columns that are part of the primary key.
   * @param clusterKey The list of columns that are part of the clustering key.
   * @param primaryKeyType The type of primary key.
   * @param columnNumberPK The number of the column associated with the primary key. This value is
   *        only used if the type of primary key is {@code 1}.
   */
  public CreateTableStatement(String tableName, Map<String, String> columns,
                              List<String> primaryKey, List<String> clusterKey, int primaryKeyType, int columnNumberPK) {
    this.command = false;
    if (tableName.contains(".")) {
      String[] ksAndTablename = tableName.split("\\.");
      catalog = ksAndTablename[0];
      this.tableName = ksAndTablename[1];
      catalogInc = true;
    } else {
      this.tableName = tableName;
    }
    this.columnsWithType = columns;
    this.primaryKey = primaryKey;
    this.clusterKey = clusterKey;
    this.primaryKeyType = primaryKeyType;
    this.columnNumberPK = columnNumberPK;
  }

  public Map<String, String> getColumnsWithTypes() {
    return columnsWithType;
  }

  public String getTableName() {
    return tableName;
  }

  /**
   * Set the keyspace specified in the create table statement.
   *
   * @param keyspace The name of the keyspace.
   */
  public void setKeyspace(String keyspace) {
    this.catalog = keyspace;
  }

  /**
   * Set the list of {@link com.stratio.meta.core.structures.Property}.
   *
   * @param properties The list.
   */
  public void setProperties(List<Property> properties) {
    this.properties = properties;
    if ((properties == null) || properties.isEmpty()) {
      withProperties = false;
    } else {
      withProperties = true;
    }
  }

  public void setIfNotExists(boolean ifNotExists) {
    this.ifNotExists = ifNotExists;
  }

  public void setWithProperties(boolean withProperties) {
    this.withProperties = withProperties;
  }

  public String getSinglePKString() {
    StringBuilder sb = new StringBuilder(" (");
    Set<String> keySet = columnsWithType.keySet();
    int i = 0;
    for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
      String key = it.next();
      String vp = columnsWithType.get(key);
      sb.append(key).append(" ").append(vp);
      if (i == columnNumberPK) {
        sb.append(" PRIMARY KEY");
      }
      i++;
      if (it.hasNext()) {
        sb.append(", ");
      } else {
        sb.append(")");
      }
    }
    return sb.toString();
  }

  public String getCompositePKString() {
    StringBuilder sb = new StringBuilder("PRIMARY KEY (");
    if (primaryKeyType == PRIMARY_AND_CLUSTERING_SPECIFIED) {
      sb.append("(");
    }

    Iterator<String> pks = primaryKey.iterator();
    while (pks.hasNext()) {
      sb.append(pks.next());
      if (pks.hasNext()) {
        sb.append(", ");
      }
    }

    if (primaryKeyType == PRIMARY_AND_CLUSTERING_SPECIFIED) {
      sb.append(")");
      for (String key : clusterKey) {
        sb.append(", ").append(key);
      }
    }

    sb.append("))");
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Create table ");
    if (ifNotExists) {
      sb.append("IF NOT EXISTS ");
    }

    if (catalogInc) {
      sb.append(catalog).append(".");
    }
    sb.append(tableName);

    if (primaryKeyType == PRIMARY_SINGLE) {
      sb.append(getSinglePKString());
    } else {
      Set<String> keySet = columnsWithType.keySet();
      sb.append(" (");
      for (String key : keySet) {
        String vp = columnsWithType.get(key);
        sb.append(key).append(" ").append(vp).append(", ");
      }
      sb.append(getCompositePKString());
    }

    if (withProperties) {
      sb.append(" WITH ").append(StringUtils.stringList(properties, " AND "));
    }
    return sb.toString();
  }

  /** {@inheritDoc} */
  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result = validateKeyspaceAndTable(metadata);

    if (!result.hasError()){
      result=validateEphimeral(metadata);
    }

    if(!result.hasError()){
      result = validateColumns();
    }
    if (!result.hasError() && withProperties) {
      result = validateProperties();
    }
    return result;
  }

  private Result validateEphimeral(MetadataManager metadata) {
    Result result = QueryResult.createSuccessQueryResult();
    createTable = true;
    if (metadata.checkStream(getEffectiveCatalog()+"."+tableName)){
      result= Result.createValidationErrorResult(tableName+ " already exists in catalog "+ getEffectiveCatalog());
      createTable = false;
    }
    return result;
  }

  /**
   * Validate that a valid keyspace is present, and that the table does not
   * exits unless {@code ifNotExists} has been specified.
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
   *                 the required information.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateKeyspaceAndTable(MetadataManager metadata){
    Result result = QueryResult.createSuccessQueryResult();
    //Get the effective keyspace based on the user specification during the create
    //sentence, or taking the keyspace in use in the user session.
    String effectiveKeyspace = getEffectiveCatalog();

    //Check that the keyspace exists, and that the table does not exits.
    if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
      result = Result.createValidationErrorResult("Target catalog missing or no catalog has been selected.");
    }else{
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
      if(ksMetadata == null){
        result = Result.createValidationErrorResult("Keyspace " + effectiveKeyspace + " does not exist.");
      }else {
        TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tableName);
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
    for (String pk : primaryKey) {
      if(!columnsWithType.containsKey(pk)){
        result= Result.createValidationErrorResult("Missing declaration for Primary Key column " + pk);
      }
    }

    //The columns in the clustering key must be declared and not part of the primary key.
    for(String ck : clusterKey){
      if(!columnsWithType.containsKey(ck)){
        result= Result.createValidationErrorResult("Missing declaration for Clustering Key column " + ck);
      }
      if(primaryKey.contains(ck)){
        result= Result.createValidationErrorResult("Column " + ck + " found as part of primary and clustering key.");
      }
    }

    String [] supported = {"BIGINT", "BOOLEAN", "COUNTER", "DOUBLE", "FLOAT", "INT", "VARCHAR"};
    Set<String> supportedColumns = new HashSet<>(Arrays.asList(supported));
    for(String c : columnsWithType.keySet()){
      if(!supportedColumns.contains(columnsWithType.get(c).toUpperCase()) || c.toLowerCase().startsWith("stratio")){
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
          result= Result.createValidationErrorResult("Property 'ephemeral' must be a boolean");
          exit = true;
        } else if ("ephemeral_persist_on".equalsIgnoreCase(propertyNameValue.getName())
                   && propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN) {
          // If property ephemeral_persist_on is present, it must be a string type
          result= Result.createValidationErrorResult("Property 'ephemeral_persist_on' must be a string");
          exit = true;
        }
      }
    }
    return result;
  }

  @Override
  public String translateToCQL() {
    String cqlString = this.toString();
    if (!cqlString.contains(" WITH ")) {
      return cqlString;
    }
    StringBuilder sb = new StringBuilder();
    int i = 0;
    while (i < cqlString.length()) {
      char c = cqlString.charAt(i);
      if (c == '{') {
        sb.append("{");
        int newI = cqlString.indexOf('}', i);
        String insideBracket = cqlString.substring(i + 1, newI);
        insideBracket = insideBracket.replace(":", " ").replace(",", " ");

        boolean wasChanged = true;
        while (wasChanged) {
          int before = insideBracket.length();
          insideBracket = insideBracket.replace("  ", " ");
          int after = insideBracket.length();
          if (before == after) {
            wasChanged = false;
          }
        }

        insideBracket = insideBracket.trim();
        String[] splits = insideBracket.split(" ");
        // Check between brackets
        sb.append(addQuotesAndClassifyParams(splits));
        sb.append("}");
        i = newI;
      } else {
        sb.append(c);
      }
      i++;
    }
    return sb.toString();
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

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    if (createTable) {
      tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
      boolean streamingMode = false;
      if (withProperties) {
        for (Property property : properties) {
          if (property.getType() == Property.TYPE_NAME_VALUE) {
            PropertyNameValue pnv = (PropertyNameValue) property;
            String propName = pnv.getName();
            if ("ephemeral".equalsIgnoreCase(propName)
                && (pnv.getVp().getType() == ValueProperty.TYPE_BOOLEAN)
                && ((BooleanProperty) pnv.getVp()).getBool()) {
              streamingMode = true;
              break;
            }
          }
        }
      }
      if (streamingMode) {
        tree.setNode(new MetaStep(MetaPath.STREAMING, this));
      }
    }
    return tree;
  }

}
