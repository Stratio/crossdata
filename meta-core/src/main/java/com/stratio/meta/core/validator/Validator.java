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
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.QueryStatus;
import org.apache.log4j.Logger;

public class Validator {

    private final Logger logger = Logger.getLogger(Validator.class);
    //private final Session session;

    private final MetadataManager _metadata;

    public Validator(Session session){
        //this.session = session;
        _metadata = new MetadataManager(session);
        _metadata.loadMetadata();
    }

    public MetaQuery validateQuery(MetaQuery metaQuery) {
        metaQuery.setResult(
                metaQuery.getStatement()
                        .validate(_metadata, metaQuery.getTargetKeyspace())
        );
        //System.out.println("Validation hasError: " + metaQuery.getResult().hasError());
        if(!metaQuery.hasError()) {
            metaQuery.setStatus(QueryStatus.VALIDATED);
        }
        return metaQuery;
    }
    
}
