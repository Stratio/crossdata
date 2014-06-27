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
