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

package com.stratio.crossdata.driver;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ConnectResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.api.datastore.DataStoreType;

public class ConnectTest extends DriverParentTest {

    private final static Logger logger = Logger.getLogger(ConnectTest.class);


    @Test(groups = "connect")
    public void connect() {

        Result metaResult = null;
        try {
            metaResult = driver.connect("TEST_USER");
        } catch (ConnectionException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
        assertFalse(metaResult.hasError());
        ConnectResult r = ConnectResult.class.cast(metaResult);
        assertTrue(r.getSessionId() != null, "Invalid session identifier: " + r.getSessionId());

    }

   
}
