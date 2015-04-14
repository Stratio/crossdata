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

package com.stratio.crossdata.connector.plugin.installer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.logging.Log;

public class InstallerGoalLauncher {



    public static void launchInstallerGoal(InstallerGoalConfig config, Log log) throws IOException {

        log.info("Create TARGET directory.");
        File targetDirectory = new File(FilenameUtils.concat(config.getOutputDirectory(), config.getConnectorName()));
        if (targetDirectory.exists()) {
            log.info("Remove previous TARGET directory");
            FileUtils.forceDelete(targetDirectory);
        }
        File includeConfigDirectory = new File(config.getIncludeDirectory());
        FileUtils.copyDirectory(includeConfigDirectory,targetDirectory);

        log.info("Create LIB directory.");
        File libDirectory = new File(FilenameUtils.concat(targetDirectory.toString(), "lib"));
        FileUtils.copyFileToDirectory(config.getMainJarRepo(),libDirectory);
        for(File jarFile:config.getDependenciesJarRepo()){
            FileUtils.copyFileToDirectory(jarFile,libDirectory);
        }

        log.info("Create CONF directory.");
        File confDirectory = new File(FilenameUtils.concat(targetDirectory.toString(), "conf"));
        File confConfigDirectory= new File(config.getConfigDirectory());
        FileUtils.copyDirectory(confConfigDirectory,confDirectory);

        log.info("Create BIN Directory");
        File binDirectory = new File(FilenameUtils.concat(targetDirectory.toString(), "bin"));
        FileUtils.forceMkdir(binDirectory);

        log.info("Launch template");
        String outputString=(config.getUnixScriptTemplate());
        outputString=outputString.replaceAll("<name>", config.getConnectorName());
        outputString=outputString.replaceAll("<desc>", config.getDescription());
        String user = config.getUserService();
        if(config.isUseCallingUserAsService()){
            user = System.getProperty("user.name");
        }
        outputString=outputString.replaceAll("<user>", user);
        outputString=outputString.replaceAll("<mainClass>", config.getMainClass());
        int index = config.getMainClass().lastIndexOf('.');
        String className = config.getMainClass();
        if(index != -1){
            className = config.getMainClass().substring(index+1);
        }
        outputString=outputString.replaceAll("<mainClassName>", className);

        outputString=outputString.replaceAll("<javaOpts>", config.getJavaOpts());
        outputString=outputString.replaceAll("<jmxPort>", config.getJmxPort());
        String pidFileName = "";
        if(config.getPidFileName() != null){
            pidFileName = config.getPidFileName();
        }
        outputString=outputString.replaceAll("<pidFileName>", pidFileName);

        File binFile= new File(FilenameUtils.concat(binDirectory.toString(),config.getConnectorName()));
        FileUtils.writeStringToFile(binFile,outputString);
        if(!binFile.setExecutable(true)){
            throw new IOException("Can't change executable option.");
        }

        log.info("Process complete: " + targetDirectory);
    }

}
