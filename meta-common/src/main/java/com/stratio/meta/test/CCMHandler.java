/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.test;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;

public class CCMHandler {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(CCMHandler.class);

    /**
     * Start a test Cassandra cluster to execute the unit tests. The method creates a
     * temporal file with the contents of {@code /com/stratio/meta/test/test.sh} and proceeds
     * with its execution.
     */
    public static void StartCCM(){
        BufferedReader in = null;
        try {
            File tempFile = File.createTempFile("stratio-start-ccm",".sh");
            InputStream resourceStream = CCMHandler.class.getResourceAsStream("/com/stratio/meta/test/test.sh");
            FileUtils.copyInputStreamToFile(resourceStream,tempFile);
            tempFile.setExecutable(true);

            Process p = Runtime.getRuntime().exec(tempFile.getAbsolutePath());

            in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            FileUtils.forceDeleteOnExit(tempFile);

        } catch (IOException e) {
            logger.error("Error starting CCM", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("IO exception closing ccm output.", e);
                e.printStackTrace();
            }
        }
    }

    public static void FinishCCM(){
        /*
        try {
            File tempFile= File.createTempFile("stratio-close-ccm",".sh");
            InputStream resourceStream = CCMHandler.class.getResourceAsStream("/com/stratio/meta/test/close.sh");
            FileUtils.copyInputStreamToFile(resourceStream,tempFile);
            tempFile.setExecutable(true);

            Process p = Runtime.getRuntime().exec(tempFile.getAbsolutePath());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            FileUtils.forceDeleteOnExit(tempFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
