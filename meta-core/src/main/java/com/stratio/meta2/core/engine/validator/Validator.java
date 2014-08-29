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

package com.stratio.meta2.core.engine.validator;

import java.util.List;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.exceptions.validation.ExistCatalogException;
import com.stratio.meta.common.exceptions.validation.ExistTableException;
import com.stratio.meta.common.exceptions.validation.NotExistTableException;
import com.stratio.meta.common.exceptions.validation.NotExistCatalogException;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.common.data.CatalogName;
import org.apache.log4j.Logger;


import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.query.ValidatedQuery;

public class Validator {
  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Validator.class);

  public ValidatedQuery validate(ParsedQuery parsedQuery)
      throws ValidationException, IgnoreQueryException {
    this.validateRequirements(parsedQuery);
    return new ValidatedQuery(parsedQuery) ;
  }

  private void completeTableName(TableName tableName, CatalogName defaultCatalog) {
    if(!tableName.isCompletedName()){
      tableName.setCatalogName(defaultCatalog);
    }
  }




  private void validateRequirements(ParsedQuery parsedQuery)
      throws ValidationException, IgnoreQueryException {
    for(Validation req:parsedQuery.getValidationRequirements().getValidations()){
      validateRequirements(req,parsedQuery);
    }
  }
  private void validateRequirements(Validation requirement, ParsedQuery parsedQuery)
      throws ValidationException, IgnoreQueryException {
    /*
    TODO Move to ValidatedQuery
    switch (requirement) {
      case MUST_NOT_EXIST_CATALOG:
        validateNotExistCatalog(parsedQuery.getCatalogs(), parsedQuery.getIfNotExists());
        break;
      case MUST_EXIST_CATALOG:
        validateExistCatalog(parsedQuery.getCatalogs(), parsedQuery.getIfExists());
        break;
       case MUST_EXIST_TABLE:
         validateExistTable(parsedQuery.getTables(),parsedQuery.getIfExists(),parsedQuery.getDefaultCatalog());
         break;
      case MUST_NOT_EXIST_TABLE:
        validateNotExistTable(parsedQuery.getTables(), parsedQuery.getIfNotExists(),
            parsedQuery.getDefaultCatalog());
        break;
    }
    */
  }

  private void validateNotExistTable(List<TableName> tables, boolean hasIfNotExists,
      CatalogName defaultCatalog) throws IgnoreQueryException, ExistTableException {
    for(TableName tableName:tables){
      validateNotExistTable(tableName, hasIfNotExists, defaultCatalog);
    }

  }
  private void validateNotExistTable(TableName table, boolean hasIfNotExists,
      CatalogName defaultCatalog) throws IgnoreQueryException, ExistTableException {
    completeTableName(table,defaultCatalog);
    if(MetadataManager.MANAGER.existsTable(table)){
      if(hasIfNotExists){
        throw new IgnoreQueryException("TABLE ["+ table + "] EXIST");
      }else{
        throw new ExistTableException(table);
      }
    }
  }

  private void validateExistTable(List<TableName> tables, boolean hasIfExists, CatalogName defaultCatalog)
      throws IgnoreQueryException, NotExistTableException {
    for(TableName tableName:tables){
      validateExistTable(tableName,hasIfExists,defaultCatalog);
    }
  }


  private void validateExistTable(TableName table, boolean hasIfExists, CatalogName defaultCatalog)
      throws IgnoreQueryException, NotExistTableException {
    completeTableName(table,defaultCatalog);
    if(!MetadataManager.MANAGER.existsTable(table)){
      if(hasIfExists){
        throw new IgnoreQueryException("TABLE ["+ table + "] NOT EXISTS");
      }else{
        throw new NotExistTableException(table);
      }
    }
  }

  private void validateExistCatalog(List<CatalogName> catalogs, boolean hasIfExists)
      throws NotExistCatalogException, IgnoreQueryException {
    for(CatalogName catalog:catalogs){
      this.validateExistCatalog(catalog, hasIfExists);
    }

  }

  private void validateExistCatalog(CatalogName catalog, boolean hasIfExists)
      throws NotExistCatalogException, IgnoreQueryException {
    if(!MetadataManager.MANAGER.existsCatalog(catalog)){
      if(hasIfExists) {
        throw new IgnoreQueryException("CATALOG["+ catalog + "] NOT EXIST");
      }else{
        throw new NotExistCatalogException(catalog);
      }
    }
  }

  private void validateNotExistCatalog(List<CatalogName> catalogs, boolean hasIfNotExist)
      throws ExistCatalogException, IgnoreQueryException {
    for(CatalogName catalog:catalogs){
      this.validateNotExistCatalog(catalog,hasIfNotExist);
    }
  }


  private void validateNotExistCatalog(CatalogName catalog, boolean onlyIfNotExis)
      throws ExistCatalogException, IgnoreQueryException {
    if(MetadataManager.MANAGER.existsCatalog(catalog)){
      if(onlyIfNotExis) {
        throw new IgnoreQueryException("CATALOG["+ catalog + "] EXIST");
      }else{
        throw new ExistCatalogException(catalog);
      }
    }
  }

}
