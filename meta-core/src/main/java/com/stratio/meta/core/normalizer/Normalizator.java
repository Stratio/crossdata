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
import com.stratio.meta.common.exceptions.validation.BadFormatException;
import com.stratio.meta.common.exceptions.validation.NotExistNameException;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.*;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.SelectParsedQuery;

import java.util.ArrayList;
import java.util.List;

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
    for(TableName tableName: fromTables){
      checkTable(tableName);
      fields.getCatalogNames().add(tableName.getCatalogName());
      fields.getSearchTableNames().add(tableName);
      fields.getTableNames().add(tableName);
    }
  }

  public void normalizeJoins() throws NotExistNameException {
    InnerJoin innerJoin= parsedQuery.getStatement().getJoin();
    if(innerJoin!=null){
      TableName joinTable = innerJoin.getTablename();
      checkTable(joinTable);
      fields.getSearchTableNames().add(joinTable);
      fields.getTableNames().add(joinTable);

    }
  }


  public void normalizeSelectExpresion()
      throws AmbiguousNameException, NotExistNameException {
    SelectExpression selectExpression=parsedQuery.getStatement().getSelectExpression();
    fields.setDistinctSelect(selectExpression.isDistinct());
    List<Selector> normalizeSelectors= checkListSelector(selectExpression.getSelectorList());
    fields.getSelectors().addAll(normalizeSelectors);
  }


  public void checkRelations(List<Relation> relations)
      throws BadFormatException, AmbiguousNameException, NotExistNameException {
    for(Relation relation:relations){
      switch (relation.getIdentifier().getType()){
        case FUNCTION:
          checkFunctionSelector((FunctionSelector) relation.getIdentifier());
          break;
        case COLUMN:
          checkColumnSelector((ColumnSelector)relation.getIdentifier());
          break;
        case ASTERISK:
          throw new BadFormatException("You mustn't put asterisk in relations");
      }
      //TODO: Resolve terms problem
    }
  }


  public void checkTable(TableName tableName)
      throws NotExistNameException {
    if (!tableName.isCompletedName()) {
      tableName.setCatalogName(parsedQuery.getDefaultCatalog());
    }
    if (!MetadataManager.MANAGER.exists(tableName)) {
      throw new NotExistNameException(tableName);
    }
  }



  public void checkColumnSelector(ColumnSelector selector)
      throws AmbiguousNameException, NotExistNameException {
    ColumnName columnName=selector.getName();
    if(!columnName.isCompletedName()){
      TableName searched=this.searchTableNameByColumn(columnName);
      columnName.setTableName(searched);
    }
    fields.getColumnNames().add(columnName);
    fields.getTableNames().add(columnName.getTableName());
  }

  public TableName searchTableNameByColumn(ColumnName columnName)
      throws AmbiguousNameException, NotExistNameException {
    TableName selectTableName=null;
    for(TableName tableName:fields.getSearchTableNames()){
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

  public List<ColumnSelector> checkAsteriskSelector(){
    List<ColumnSelector> columnSelectors =new ArrayList<>();
    for(TableName table: fields.getSearchTableNames()){
      TableMetadata tableMetadata =MetadataManager.MANAGER.getTable(table);
      for(ColumnName columnName:tableMetadata.getColumns().keySet()){
        ColumnSelector selector=new ColumnSelector(columnName);
        columnSelectors.add(selector);
        fields.getColumnNames().add(columnName);
      }
    }
    return columnSelectors;
  }
  public List<Selector> checkListSelector(List<Selector> selectors)
      throws AmbiguousNameException, NotExistNameException {
    List<Selector> result=new ArrayList<>();
    for(Selector selector:selectors){
      switch(selector.getType()){
        case FUNCTION:
          FunctionSelector functionSelector=(FunctionSelector)selector;
          checkFunctionSelector(functionSelector);
          result.add(functionSelector);
          break;
        case COLUMN:
          ColumnSelector columnSelector=(ColumnSelector)selector;
          checkColumnSelector(columnSelector);
          result.add(columnSelector);
          break;
        case ASTERISK:
          result.addAll(checkAsteriskSelector());
          break;
      }
    }
    return result;
  }

  public void checkFunctionSelector(FunctionSelector functionSelector)
      throws AmbiguousNameException, NotExistNameException {
    List<Selector> normalizeSelector= checkListSelector(functionSelector.getFunctionColumns());
    functionSelector.getFunctionColumns().clear();
    functionSelector.getFunctionColumns().addAll(normalizeSelector);
  }
}
