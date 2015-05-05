/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.statements.structures;

import com.stratio.crossdata.common.data.ColumnName;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lcisneros on 5/05/15.
 */
public class SelectorHelperTest {


    @Test
    public void testConvertSelectorMapToStringMap(){

        Map<Selector, Selector> selectorsMap = new HashMap<>();
        ColumnSelector col = new ColumnSelector(new ColumnName("CatalogName", "TableName", "ColumnName"));
        StringSelector value = new StringSelector("Cool Value");
        selectorsMap.put(col, value);

        //Experimentation
        Map<String, String> stringsMap = SelectorHelper.convertSelectorMapToStringMap(selectorsMap);

        //Expectations
        Assert.assertEquals(stringsMap.get(col.getStringValue()), "Cool Value");
        Assert.assertEquals(col.getStringValue(), "CatalogName.TableName.ColumnName");

    }
}
