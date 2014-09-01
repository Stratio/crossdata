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

package com.stratio.meta.test;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;

public class CCMHandler {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(CCMHandler.class);

    /**
     * Private class constructor as all methods are static.
     */
    private CCMHandler(){
    }

    /**
     * Start a test Cassandra cluster to execute the unit tests. The method creates a
     * temporal file with the contents of {@code /com/stratio/meta/test/test.sh} and proceeds
     * with its execution.
     */
    public static void startCCM(){
        BufferedReader in = null;
        try {
            File tempFile = File.createTempFile("stratio-start-ccm",".sh");
            InputStream resourceStream = CCMHandler.class.getResourceAsStream("/com/stratio/meta/test/test.sh");
            FileUtils.copyInputStreamToFile(resourceStream,tempFile);
            tempFile.setExecutable(true);

            Process p = Runtime.getRuntime().exec(tempFile.getAbsolutePath());

            in = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("UTF-8")));

            String line;
            while ((line = in.readLine()) != null) {
                LOG.debug(line);
            }
            FileUtils.forceDeleteOnExit(tempFile);

        } catch (IOException e) {
            LOG.error("Error starting CCM", e);
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error("IO exception closing ccm output.", e);
                }
            }
        }
    }

}
