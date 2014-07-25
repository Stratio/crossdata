/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta2.core.statements;

import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;
import com.stratio.meta2.core.statements.MetaStatement;

import java.util.HashMap;
import java.util.Map;

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
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
