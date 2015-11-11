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

/**
 * Class that contains the information of help item for Crossdata.
 */
public class HelpEntry {

    /**
     * The entry.
     */
    private String entry = null;

    /**
     * The associated help.
     */
    private String help = null;

    /**
     * Get the entry.
     * @return The help entry.
     */
    public String getEntry() {
        return entry;
    }

    /**
     * Set the help entry.
     * @param entry The help topic entry.
     */
    public void setEntry(String entry) {
        this.entry = entry;
    }

    /**
     * Get the help associated with an entry.
     * @return A String.
     */
    public String getHelp() {
        return help;
    }

    /**
     * Set the help associated with an entry.
     * @param help The associated help.
     */
    public void setHelp(String help) {
        this.help = help;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(entry);
        sb.append(System.lineSeparator());
        sb.append(help);
        return sb.toString();
    }
}
