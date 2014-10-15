package com.stratio.meta.connector.plugin.installer;/*
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;


@Mojo(
        name = "install",
        requiresDependencyResolution = ResolutionScope.RUNTIME
)
@Execute(
        phase = LifecyclePhase.INSTALL
)
public class InstallerGoalMojo extends AbstractMojo {

    private final String UNIX_SCRIPT_REFERNCE= "/UnixScripTemplate.st";

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Component
    private ArtifactResolver resolver;

    @Parameter(defaultValue = "${project.artifact}", readonly = true)
    private Artifact projectArtifact;

    @Parameter(defaultValue = "${localRepository}")
    private ArtifactRepository localRepository;

    @Parameter(defaultValue = "${project.remoteArtifactRepositories}")
    private List remoteRepositories;

    @Parameter(
            name = "outputDirectory",
            defaultValue = "${project.build.directory}"
    )
    private String outputDirectory;

    @Parameter(
            name = "configDirectory",
            defaultValue = "${project.basedir}/src/main/config"
    )
    private String configDirectory;

    @Parameter(
            name = "includeDirectory",
            defaultValue = "${project.basedir}/src/main/include"
    )
    private String includeDirectory;

    @Parameter(
            name = "connectorName",
            defaultValue = "${project.artifactId}-${project.version}"
    )
    private String connectorName;

    @Parameter(
            name = "description",
            defaultValue = "Description for ${project.artifactId}-${project.version}"
    )
    private String description;

    @Parameter(
            name = "userService",
            defaultValue = "root"
    )
    private String userService;

    @Parameter(
            name = "mainClass",
            required = true
    )
    private String mainClass;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File mainJarRepo = this.resolveProjectArtifact();
        List<File> dependenciesJarRepo = this.resolveTransitiveDependecies();
        String unixScriptTemplate= readStream(getClass().getResourceAsStream(UNIX_SCRIPT_REFERNCE));


        InstallerGoalConfig config = new InstallerGoalConfig(this.outputDirectory, this.configDirectory,
                this.includeDirectory, this.connectorName, this.description, this.userService, this.mainClass,
                mainJarRepo, dependenciesJarRepo, unixScriptTemplate);
        try {
            InstallerGoalLauncher.launchInstallerGoal(config,getLog());
        } catch (IOException e) {
            throw  new MojoExecutionException("Problem with Launcher: " + e.getMessage(),e.getCause());
        }
    }

    public static String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder(512);
        try {
            Reader r = new InputStreamReader(is, "UTF-8");
            int c = 0;
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    private File resolveProjectArtifact() throws MojoFailureException {
        getLog().info("Recovering project JAR");
        try {
            resolver.resolve(projectArtifact, project.getRemoteArtifactRepositories(), localRepository);
            getLog().debug(new StringBuilder().append("Resolve: ").append(projectArtifact.getArtifactId())
                    .append("PATH: ").append(projectArtifact.getFile()));
        } catch (ArtifactResolutionException | ArtifactNotFoundException e) {
            throw new MojoFailureException("Not resolver main project JAR", e.getCause());
        }
        return projectArtifact.getFile();
    }

    private List<File> resolveTransitiveDependecies() throws MojoFailureException {
        List<File> result = new ArrayList<>();
        Set artifacts = project.getArtifacts();
        for (Object anArtifactObj : artifacts) {
            Artifact artifact = (Artifact) anArtifactObj;
            if (artifact.getFile() == null) {
                throw new MojoFailureException("Can't resolve all dependencies");
            }
            result.add(artifact.getFile());
            getLog().debug(new StringBuilder().append("Resolve: ").append(artifact.getArtifactId())
                    .append("PATH: ").append(artifact.getFile()));
        }
        return result;
    }
}
