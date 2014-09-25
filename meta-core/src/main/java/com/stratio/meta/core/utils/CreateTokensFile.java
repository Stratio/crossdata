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

package com.stratio.meta.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

public class CreateTokensFile {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(CreateTokensFile.class);

    /**
     * Private class constructor as all methods are static.
     */
    private CreateTokensFile() {
    }

    public static void main(String[] args) {
        String line = null;
        try {
            String workingDir = System.getProperty("user.dir");
            if (workingDir.endsWith("stratio-meta")) {
                workingDir = workingDir.concat("/meta-core/");
            } else if (!workingDir.endsWith("meta-core")) {
                workingDir = workingDir.substring(0, workingDir.lastIndexOf("/"));
                workingDir = workingDir.concat("/meta-core/");
            } else {
                workingDir = workingDir.concat("/");
            }

            String metaGrammarPath = workingDir + "src/main/java/com/stratio/meta/core/grammar/Meta.g";
            String metaTokens = workingDir + "src/main/resources/com/stratio/meta/parser/tokens.txt";
            File fileGrammar = new File(metaGrammarPath);
            File outFile = new File(metaTokens);

            LOG.info("Reading grammar from " + fileGrammar.getAbsolutePath());
            BufferedWriter bw;
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(fileGrammar), Charset.forName("UTF-8")))) {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "UTF-8"));
                line = br.readLine();
                while (line != null) {
                    if (line.startsWith("T_")) {
                        String token = line.substring(2, line.indexOf(":") + 2);
                        String replacement = line.substring(line.indexOf(":") + 1);
                        replacement = replacement.replace(";", "");
                        replacement = replacement.replace("'", "");
                        replacement = replacement.replace(" ", "");
                        replacement = "[#" + replacement + "#]";
                        bw.write(token + replacement);
                        bw.newLine();
                    }
                    line = br.readLine();
                }
            }
            bw.flush();
            bw.close();
            LOG.info(outFile.getAbsolutePath() + " created");
        } catch (IOException ex) {
            LOG.error("IOException creating token file", ex);
        } catch (StringIndexOutOfBoundsException ex) {
            LOG.error("line: '" + line + "' couldn't be parsed", ex);
        }
    }

}
