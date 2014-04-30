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

package com.stratio.meta.core.planner;

import com.datastax.driver.core.Session;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.QueryStatus;

public class Planner {

    private final MetadataManager metadata;

    public Planner(Session session){
        metadata = new MetadataManager(session);
        metadata.loadMetadata();
    }

    public MetaQuery planQuery(MetaQuery metaQuery) {
        metaQuery.setStatus(QueryStatus.PLANNED);
        metaQuery.setPlan(metaQuery.getStatement().getPlan(metadata, metaQuery.getSessionKeyspace()));
        return metaQuery;
    }
    
}
