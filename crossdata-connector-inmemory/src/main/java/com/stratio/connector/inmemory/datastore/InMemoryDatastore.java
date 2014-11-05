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

package com.stratio.connector.inmemory.datastore;

import org.apache.log4j.Logger;

/**
 * This class provides a proof-of-concept implementation of an in-memory datastore for
 * demonstration purposes.
 */
public class InMemoryDatastore {

    /**
     * Maximum number of rows per table.
     */
    private final int TABLE_ROW_LIMIT;

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(InMemoryDatastore.class);

    /**
     * Class constructor.
     * @param tableRowLimit The maximum number of rows per table.
     */
    public InMemoryDatastore(int tableRowLimit){
        TABLE_ROW_LIMIT = tableRowLimit;
        LOG.info("InMemoryDatastore created with row limit: " + TABLE_ROW_LIMIT);
    }



}
