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

package com.stratio.crossdata.sh;

import static org.testng.Assert.assertNotNull;

import java.net.URL;

import org.testng.annotations.Test;

public class CrossdatashTestIT {

    @Test(timeOut = 12000)
    public void testSendManifest() throws Exception {
        Shell xdsh = new Shell(false);
        xdsh.connect();
        URL url = Thread.currentThread().getContextClassLoader()
                .getResource("com/stratio/crossdata/connector/DataStoreDefinition.xml");
        String result = url.getPath();
        assertNotNull(result, "testSendManifest returns NULL");
    }

}
