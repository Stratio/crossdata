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

package com.stratio.crossdata.core.api;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.ask.APICommand;
import com.stratio.crossdata.common.ask.Command;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.api.PropertiesType;
import com.stratio.crossdata.common.api.PropertyType;
import com.stratio.crossdata.common.api.datastore.DataStoreType;
import com.stratio.crossdata.core.metadata.MetadataManagerTestHelper;

public class APIManagerTest extends MetadataManagerTestHelper {

    @Test
    public void testProcessRequest() throws Exception {
        APIManager apiMangager = new APIManager();
        List params = new ArrayList<DataStoreType>();
        DataStoreType dataStoreType = new DataStoreType();
        dataStoreType.setName("CassandraDataStore");
        dataStoreType.setVersion("1.0");

        PropertiesType propertiesType = new PropertiesType();
        PropertyType prop = new PropertyType();
        prop.setPropertyName("DefaultLimit");
        prop.setDescription("Description");
        List<PropertyType> list=new ArrayList<>();
        list.add(prop);
        propertiesType.setProperty(list);
        dataStoreType.setRequiredProperties(propertiesType);

        params.add(dataStoreType);
        Command cmd = new Command(APICommand.ADD_MANIFEST(), params);
        Result result = apiMangager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.startsWith("CrossdataManifest added"));
    }
}
