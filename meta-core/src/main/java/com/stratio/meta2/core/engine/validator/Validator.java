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

import com.stratio.meta2.common.exception.validation.NotMustExistCatalogException;
import com.stratio.meta2.common.exception.validation.ValidationException;
import com.stratio.meta2.core.metadata.MetadataManager;
import org.apache.log4j.Logger;


import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.query.ValidatedQuery;
import com.stratio.meta2.core.statements.IStatement;

public class Validator {
  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Validator.class);

  public ValidatedQuery validate(ParsedQuery parsedQuery) throws ValidationException {
    this.validate(parsedQuery.getStatement());
    return new ValidatedQuery(parsedQuery);
  }

  private void validate(IStatement statement){

  }

  private void validate(Validation requirement, IStatement statement) throws ValidationException  {
    switch (requirement) {
      case MUST_NOT_EXIST_CATALOG:
        validateNotExistCatalog(statement.getCatalogs(), statement.getIfNotExists());
        break;
    }
  }

  private void validateNotExistCatalog(List<String> catalogs, boolean hasIfNotExist) throws NotMustExistCatalogException {
    for(String catalog:catalogs){
      this.validateNotExistCatalog(catalog,hasIfNotExist);
    }
  }


  private void validateNotExistCatalog(String catalog, boolean onlyIfNotExis) throws NotMustExistCatalogException{
    if(MetadataManager.MANAGER.existsCatalog(catalog)){
      if(!onlyIfNotExis){
        throw new NotMustExistCatalogException(catalog);
      }
    }
  }

}
