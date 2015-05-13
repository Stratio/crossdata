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

package com.stratio.connector.inmemory.metadata;

import com.stratio.crossdata.common.connector.IMetadataListener;
import com.stratio.crossdata.common.data.Name;
import com.stratio.crossdata.common.metadata.UpdatableMetadata;
import org.apache.log4j.Logger;

/**
 * Logs the update and delete calls.
 */
public class MetadataListener implements IMetadataListener {


    private static final Logger LOG = Logger.getLogger(MetadataListener.class);
    private static final long serialVersionUID = -3053782853995677803L;

    @Override
    public void updateMetadata(UpdatableMetadata uMetadata) {
        LOG.info("RECEIVED UPDATE METADATA: "
                + System.lineSeparator()
                + uMetadata.toString());
    }

    @Override
    public void deleteMetadata(Name name) {
        LOG.info("RECEIVED DELETE METADATA: "
                + System.lineSeparator()
                + name.toString());
    }
}
