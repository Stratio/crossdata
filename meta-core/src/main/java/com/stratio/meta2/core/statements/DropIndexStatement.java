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
import com.stratio.meta2.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.core.validator.ValidationRequirements;

import java.util.Iterator;

/**
 * Class that models a {@code DROP INDEX} statement from the META language.
 */
public class DropIndexStatement extends MetaDataStatement {

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
   * @param name The name of the index. The name may contain the name of the catalog where the
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
    //Get the effective catalog based on the user specification during the create
    //sentence, or taking the catalog in use in the user session.
    String effectiveCatalog = getEffectiveCatalog();
    if(catalogInc){
      effectiveCatalog = catalog;
    }

    //Check that the catalog and table exists.
    if(effectiveCatalog == null || effectiveCatalog.length() == 0){
      result= Result.createValidationErrorResult(
          "Target catalog missing or no catalog has been selected.");
    }else{
      CatalogMetadata ksMetadata = metadata.getCatalogMetadata(effectiveCatalog);
      if(ksMetadata == null){
        result= Result.createValidationErrorResult(
            "Catalog " + effectiveCatalog + " does not exist.");
      }else{
        result = validateIndexName(ksMetadata);
      }
    }
    return result;
  }

  /**
   * Validate the existence of the index in the selected catalog.
   * @param ksMetadata The catalog metadata.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  public Result validateIndexName(CatalogMetadata ksMetadata){
    Result result = QueryResult.createSuccessQueryResult();
    boolean found = false;
    Iterator<TableMetadata> tables = ksMetadata.getTables().values().iterator();

    while(tables.hasNext() && !found){
      TableMetadata tableMetadata = tables.next();
      Iterator<ColumnMetadata> columns = tableMetadata.getColumns().values().iterator();
      while(columns.hasNext() && !found){
        ColumnMetadata column = columns.next();
        if(column.getIndex() != null
           && (column.getIndex().equals(name)
               || column.getIndex().equals("stratio_lucene_" + name))){
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
  public ValidationRequirements getValidationRequirements() {
    return new ValidationRequirements();
  }

}
