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
 * Wrapper class for help statements.
 */
public class HelpStatement {

    /**
     * The type of help requested.
     */
    private final HelpType type;

    /**
     * Class constructor.
     *
     * @param type The type of {@link com.stratio.crossdata.sh.help.HelpType} to be shown.
     */
    public HelpStatement(HelpType type) {
        this.type = type;
    }

    /**
     * Get the type of help.
     * @return A {@link com.stratio.crossdata.sh.help.HelpType}.
     */
    public HelpType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "HELP " + type;
    }
}
