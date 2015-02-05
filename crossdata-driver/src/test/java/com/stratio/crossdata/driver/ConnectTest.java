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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.result.ConnectResult;
import com.stratio.crossdata.common.result.Result;

public class ConnectTest extends DriverParentTest {

    private final static Logger logger = Logger.getLogger(ConnectTest.class);

    //@Test
    public void connect() {

        Result crossDataResult = null;
        try {
            crossDataResult = driver.connect("TEST_USER");
        } catch (ConnectionException e) {
            fail("Exception not expected: " + System.lineSeparator() + e.getMessage());
            driver.close();
        }
        assertFalse(crossDataResult.hasError());
        ConnectResult r = ConnectResult.class.cast(crossDataResult);
        assertTrue(r.getSessionId() != null, "Invalid session identifier: " + r.getSessionId());

    }

}
