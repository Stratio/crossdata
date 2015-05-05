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
package com.stratio.crossdata.common.utils;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import org.testng.Assert;
import org.testng.annotations.Test;

import difflib.Patch;
import difflib.PatchFailedException;

public class StringUtilsTest {
    @Test
    public void testObjectDiff() throws PatchFailedException, IOException {
        java.util.ArrayList<String> a=new java.util.ArrayList<String>();
        java.util.ArrayList<String> b=new java.util.ArrayList<String>();
        a.add("hola1hola2");
        a.add("hola3hola5");
        b.add("hola1hola2");
        b.add("hola2hola4");
        
        Patch diff = StringUtils.objectDiff(a, b); //get diff of Serialized objects

        String jsonresult = StringUtils.patchObject(a, diff); // patch object

        java.util.ArrayList<String> result= //deserialize
                (ArrayList<String>) StringUtils.deserializeObjectFromString(jsonresult,ArrayList.class);
        
        //test
        assertTrue(result.equals(b), result + "should be equals to " + b);
    }

    @Test
    public void testStringUtils() throws PatchFailedException, IOException {
        String a=StringUtils.getAkkaActorRefUri("akka.tcp://app@otherhost:1234/user/serviceB",true);
        String b=StringUtils.getAkkaActorRefUri("akka://app@otherhost:1234/user/akka" +
                ".tcp://app@otherhost:1234/user/serviceB",true);
        String c=StringUtils.getAkkaActorRefUri("/user/receptionist/akka" +
                ".tcp://app@otherhost:1234/user/serviceB",true);
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
    }

    @Test
    public void testConvertJavaTypeToXdType(){

        Assert.assertEquals(new ColumnType(DataType.BIGINT), StringUtils.convertJavaTypeToXdType("Long"));
        Assert.assertEquals(new ColumnType(DataType.BOOLEAN), StringUtils.convertJavaTypeToXdType("Boolean"));
        Assert.assertEquals(new ColumnType(DataType.DOUBLE), StringUtils.convertJavaTypeToXdType("Double"));
        Assert.assertEquals(new ColumnType(DataType.DOUBLE), StringUtils.convertJavaTypeToXdType("Float"));
        Assert.assertEquals(new ColumnType(DataType.INT), StringUtils.convertJavaTypeToXdType("Integer"));
        Assert.assertEquals(new ColumnType(DataType.TEXT), StringUtils.convertJavaTypeToXdType("String"));
        Assert.assertEquals(new ColumnType(DataType.SET), StringUtils.convertJavaTypeToXdType("Set"));
        Assert.assertEquals(new ColumnType(DataType.LIST), StringUtils.convertJavaTypeToXdType("List"));
        Assert.assertEquals(new ColumnType(DataType.MAP), StringUtils.convertJavaTypeToXdType("Map"));

        ColumnType nativeType = new ColumnType(DataType.NATIVE);
        nativeType.setDbType("NATIVE");
        nativeType.setODBCType("NATIVE");
        Assert.assertEquals(nativeType, StringUtils.convertJavaTypeToXdType("NATIVE"));
    }

    @Test
    public void testConvertJsonToMap(){

        String json = "{'name':'cool name', 'booleanValue':true, 'intValue':42, 'doubleValue':3.1415}";
        Map<String, Object> result = StringUtils.convertJsonToMap(json);

        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("name", "cool name");
        expected.put("booleanValue", true);
        expected.put("intValue", 42);
        expected.put("doubleValue", 3.1415);


        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConvertXdTypeToColumnType(){
        Assert.assertEquals(new ColumnType(DataType.BIGINT), StringUtils.convertXdTypeToColumnType("BigInt"));
        Assert.assertEquals(new ColumnType(DataType.BOOLEAN), StringUtils.convertXdTypeToColumnType("Bool"));
        Assert.assertEquals(new ColumnType(DataType.DOUBLE), StringUtils.convertXdTypeToColumnType("Double"));
        Assert.assertEquals(new ColumnType(DataType.FLOAT), StringUtils.convertXdTypeToColumnType("Float"));
        Assert.assertEquals(new ColumnType(DataType.INT), StringUtils.convertXdTypeToColumnType("Integer"));
        Assert.assertEquals(new ColumnType(DataType.TEXT), StringUtils.convertXdTypeToColumnType("Text"));
        Assert.assertEquals(new ColumnType(DataType.VARCHAR), StringUtils.convertXdTypeToColumnType("Varchar"));
        Assert.assertEquals(new ColumnType(DataType.SET), StringUtils.convertXdTypeToColumnType("Set"));
        Assert.assertEquals(new ColumnType(DataType.LIST), StringUtils.convertXdTypeToColumnType("List"));
        Assert.assertEquals(new ColumnType(DataType.MAP), StringUtils.convertXdTypeToColumnType("Map"));

    }

}
