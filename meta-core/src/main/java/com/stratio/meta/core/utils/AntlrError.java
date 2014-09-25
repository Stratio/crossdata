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

public class AntlrError {

    private final String header;
    private final String message;

    public AntlrError(String header, String message) {
        this.header = header;
        this.message = message;
    }

    public String getHeader() {
        return header;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\tError recognized: ");
        sb.append(header).append(": ").append(message);
        return sb.toString();
    }

    public String toStringWithTokenTranslation() {
        StringBuilder sb = new StringBuilder("\tError recognized: ");
        sb.append(header).append(": ");
        sb.append(ParserUtils.translateToken(message));
        return sb.toString();
    }

}
