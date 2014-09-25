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

package com.stratio.meta.sh.help;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class HelpManager {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(HelpManager.class);

    /**
     * Path of the file with the help contents.
     */
    private static final String HELP_PATH = "com/stratio/meta/sh/help/MetaClientHelp.yaml";

    /**
     * Load the help contents by reading the contents of {@code HELP_PATH}.
     *
     * @return A {@link com.stratio.meta.sh.help.HelpContent} with the help.
     */
    public HelpContent loadHelpContent() {
        HelpContent result = null;
        InputStream is = HelpManager.class.getClassLoader().getResourceAsStream(HELP_PATH);
        try {
            Constructor constructor = new Constructor(HelpContent.class);
            Yaml yaml = new Yaml(constructor);
            result = yaml.loadAs(is, HelpContent.class);
            result.loadMap();
        } catch (Exception e) {
            LOG.error("Cannot read help file", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LOG.error("Cannot close help file", e);
            }
        }
        return result;
    }
}
