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

import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code CREATE CATALOG} statement from the META language. Catalog
 * information will be stored internally as part of the existing metadata. Catalog creation
 * in the underlying datastore is done when a table is created in a catalog.
 */
public class CreateCatalogStatement extends MetaStatement {

  /**
   * Whether the keyspace should be created only if it not exists.
   */
  private final boolean ifNotExists;

  /**
   * A JSON with the options specified by the user.
   */
  private final String options;

  /**
   * Class constructor.
   * @param catalogName The name of the catalog.
   * @param ifNotExists Whether it should be created only if it not exists.
   * @param JSON A JSON with the storage options.
   */
  public CreateCatalogStatement(String catalogName, boolean ifNotExists,
                                String JSON) {
    this.catalog = catalogName;
    this.catalogInc = true;
    this.command = false;
    this.ifNotExists = ifNotExists;
    this.options = JSON;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("CREATE CATALOG ");
    if(ifNotExists){
      sb.append("IF NOT EXISTS ");
    }
    sb.append(catalog);
    if(options != null) {
      sb.append(" WITH ").append(options);
    }
    return sb.toString();
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result = QueryResult.createSuccessQueryResult();
    if(catalog!= null && catalog.length() > 0) {
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(catalog);
      if(ksMetadata != null && !ifNotExists){
        result = Result.createValidationErrorResult("Keyspace " + catalog + " already exists.");
      }
    }else{
      result = Result.createValidationErrorResult("Empty catalog name found.");
    }

    //if(properties.isEmpty() || !properties.containsKey("replication")){
    //  result = Result.createValidationErrorResult("Missing mandatory replication property.");
    //}

    return result;
  }

  @Override
  public String translateToCQL() {
    String metaStr = this.toString();
    if(metaStr.contains("{")){
      return ParserUtils.translateLiteralsToCQL(metaStr);
    } else {
      return metaStr;
    }
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    // TODO Auto-generated method stub
    return null;
  }

}
