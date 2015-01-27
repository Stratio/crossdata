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

package com.stratio.crossdata.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Errors Helper class used by th parser.
 */
public class ErrorsHelper {

    private String message;

    private List<AntlrError> antlrErrors = new ArrayList<>();

    /**
     * Build a more friendly message for the user when a parsing exception arises.
     *
     * @param ae    {@link com.stratio.crossdata.core.utils.AntlrError}.
     * @param query Additional information about the exception.
     * @return Additional information about the exception.
     */
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

    /**
     * Add a new error coming from the parser.
     *
     * @param antlrError {@link com.stratio.crossdata.core.utils.AntlrError}.
     */
    public void addError(AntlrError antlrError) {
        antlrErrors.add(antlrError);
    }

    /**
     * Find out if specific information about the parsing error is provided.
     *
     * @return whether specific information about the exception is provided.
     */
    public boolean isEmpty() {
        return antlrErrors.isEmpty();
    }

    /**
     * Get list with parsing errors.
     *
     * @return List with {@link com.stratio.crossdata.core.utils.AntlrError} objects.
     */
    public List<AntlrError> getAntlrErrors() {
        return antlrErrors;
    }

    /**
     * Convert information in this object into a String.
     *
     * @param query original query.
     * @return String with all the information of this object.
     */
    public String toString(String query) {
        String result = "Parser exception: ";
        if (!antlrErrors.isEmpty()) {
            AntlrError ae = antlrErrors.get(0);
            result = parseAntlrErrorToString(ae, query);
        }
        return result;
    }

    /**
     * Get a List of messages with additional information about errors.
     *
     * @param query original query.
     * @return List of errors information.
     */
    public List<String> getListErrors(String query) {
        List<String> results = new ArrayList<>();
        if (antlrErrors != null && !antlrErrors.isEmpty()) {
            for (AntlrError error : antlrErrors) {
                results.add(parseAntlrErrorToString(error, query));
            }
        }
        return results;
    }

    /**
     * Get the message with information about the parsing exception.
     *
     * @return String with error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set a message about the parsing exception.
     *
     * @param message information about the error.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
