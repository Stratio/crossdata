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
import java.util.Map;
import java.util.Set;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.validation.ExistNameException;
import com.stratio.meta.common.exceptions.validation.NotExistNameException;
import com.stratio.meta2.common.api.Manifest;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.Name;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.MetaDataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.query.ValidatedQuery;
import org.apache.log4j.Logger;


public class Validator {
  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Validator.class);

  public ValidatedQuery validate(ParsedQuery parsedQuery)
      throws ExistNameException, IgnoreQueryException, NotExistNameException {
      ValidatedQuery validatedQuery=null;
      MetaDataParsedQuery metaDataParsedQuery=null;

      for (Validation val:parsedQuery.getStatement().getValidationRequirements().getValidations()) {
          switch (val) {
              case MUST_NOT_EXIST_CATALOG:
                  validateNotExistCatalog(
                      new CatalogName(parsedQuery.getStatement().getEffectiveCatalog()), true);
                  break;
              case MUST_EXIST_CATALOG:
                  validateExist(new CatalogName(parsedQuery.getStatement().getEffectiveCatalog()),true);
                  break;
              case MUST_EXIST_TABLE:
                   validateExist(parsedQuery.getStatement().getFromTables(),true);
                  break;
              case MUST_NOT_EXIST_TABLE:
                  validateNotExist(parsedQuery.getStatement().getFromTables(),true);
                  break;
              case MUST_NOT_EXIST_CLUSTER:
                  metaDataParsedQuery=(MetaDataParsedQuery)parsedQuery;
                  validateNotExist(
                      metaDataParsedQuery.getStatement().getClusterMetadata().getName(), true);
                  break;
              case MUST_EXIST_CLUSTER:
                  metaDataParsedQuery=(MetaDataParsedQuery)parsedQuery;
                  validateExist(metaDataParsedQuery.getStatement().getClusterMetadata().getName(),
                      true);
                  break;
              case MUST_EXIST_CONNECTOR:
                  metaDataParsedQuery=(MetaDataParsedQuery)parsedQuery;
                  validateExist(metaDataParsedQuery.getStatement().getConnectorMetadata().getName(),true);
                  break;
              case MUST_NOT_EXIST_CONNECTOR:
                  metaDataParsedQuery=(MetaDataParsedQuery)parsedQuery;
                  validateNotExist(metaDataParsedQuery.getStatement().getConnectorMetadata().getName(),true);
                  break;
              case MUST_EXIST_DATASTORE:
                  metaDataParsedQuery=(MetaDataParsedQuery)parsedQuery;
                  validateExist(metaDataParsedQuery.getStatement().getDataStoreMetadata().getName(),true);
                  break;
              case MUST_NOT_EXIST_DATASTORE:
                  metaDataParsedQuery=(MetaDataParsedQuery)parsedQuery;
                  validateNotExist(
                      metaDataParsedQuery.getStatement().getDataStoreMetadata().getName(), true);
                  break;
              case VALID_DATASTORE_MANIFEST:
                  //TODO Manifest is an API call with a previous validation
                  Manifest datastoreManifest=null;
                  validateManifest(datastoreManifest);
                  break;
              case VALID_CONNECTOR_MANIFEST:
                  //TODO Manifest is an API call with a previous validation
                  Manifest connectorManifest=null;
                  validateManifest(connectorManifest);
                  break;
              case VALID_CLUSTER_OPTIONS:
                  //TODO What can we validate if it is a Set<String>?
                  metaDataParsedQuery=(MetaDataParsedQuery)parsedQuery;
                  Set<String> dataStoreOtherProperties=metaDataParsedQuery.getStatement().getDataStoreMetadata().getOthersProperties();
                  Set<String> dataStoreRequiredProperties=metaDataParsedQuery.getStatement().getDataStoreMetadata().getRequiredProperties();
                  validateProperties(dataStoreOtherProperties);
                  validateProperties(dataStoreRequiredProperties);
                  break;
              case VALID_CONNECTOR_OPTIONS:
                  //TODO What can we validate if it is a Set<String>?
                  metaDataParsedQuery=(MetaDataParsedQuery)parsedQuery;
                  Set<String> connectorOtherProperties=metaDataParsedQuery.getStatement().getConnectorMetadata().getOthersProperties();
                  Set<String> connectorRequiredProperties=metaDataParsedQuery.getStatement().getConnectorMetadata().getRequiredProperties();
                  validateProperties(connectorOtherProperties);
                  validateProperties(connectorRequiredProperties);
                  break;
              case MUST_EXIST_ATTACH_CONNECTOR_CLUSTER:
                  break;
              default:
                  break;

          }
      }
      //validatedQuery=(ValidatedQuery)parsedQuery;
      return validatedQuery;
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

  private void validateNotExist(Name name, boolean hasIfExists)
      throws ExistNameException, IgnoreQueryException {
    if(MetadataManager.MANAGER.exists(name)){
      if(hasIfExists) {
         throw new IgnoreQueryException("["+ name + "] doesn't exist");
      }else{
         throw new ExistNameException(name);
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

  private void validateManifest(Manifest manifest){

  }

  private void validateProperties(Set<String> properties){

  }



}
