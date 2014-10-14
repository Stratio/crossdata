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

package com.stratio.meta.connector.plugin.installer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class InstallerGoalLauncher {

    public static void launchInstallerGoal(InstallerGoalConfig config) throws IOException {
        //Create TARGET Directory
        File targetDirectory = new File(FilenameUtils.concat(config.getConfigDirectory(), config.getConnectorName()));
        if (targetDirectory.exists()) {
            FileUtils.forceDelete(targetDirectory);
        }
        File includeConfigDirectory = new File(config.getIncludeDirectory());
        FileUtils.copyDirectory(includeConfigDirectory,targetDirectory);

        //Create LIB Directory
        File libDirectory = new File(FilenameUtils.concat(targetDirectory.toString(), "lib"));
        FileUtils.copyFileToDirectory(config.getMainJarRepo(),libDirectory);
        for(File jarFile:config.getDependenciesJarRepo()){
            FileUtils.copyFileToDirectory(jarFile,libDirectory);
        }

        //Create CONF Directory
        File confDirectory = new File(FilenameUtils.concat(targetDirectory.toString(), "conf"));
        File confConfigDirectory= new File(config.getConfigDirectory());
        FileUtils.copyDirectory(confConfigDirectory,confDirectory);

        //Create BIN Directory
        File binDirectory = new File(FilenameUtils.concat(targetDirectory.toString(), "bin"));
        FileUtils.forceMkdir(binDirectory);


    }

}
