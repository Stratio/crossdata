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

package com.stratio.meta.core.api;

import com.datastax.driver.core.Session;
import com.stratio.meta.common.ask.APICommand;
import com.stratio.meta.common.ask.Command;
import com.stratio.meta.common.result.ErrorResult;
import com.stratio.meta.common.result.MetadataResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.AbstractMetadataHelper;
import com.stratio.meta.core.metadata.CassandraMetadataHelper;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.streaming.api.IStratioStreamingAPI;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APIManager {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(APIManager.class);

  /**
   * Metadata manager.
   */
  private final MetadataManager metadata;

  /**
   * Metadata helper.
   */
  private final AbstractMetadataHelper helper;

  /**
   * Class constructor.
   * @param session Cassandra session used to retrieve the metadata.
   */
  public APIManager(Session session, IStratioStreamingAPI stratioStreamingAPI){
    metadata = new MetadataManager(session, stratioStreamingAPI);
    metadata.loadMetadata();
    helper = new CassandraMetadataHelper();
  }
  /**
   * Process an incoming API request.
   *
   * @param cmd The commnand to be executed.
   * @return A {@link com.stratio.meta.common.result.MetadataResult}.
   */
  public Result processRequest(Command cmd) {
    Result result = null;
    if (APICommand.LIST_CATALOGS().equals(cmd.commandType())) {
      LOG.info("Processing " + APICommand.LIST_CATALOGS().toString());
      result = MetadataResult.createSuccessMetadataResult();
      MetadataResult.class.cast(result).setCatalogList(metadata.getCatalogsNames());
    } else if (APICommand.LIST_TABLES().equals(cmd.commandType())) {
      LOG.info("Processing " + APICommand.LIST_TABLES().toString());
      CatalogMetadata catalogMetadata = metadata.getCatalogMetadata(cmd.params().get(0));
      if (catalogMetadata != null) {
        result = MetadataResult.createSuccessMetadataResult();
        Map<String, TableMetadata> tableList = new HashMap<>();
        //Add db tables.
        tableList.putAll(helper.toCatalogMetadata(catalogMetadata).getTables());
        //Add ephemeral tables.
        //tableList.addAll(metadata.getEphemeralTables(cmd.params().get(0)));
        MetadataResult.class.cast(result).setTableList(new ArrayList(tableList.keySet()));
      } else {
        result =
            Result.createExecutionErrorResult("Catalog " + cmd.params().get(0) + " not found");
      }
    } else {
      result =
          Result.createExecutionErrorResult("Command " + cmd.commandType() + " not supported");
      LOG.error(ErrorResult.class.cast(result).getErrorMessage());
    }
    return result;
  }
}
