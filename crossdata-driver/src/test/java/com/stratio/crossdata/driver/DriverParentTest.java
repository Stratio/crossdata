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

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.server.CrossdataServer;

public class DriverParentTest {

    private static final long SLEEP_TIME = 5000;

    private final static Logger logger = Logger.getLogger(DriverParentTest.class);

    protected static BasicDriver driver = null;

    protected static CrossdataServer crossdataServer = null;

    public static String getErrorMessage(Result crossDataResult) {
        String result = "Invalid class: " + crossDataResult.getClass();
        if (ErrorResult.class.isInstance(crossDataResult)) {
            result = ErrorResult.class.cast(crossDataResult).getErrorMessage();
        }
        return result;
    }

    @BeforeClass
    public void init() {
        logger.info("INIT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (crossdataServer == null) {
            crossdataServer = new CrossdataServer();
            crossdataServer.init(null);
            crossdataServer.start();

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                logger.error(e);
            }

            driver = new BasicDriver();
            try {
                driver.connect("TEST_USER");
            } catch (Exception e) {
                logger.error(e);
                finish();
            }
        }
    }

    @AfterClass
    public void finish() {
        logger.info("FINISHING ------------------------------");
        driver.close();
        crossdataServer.stop();
        crossdataServer.destroy();
        logger.info("FINISH <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

}
