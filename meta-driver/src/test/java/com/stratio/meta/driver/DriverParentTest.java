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

package com.stratio.meta.driver;


import com.stratio.meta.common.result.ErrorResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.server.MetaServer;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

public class DriverParentTest extends ParentCassandraTest {

  private static final long SLEEP_TIME = 5000;

  private final static Logger logger = Logger.getLogger(DriverParentTest.class);

  protected static BasicDriver driver = null;

  protected static MetaServer metaServer = null;

  @BeforeClass
  public void init() {
    logger.info("INIT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    if (metaServer == null) {
      metaServer = new MetaServer();
      metaServer.init(null);
      metaServer.start();

      try {
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      driver = new BasicDriver();
      try {
        driver.connect("TEST_USER");
      }catch (Exception e){
        e.printStackTrace();
        driver = null;
        finish();
      }
    }
  }

  // @AfterClass(alwaysRun=true)
  @AfterSuite
  public void finish() {
    logger.info("FINISHING ------------------------------");
    driver.close();
    metaServer.stop();
    metaServer.destroy();
    logger.info("FINISH <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
  }

  public static String getErrorMessage(Result metaResult){
    String result = "Invalid class: " + metaResult.getClass();
    if(ErrorResult.class.isInstance(metaResult)){
      result = ErrorResult.class.cast(metaResult).getErrorMessage();
    }
    return result;
  }

}
