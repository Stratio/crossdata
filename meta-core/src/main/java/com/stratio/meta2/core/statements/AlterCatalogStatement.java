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

package com.stratio.meta2.core.statements;

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.validator.ValidationRequirements;

import java.util.Map;

/**
 * Class that models an {@code ALTER Catalog} statement from the META language.
 */
public class AlterCatalogStatement extends MetaStatement {

  /**
   * A JSON with the options specified by the user.
   */
  private final String options;

  /**
   * The map of properties of the Catalog. The different options accepted by a Catalog are
   * determined by the selected {@link com.datastax.driver.core.ReplicationStrategy}.
   */
  private Map<Selector, Selector> properties;

  /**
   * Class constructor.
   *
   * @param catalogName The name of the catalog.
   * @param options     A JSON with the storage options.
   */
  public AlterCatalogStatement(String catalogName, String options) {
    this.command = false;
    this.catalog = catalogName;
    this.catalogInc = true;
    this.options = options;
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
      CatalogMetadata ksMetadata = metadata.getCatalogMetadata(catalog);
      if (ksMetadata == null) {
        result = Result.createValidationErrorResult("Catalog " + catalog + " not found.");
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
  public ValidationRequirements getValidationRequirements() {
    return new ValidationRequirements();
  }

}
