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

package com.stratio.meta2.core.validator;

import java.util.List;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.exceptions.validation.ExistNameException;
import com.stratio.meta.common.exceptions.validation.NotExistNameException;
import com.stratio.meta2.common.data.Name;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.NormalizedQuery;
import org.apache.log4j.Logger;


import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.query.ValidatedQuery;

public class Validator {
  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Validator.class);

  public ValidatedQuery validate(NormalizedQuery normalizedQuery)
      throws ValidationException, IgnoreQueryException {
    this.validateRequirements(normalizedQuery);
    return new ValidatedQuery(normalizedQuery) ;
  }



  private void validateRequirements(ParsedQuery parsedQuery)
      throws ValidationException, IgnoreQueryException {
    for(Validation req:parsedQuery.getValidationRequirements().getValidations()){
      validateRequirements(req,parsedQuery);
    }
  }
  private void validateRequirements(Validation requirement, ParsedQuery parsedQuery)
      throws ValidationException, IgnoreQueryException {
    switch (requirement) {
      case MUST_NOT_EXIST_CATALOG:
        validateNotExist(parsedQuery.getCatalogs(), parsedQuery.getIfNotExists());
        break;
      case MUST_EXIST_CATALOG:
        validateExist(parsedQuery.getCatalogs(), parsedQuery.getIfExists());
        break;
       case MUST_EXIST_TABLE:
         validateExist(parsedQuery.getTables(), parsedQuery.getIfExists());
         break;
      case MUST_NOT_EXIST_TABLE:
        validateNotExist(parsedQuery.getTables(), parsedQuery.getIfNotExists());
        break;
      case MUST_NOT_EXIST_CLUSTER:
        validateNotExist(parsedQuery.getClusters(),parsedQuery.getIfNotExists());
        break;
      case MUST_EXIST_CLUSTER:
        validateExist(parsedQuery.getClusters(), parsedQuery.getIfExists());
        break;
      case MUST_EXIST_CONNECTOR:
        validateExist(parsedQuery.getConnectors(),parsedQuery.getIfExists());
        break;
      case MUST_NOT_EXIST_CONNECTOR:
        validateNotExist(parsedQuery.getConnectors(),parsedQuery.getIfNotExists());
        break;
      case MUST_EXIST_DATASTORE:
        validateNotExist(parsedQuery.getDatastores(),parsedQuery.getIfExists());
        break;
      case MUST_NOT_EXIST_DATASTORE:
        validateNotExist(parsedQuery.getDatastores(),parsedQuery.getIfNotExists());
        break;
      case VALID_DATASTORE_MANIFEST:
        break;
      case VALID_CLUSTER_OPTIONS:
        break;
      case VALID_CONNECTOR_OPTIONS:
        break;
      case MUST_EXIST_ATTACH_CONNECTOR_CLUSTER:
        break;
      case VALID_CONNECTOR_MANIFEST:
        break;
    }
  }



  private void validateExist(List<? extends Name> names, boolean hasIfExists)
      throws IgnoreQueryException, NotExistNameException {
    for(Name name:names){
      this.validateExist(name, hasIfExists);
    }
  }



  private void validateExist(Name name, boolean hasIfExists)
      throws NotExistNameException, IgnoreQueryException {
    if(!MetadataManager.MANAGER.exists(name)){
      if(hasIfExists) {
        throw new IgnoreQueryException("["+ name + "] doesn't exist");
      }else{
        throw new NotExistNameException(name);
      }
    }
  }

  private void validateNotExist(List<? extends Name> names, boolean hasIfNotExist)
      throws ExistNameException, IgnoreQueryException {
    for(Name name:names){
      this.validateNotExistCatalog(name,hasIfNotExist);
    }
  }


  private void validateNotExistCatalog(Name name, boolean onlyIfNotExist)
      throws ExistNameException, IgnoreQueryException {
    if(MetadataManager.MANAGER.exists(name)){
      if(onlyIfNotExist) {
        throw new IgnoreQueryException("["+ name + "] exists");
      }else{
        throw new ExistNameException(name);
      }
    }
  }

}
