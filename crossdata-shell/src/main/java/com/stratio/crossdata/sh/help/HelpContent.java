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

package com.stratio.crossdata.sh.help;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that contains the list of {@link HelpEntry} with the help contents.
 */
public class HelpContent {

    /**
     * The list of {@link HelpEntry}.
     */
    private List<HelpEntry> content;

    /**
     * A mapped view of the help entries.
     */
    private Map<HelpType, String> help;

    public List<HelpEntry> getContent() {
        return content;
    }

    public void setContent(List<HelpEntry> content) {
        this.content = content;
    }

    public Map<HelpType, String> getHelp() {
        return help;
    }

    public void setHelp(Map<HelpType, String> help) {
        this.help = help;
    }

    /**
     * Load the mapped view of the help contents.
     */
    public void loadMap() {
        help = new HashMap<>();
        for (HelpEntry e : content) {
            help.put(HelpType.valueOf(e.getEntry()), e.getHelp());
        }
    }

    /**
     * Retrieve the help associated with {@link HelpType}.
     *
     * @param type The requested {@link HelpType}
     * @return The help string or null if the help is not available.
     */
    public String searchHelp(HelpType type) {
        return help.get(type);
    }
}
