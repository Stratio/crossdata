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

package com.stratio.crossdata.common.manifest;

import java.io.Serializable;

public abstract class CrossdataManifest implements Serializable {

  /*
   * NOTE: Every time we create a new DataStoreType and a new ConnectorType with xjc, we have to check:
   * - both classes must extend CrossdataManifest
   * - update ManifestHelper if necessary
   * - the rest of created classes must implement Serializable
   * - Create some getters and setters
   */

    public static final int TYPE_DATASTORE = 1;
    public static final int TYPE_CONNECTOR = 2;
    private static final long serialVersionUID = -614600537779801491L;

    protected int manifestType;

    protected CrossdataManifest(int manifestType) {
        this.manifestType = manifestType;
    }

    public int getManifestType() {
        return manifestType;
    }

    @Override
    public String toString() {
        return ManifestHelper.manifestToString(this);
    }
}
