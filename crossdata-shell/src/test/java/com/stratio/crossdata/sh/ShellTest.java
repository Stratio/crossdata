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

import org.testng.Assert;
import org.testng.annotations.Test;

public class ShellTest {

    @Test
    public void testShellConnectWithoutServer()  {
        Shell crossDatash = new Shell(false);
        boolean result=crossDatash.connect();
        Assert.assertFalse(result);
    }

    @Test
    public void testShellDisConnectWithoutServer()  {
        Shell crossDatash = new Shell(false);
        try {
            crossDatash.closeConsole();
            Assert.assertTrue(true);
        }catch (Exception e){
            Assert.fail("An error happened in sh");
        }
    }

    @Test
    public void testPrinln()  {
        Shell crossDatash = new Shell(false);
        try {
            crossDatash.println("prueba");
            Assert.assertTrue(true);
        }catch (Exception e){
            Assert.fail("An error happened in sh");
        }
    }

    @Test
    public void testSendManifest()  {
        Shell crossDatash = new Shell(false);
        try {
            crossDatash.sendManifest("ADD DATASTORE 'com/stratio/crossdata/connector/DataStoreDefinition.xml'");
            Assert.assertTrue(true);
        }catch (Exception e){
            Assert.fail("An error happened in sh");
        }
    }




}
