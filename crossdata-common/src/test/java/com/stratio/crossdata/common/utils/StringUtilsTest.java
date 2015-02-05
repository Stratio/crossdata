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


}
