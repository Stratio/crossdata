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

package com.stratio.meta.core.validator;

import com.datastax.driver.core.Session;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.streaming.api.IStratioStreamingAPI;


public class Validator {

  private final MetadataManager metadata;
  private final EngineConfig config;

  public Validator(Session session, IStratioStreamingAPI stratioStreamingAPI, EngineConfig config){
    metadata = new MetadataManager(session, stratioStreamingAPI);
    metadata.loadMetadata();
    this.config = config;
  }

  public MetaQuery validateQuery(MetaQuery metaQuery) {
    //TODO: Implement metadata invalidation messages between servers.
    metadata.loadMetadata();
    metaQuery.setResult(metaQuery.getStatement().validate(metadata, config));
    if(!metaQuery.hasError()) {
      metaQuery.setStatus(QueryStatus.VALIDATED);
    }
    return metaQuery;
  }

}
