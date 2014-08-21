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

import java.util.Iterator;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code DROP INDEX} statement from the META language.
 */
public class DropIndexStatement extends MetaStatement {

  /**
   * Whether the index should be dropped only if exists.
   */
  private boolean dropIfExists = false;

  /**
   * Whether the index will be dropped.
   */
  private boolean dropIndex = false;

  /**
   * The name of the index.
   */
  private String name = null;

  /**
   * Target column associated with the index.
   */
  private ColumnMetadata targetColumn = null;

  /**
   * Class constructor.
   */
  public DropIndexStatement() {
    this.command = false;
  }

  /**
   * Set the option to drop the index only if exists.
   */
  public void setDropIfExists() {
    dropIfExists = true;
  }

  /**
   * Set the index name.
   * 
   * @param name The name of the index. The name may contain the name of the keyspace where the
   *        index is active.
   */
  public void setName(String name) {
    this.name = name;
    if (name.contains(".")) {
      String[] ksAndName = name.split("\\.");
      catalog = ksAndName[0];
      this.name = ksAndName[1];
      catalogInc = true;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("DROP INDEX ");
    if (dropIfExists) {
      sb.append("IF EXISTS ");
    }
    if (catalogInc) {
      sb.append(catalog).append(".");
    }
    sb.append(name);
    return sb.toString();
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {

    Result result = null;
    //Get the effective keyspace based on the user specification during the create
    //sentence, or taking the keyspace in use in the user session.
    String effectiveKeyspace = getEffectiveCatalog();
    if(catalogInc){
      effectiveKeyspace = catalog;
    }

    //Check that the keyspace and table exists.
    if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
      result= Result.createValidationErrorResult(
          "Target catalog missing or no catalog has been selected.");
    }else{
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
      if(ksMetadata == null){
        result= Result.createValidationErrorResult(
            "Keyspace " + effectiveKeyspace + " does not exist.");
      }else{
        result = validateIndexName(ksMetadata);
      }
    }
    return result;
  }

  /**
   * Validate the existence of the index in the selected keyspace.
   * @param ksMetadata The keyspace metadata.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  public Result validateIndexName(KeyspaceMetadata ksMetadata){
    Result result = QueryResult.createSuccessQueryResult();
    boolean found = false;
    Iterator<TableMetadata> tables = ksMetadata.getTables().iterator();

    while(tables.hasNext() && !found){
      TableMetadata tableMetadata = tables.next();
      Iterator<ColumnMetadata> columns = tableMetadata.getColumns().iterator();
      while(columns.hasNext() && !found){
        ColumnMetadata column = columns.next();
        if(column.getIndex() != null
           && (column.getIndex().getName().equals(name)
               || column.getIndex().getName().equals("stratio_lucene_" + name))){
          found = true;
          targetColumn = column;
        }
      }
    }

    if(!dropIfExists && !found){
      result = Result.createValidationErrorResult("Index " + name + " not found in catalog " + ksMetadata.getName());
    }else{
      dropIndex = true;
    }

    return result;
  }

  @Override
  public String translateToCQL() {
    return this.toString();
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree result = new Tree();
    if(dropIndex) {
      //Add CREATE INDEX as the root.
      StringBuilder sb = new StringBuilder("DROP INDEX ");
      if(catalogInc) {
        sb.append(catalog).append(".");
      }
      sb.append(targetColumn.getIndex().getName());

      if (targetColumn.getIndex().getName().startsWith("stratio")) {
        //Remove associated column.
        StringBuilder sb2 = new StringBuilder("ALTER TABLE ");
        if(catalogInc) {
          sb2.append(catalog).append(".");
        }
        sb2.append(targetColumn.getTable().getName());
        sb2.append(" DROP ").append("stratio_lucene_").append(name);

        result.setNode(new MetaStep(MetaPath.CASSANDRA, sb2.toString()));
        result.addChild(new Tree(new MetaStep(MetaPath.CASSANDRA, sb.toString())));
      }else{
        result.setNode(new MetaStep(MetaPath.CASSANDRA, sb.toString()));
      }

    }
    return result;
  }

}
