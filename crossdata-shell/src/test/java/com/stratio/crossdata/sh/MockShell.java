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

import java.util.UUID;
import com.stratio.crossdata.driver.DriverConnection;

/**
 * Mock shell which provide a driver connection without connecting to servers.
 */
public class MockShell extends Shell{
    public MockShell(boolean useAync){
        super(useAync);
    }

    @Override
    public boolean connect(String user, String pass) {
        this.xdConnection = new DriverConnection(UUID.randomUUID().toString(), "XD_TEST" , crossdataDriver);
        return true;
    }

}
