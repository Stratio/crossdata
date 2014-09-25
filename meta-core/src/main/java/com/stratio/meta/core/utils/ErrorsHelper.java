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

import java.util.ArrayList;
import java.util.List;

public class ErrorsHelper {

    private String message;

    private List<AntlrError> antlrErrors = new ArrayList<>();

    public static String parseAntlrErrorToString(AntlrError ae, String query) {
        StringBuilder sb = new StringBuilder("Parser exception: ");
        sb.append(System.lineSeparator());
        sb.append(ae.toStringWithTokenTranslation()).append(System.lineSeparator());
        sb.append("\t").append(ParserUtils.getQueryWithSign(query, ae));
        if (!"".equalsIgnoreCase(query)) {
            sb.append(System.lineSeparator()).append("\t");
            sb.append(ParserUtils.getSuggestion(query, ae));
        }
        return sb.toString();
    }

    public void addError(AntlrError antlrError) {
        antlrErrors.add(antlrError);
    }

    public boolean isEmpty() {
        return antlrErrors.isEmpty();
    }

    public List<AntlrError> getAntlrErrors() {
        return antlrErrors;
    }

    public String toString(String query) {
        String result = "Parser exception: ";
        if (!antlrErrors.isEmpty()) {
            AntlrError ae = antlrErrors.get(0);
            result = parseAntlrErrorToString(ae, query);
        }
        return result;
    }

    public List<String> getListErrors(String query) {
        List<String> results = new ArrayList<>();
        if (antlrErrors != null && !antlrErrors.isEmpty()) {
            for (AntlrError error : antlrErrors) {
                results.add(parseAntlrErrorToString(error, query));
            }
        }
        return results;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
