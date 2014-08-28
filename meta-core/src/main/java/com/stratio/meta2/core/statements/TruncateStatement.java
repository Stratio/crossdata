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

import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Truncate;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.core.engine.validator.ValidationRequirements;

import static com.datastax.driver.core.querybuilder.QueryBuilder.truncate;

public class TruncateStatement extends MetaStatement {

  private TableName tablename;

  public TruncateStatement(TableName tablename) {
    this.command = false;
    this.tablename = tablename;
  }

  public boolean isCatalogInc() {
    return catalogInc;
  }

  public void setCatalogInc(boolean catalogInc) {
    this.catalogInc = catalogInc;
  }

  public String getCatalog() {
    return catalog;
  }

  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  public TableName getTablename() {
    return tablename;
  }

  public void setTablename(TableName tablename) {
    this.tablename = tablename;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TRUNCATE ");
    if (catalogInc) {
      sb.append(catalog).append(".");
    }
    sb.append(tablename);
    return sb.toString();
  }


  @Override
  public String translateToCQL() {
    return this.toString();
  }

  @Override
  public Statement getDriverStatement() {
    Truncate truncateQuery;
    if (catalogInc) {
      truncateQuery = truncate(catalog, tablename.getName());
    } else {
      truncateQuery = truncate(tablename.getName());
    }
    return truncateQuery;
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result = QueryResult.createSuccessQueryResult();

    String effectiveCatalog = getEffectiveCatalog();
    if (catalogInc) {
      effectiveCatalog = catalog;
    }

    // Check that the catalog and table exists.
    if (effectiveCatalog == null || effectiveCatalog.length() == 0) {
      result =
          Result
              .createValidationErrorResult("Target catalog missing or no catalog has been selected.");
    } else {
      CatalogMetadata ksMetadata = metadata.getCatalogMetadata(effectiveCatalog);
      if (ksMetadata == null) {
        result =
            Result.createValidationErrorResult("Catalog " + effectiveCatalog + " does not exist.");
      } else {
        TableMetadata tableMetadata = metadata.getTableMetadata(effectiveCatalog, tablename);
        if (tableMetadata == null) {
          result = Result.createValidationErrorResult("Table " + tablename + " does not exist.");
        }
      }
    }
    return result;

  }

  @Override
  public ValidationRequirements getValidationRequirements() {
    return new ValidationRequirements();
  }

}
