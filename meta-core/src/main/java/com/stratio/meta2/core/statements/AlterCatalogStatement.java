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

import java.util.Map;

/**
 * Class that models an {@code ALTER KEYSPACE} statement from the META language.
 */
public class AlterCatalogStatement extends MetaStatement {

  /**
   * A JSON with the options specified by the user.
   */
  private final String options;

  /**
   * The map of properties of the keyspace. The different options accepted by a keyspace are
   * determined by the selected {@link com.datastax.driver.core.ReplicationStrategy}.
   */
  private Map<String, ValueProperty> properties;

  /**
   * Class constructor.
   *
   * @param catalogName The name of the catalog.
   * @param JSON        A JSON with the storage options.
   */
  public AlterCatalogStatement(String catalogName, String JSON) {
    this.command = false;
    this.catalog = catalogName;
    this.catalogInc = true;
    this.options = JSON;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ALTER CATALOG ");
    sb.append(catalog);
    sb.append(" WITH ").append(this.options);
    return sb.toString();
  }

  @Override
  public String translateToCQL() {
    return this.toString();
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {

    Result result = QueryResult.createSuccessQueryResult();

    if (catalog != null && catalog.length() > 0) {
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(catalog);
      if (ksMetadata == null) {
        result = Result.createValidationErrorResult("Keyspace " + catalog + " not found.");
      }
    } else {
      result = Result.createValidationErrorResult("Empty catalog name found.");
    }

    if (properties.isEmpty() || (!properties.containsKey("replication")
                                 & !properties.containsKey("durable_writes"))) {
      result =
          Result.createValidationErrorResult(
              "At least one property must be included: 'replication' or 'durable_writes'.");
    }

    return result;
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
