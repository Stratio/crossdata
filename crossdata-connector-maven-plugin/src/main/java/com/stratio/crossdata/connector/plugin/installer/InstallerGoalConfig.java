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
import java.util.List;

public class InstallerGoalConfig {
    private final String outputDirectory;
    private final String configDirectory;
    private final String includeDirectory;
    private final String connectorName;
    private final String description;
    private final String userService;
    private final String mainClass;
    private final File mainJarRepo;
    private final List<File> dependenciesJarRepo;
    private final String unixScriptTemplate;
    private final String javaOpts;
    private final String jmxPort;
    private boolean useCallingUserAsService;
    private final String pidFileName;

    public InstallerGoalConfig(String outputDirectory, String configDirectory, String includeDirectory,
            String connectorName, String description, String userService, String mainClass,
            String javaOpts,
            String jmxPort, File mainJarRepo,
            List<File> dependenciesJarRepo, String unixScriptTemplate, boolean useCallingUserAsService,
            String pidFileName) {
        this.outputDirectory = outputDirectory;
        this.configDirectory = configDirectory;
        this.includeDirectory = includeDirectory;
        this.connectorName = connectorName;
        this.description = description;
        this.userService = userService;
        this.mainClass = mainClass;
        this.javaOpts = javaOpts;
        this.jmxPort = jmxPort;
        this.mainJarRepo = mainJarRepo;
        this.dependenciesJarRepo = dependenciesJarRepo;
        this.unixScriptTemplate = unixScriptTemplate;
        this.useCallingUserAsService = useCallingUserAsService;
        this.pidFileName = pidFileName;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getConfigDirectory() {
        return configDirectory;
    }

    public String getIncludeDirectory() {
        return includeDirectory;
    }

    public String getConnectorName() {
        return connectorName;
    }

    public String getUserService() {
        return userService;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getJavaOpts() {
        return javaOpts;
    }
    public String getJmxPort() {
        return jmxPort;
    }

    public File getMainJarRepo() {
        return mainJarRepo;
    }

    public List<File> getDependenciesJarRepo() {
        return dependenciesJarRepo;
    }

    public String getDescription() {
        return description;
    }

    public String getUnixScriptTemplate() {
        return unixScriptTemplate;
    }

    public boolean isUseCallingUserAsService() {
        return useCallingUserAsService;
    }

    public String getPidFileName() {
        return pidFileName;
    }
}
