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

package com.stratio.meta.core.normalizer;

import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.exceptions.validation.AmbiguousNameException;
import com.stratio.meta.common.exceptions.validation.NotExistNameException;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.*;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.SelectParsedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Normalizator {
  private NormalizedFields fields=new NormalizedFields();
  private SelectParsedQuery parsedQuery;

  public Normalizator(SelectParsedQuery parsedQuery) {
    this.parsedQuery = parsedQuery;
  }

  public NormalizedFields getFields() {
    return fields;
  }

  public void execute(){

  }

  public void normalizeTables() throws
      ValidationException {
    List<TableName> fromTables= parsedQuery.getStatement().getFromTables();
    CatalogName sessionCatalog=parsedQuery.getDefaultCatalog();
    for(TableName tableName: fromTables){
      check(tableName,sessionCatalog);
      fields.getCatalogNames().add(tableName.getCatalogName());
      fields.getFromTableNames().add(tableName);
      fields.getTableNames().add(tableName);
    }
  }


  public void normalizeSelectExpresion()
      throws AmbiguousNameException, NotExistNameException {
    SelectExpression selectExpression=parsedQuery.getStatement().getSelectExpression();
    fields.setDistinctSelect(selectExpression.isDistinct());
    List<Selector> normalizeSelectors=check(selectExpression.getSelectorList(),fields.getTableNames());
    fields.getSelectors().addAll(normalizeSelectors);

  }


  public void check(TableName tableName, CatalogName sessionCatalog)
      throws NotExistNameException {
    if (!tableName.isCompletedName()) {
      tableName.setCatalogName(sessionCatalog);
    }
    if (!MetadataManager.MANAGER.exists(tableName)) {
      throw new NotExistNameException(tableName);
    }
  }



  public void check(ColumnSelector selector,Set<TableName> tableNames)
      throws AmbiguousNameException, NotExistNameException {
    ColumnName columnName=selector.getName();
    if(!columnName.isCompletedName()){
      TableName searched=this.searchTableNameByColumn(columnName, tableNames);
      columnName.setTableName(searched);
    }
    fields.getColumnNames().add(columnName);
    fields.getTableNames().add(columnName.getTableName());
  }

  public TableName searchTableNameByColumn(ColumnName columnName, Set<TableName> tableNames)
      throws AmbiguousNameException, NotExistNameException {
    TableName selectTableName=null;
    for(TableName tableName:tableNames){
      columnName.setTableName(tableName);
      if(MetadataManager.MANAGER.exists(columnName)){
        if(selectTableName==null){
          selectTableName=tableName;
        }else{
          columnName.setTableName(null);
          throw new AmbiguousNameException(columnName);
        }
      }
    }
    if(selectTableName==null){
      throw new NotExistNameException(columnName);
    }
    return selectTableName;
  }

  public List<ColumnSelector> check(AsteriskSelector asteriskSelector, Set<TableName> tableNames){
    List<ColumnSelector> columnSelectors =new ArrayList<>();
    for(TableName table: tableNames){
      TableMetadata tableMetadata =MetadataManager.MANAGER.getTable(table);
      for(ColumnName columnName:tableMetadata.getColumns().keySet()){
        ColumnSelector selector=new ColumnSelector(columnName);
        columnSelectors.add(selector);
        fields.getColumnNames().add(columnName);

      }
    }
    return columnSelectors;
  }
  public List<Selector> check(List<Selector> selectors, Set<TableName> tableNames)
      throws AmbiguousNameException, NotExistNameException {
    List<Selector> result=new ArrayList<>();
    for(Selector selector:selectors){
      switch(selector.getType()){
        case FUNCTION:
          FunctionSelector functionSelector=(FunctionSelector)selector;
          check(functionSelector,tableNames);
          result.add(functionSelector);
          break;
        case COLUMN:
          ColumnSelector columnSelector=(ColumnSelector)selector;
          check(columnSelector,tableNames);
          result.add(columnSelector);
          break;
        case ASTERISK:
          AsteriskSelector asteriskSelector =(AsteriskSelector) selector;
          result.addAll(check(asteriskSelector, tableNames));
          break;
      }
    }
    return result;
  }

  public void check(FunctionSelector functionSelector, Set<TableName> tableNames)
      throws AmbiguousNameException, NotExistNameException {
    List<Selector> normalizeSelector=check(functionSelector.getFunctionColumns(),tableNames);
    functionSelector.getFunctionColumns().clear();
    functionSelector.getFunctionColumns().addAll(normalizeSelector);
  }
}
