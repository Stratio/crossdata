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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.stratio.crossdata.common.utils.MetaUtils;

public final class ParserUtils {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(ParserUtils.class);

    /**
     * Private class constructor as all methods are static.
     */
    private ParserUtils() {
    }

    public static String stringMap(Map<?, ?> ids, String conjunction, String separator) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : ids.entrySet()) {
            sb.append(entry.getKey()).append(conjunction).append(entry.getValue().toString()).append(separator);
        }
        if (sb.length() < separator.length()) {
            return "";
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    public static String stringSet(Set<String> ids, String separator) {
        StringBuilder sb = new StringBuilder();
        for (String id : ids) {
            sb.append(id).append(separator);
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    public static Set<String> getBestMatches(String str, Set<String> words, int maxDistance) {
        int limit = maxDistance + 1;
        int currentLimit = 1;
        Set<String> result = new HashSet<>();
        while (result.isEmpty() && currentLimit < limit) {
            for (String word : words) {
                int distance = StringUtils.getLevenshteinDistance(str, word, maxDistance);
                if ((distance > -1) && (distance < currentLimit)) {
                    result.add(word);
                }
            }
            currentLimit++;
        }

        return result;
    }

    public static Integer getCharPosition(AntlrError antlrError) {
        Integer result = -1;
        if (antlrError.getHeader().contains(":")
                && antlrError.getHeader().split(":").length > 1) {
            result = Integer.valueOf(antlrError.getHeader().split(":")[1]);
        }
        return result;
    }

    public static String getQueryWithSign(String query, AntlrError ae) {
        int marker = 0;
        String q=query;
        if (q.startsWith("[")) {
            marker = q.indexOf("], ") + 3;
            q= q.substring(marker);
        }
        StringBuilder sb = new StringBuilder(q);
        int pos = getCharPosition(ae) - marker;
        if (pos >= 0) {
            sb.insert(pos, "?");
        }
        return sb.toString();
    }


    public static String getSuggestion(String query, AntlrError antlrError) {
        // We initialize the errorWord with the first word of the query
        // We initialize the token words with the initial tokens
        Set<String> statementTokens = MetaUtils.INITIALS;
        // We initialize the char position of the first word
        int charPosition = 0;
        // We initialize the suggestion from exception messages containing "T_..."
        String suggestionFromToken = "";

        if (antlrError != null) { // Antlr exception message provided information
            // Update char position with the information provided by antlr
            charPosition = getCharPosition(antlrError);
            // It's not a initial token
            if (charPosition > 0) {
                statementTokens = MetaUtils.NON_INITIALS;
            }

            // Antlr exception message is not provided
            String errorMessage = antlrError.getMessage();
            if (errorMessage == null) {
                return "";
            }

            // Antlr didn't recognize a token enclosed between single quotes

            // Antlr was expecting a determined token
            int positionToken = errorMessage.indexOf("T_");
            if (positionToken > -1) {

                // We get the expecting token removing the 'T_' part and the rest of the message
                suggestionFromToken = errorMessage.substring(positionToken + 2);
                suggestionFromToken = suggestionFromToken.trim().split(" ")[0].toUpperCase();

                // We check if there is a reserved word of Meta grammar equivalent to the expecting token
                if (!statementTokens.contains(suggestionFromToken)) {
                    suggestionFromToken = "";
                }
            }
        }

        // Get best suggestion words for the incorrect token

        //TODO: Update "Did you mean" mechanism
        return "";
    }

    public static String translateToken(String message) {
        if (message == null) {
            return "";
        }
        int tokenPosition = message.indexOf("T_");
        if (tokenPosition > -1) {
            String target = message.substring(tokenPosition);
            target = target.trim().split(" ")[0].toUpperCase();
            String replacement = getReplacement(target);
            return message.replace(target, replacement);
        } else {
            return message;
        }
    }

    private static String getReplacement(String target) {
        String targetToken = target.substring(2);
        String replacement = "";
        BufferedReader bufferedReaderF = null;
        try {
            String workingDir = System.getProperty("user.dir");
            if (workingDir.endsWith("stratio-com.stratio.crossdata")) {
                workingDir = workingDir.concat("/com.stratio.crossdata-core/");
            } else if (!workingDir.endsWith("com.stratio.crossdata-core")) {
                workingDir = workingDir.substring(0, workingDir.lastIndexOf('/'));
                workingDir = workingDir.concat("/com.stratio.crossdata-core/");
            } else {
                workingDir = workingDir.concat("/");
            }

            String metaTokens = workingDir + "src/main/resources/com/stratio/com.stratio.crossdata/parser/tokens.txt";

            bufferedReaderF = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(metaTokens), Charset.forName("UTF-8")));

            String line = bufferedReaderF.readLine();
            while (line != null) {
                if (line.startsWith(targetToken)) {
                    replacement = line.substring(line.indexOf(':') + 1);
                    replacement = replacement.replace("[#", "\"");
                    replacement = replacement.replace("#]", "\"");
                    replacement = replacement.replace(" ", "");
                    break;
                }
                line = bufferedReaderF.readLine();
            }
        } catch (IOException ex) {
            LOG.error("Cannot read replacement file", ex);
        } finally {
            try {
                if (bufferedReaderF != null) {
                    bufferedReaderF.close();
                }
            } catch (IOException e) {
                LOG.error("Cannot close replacement file", e);
            }
        }
        return replacement;
    }

    public static String translateLiteralsToCQL(String metaStr) {
        StringBuilder sb = new StringBuilder();
        int startSquareBracketPosition = metaStr.indexOf('{');
        int endSquareBracketPosition = metaStr.indexOf('}');
        String propsStr = metaStr.substring(startSquareBracketPosition + 1, endSquareBracketPosition).trim();
        if (propsStr.length() < 3) {
            return metaStr;
        }
        sb.append(metaStr.substring(0, startSquareBracketPosition + 1));
        String[] props = propsStr.split(",");
        for (String prop : props) {
            String[] keyAndValue = prop.trim().split(":");
            if (keyAndValue[0].contains("'")) {
                sb.append(keyAndValue[0].trim()).append(": ");
            } else {
                sb.append("'").append(keyAndValue[0].trim()).append("'").append(": ");
            }

            if (keyAndValue[1].trim().matches("[0123456789.]+")) {
                sb.append(keyAndValue[1].trim()).append(", ");
            } else {
                if (keyAndValue[1].contains("'")) {
                    sb.append(keyAndValue[1].trim()).append(", ");
                } else {
                    sb.append("'").append(keyAndValue[1].trim()).append("'").append(", ");
                }

            }
        }
        sb = sb.delete(sb.length() - 2, sb.length());
        sb.append(metaStr.substring(endSquareBracketPosition));
        return sb.toString();
    }

    public static String addSingleQuotesToString(String strList, String separator) {
        StringBuilder sb = new StringBuilder();
        String[] eltos = strList.split(separator);
        for (String elto : eltos) {
            elto = elto.trim();
            if (elto.matches("[0123456789.]+")) {
                sb.append(elto).append(", ");
            } else {
                if (elto.contains("'")) {
                    sb.append(elto).append(", ");
                } else {
                    sb.append("'").append(elto).append("'").append(", ");
                }
            }
        }
        if (sb.charAt(sb.length() - 2) == ',') {
            return sb.substring(0, sb.length() - 2);
        } else {
            return sb.substring(0, sb.length());
        }

    }

}
