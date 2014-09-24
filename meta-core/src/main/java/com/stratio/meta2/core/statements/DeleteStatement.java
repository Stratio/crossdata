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
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.core.engine.EngineConfig;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that models a {@code SELECT} statement from the META language. This class recognizes the
 * following syntax:
 * <p>
 * DELETE ( {@literal <column>}, ( ',' {@literal <column>} )*)? FROM {@literal <tablename>} WHERE
 * {@literal <where_clause>};
 */
public class DeleteStatement extends StorageStatement implements ITableStatement {

  /**
   * The name of the targe table.
   */
  private TableName tableName = null;

  /**
   * The list of {@link com.stratio.meta.common.statements.structures.relationships.Relation} found
   * in the WHERE clause.
   */
  private List<Relation> whereClauses;


  public DeleteStatement(TableName tableName, List<Relation> whereClauses) {
    this.tableName = tableName;
    this.whereClauses = whereClauses;
  }

  /**
   * Set the name of the table.
   * 
   * @param tableName The name of the table.
   */
  public void setTableName(TableName tableName) {
    this.tableName = tableName;
  }

  /**
   * Add a new {@link com.stratio.meta.common.statements.structures.relationships.Relation} found in
   * a WHERE clause.
   * 
   * @param relation The relation.
   */
  public void addRelation(Relation relation) {
    whereClauses.add(relation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("DELETE FROM ");
    if (catalogInc) {
      sb.append(catalog).append(".");
    }
    sb.append(tableName);
    if (!whereClauses.isEmpty()) {
      sb.append(" WHERE ");
      sb.append(StringUtils.stringList(whereClauses, " AND "));
    }
    return sb.toString();
  }


  @Override
  public ValidationRequirements getValidationRequirements() {
    return new ValidationRequirements().add(Validation.MUST_EXIST_CATALOG).add(Validation.MUST_EXIST_TABLE).add(Validation.MUST_EXIST_COLUMN);
  }

  public TableName getTableName() {
    return tableName;
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
