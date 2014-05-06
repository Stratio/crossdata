/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.utils;

import com.stratio.meta.common.utils.MetaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParserUtils {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(ParserUtils.class);

    /**
     * Private class constructor as all methods are static.
     */
    private ParserUtils(){
    }

    public static String stringList(List<?> ids, String separator) {
        StringBuilder sb = new StringBuilder();
        for(Object value: ids){
            sb.append(value.toString()).append(separator);
        }
        if(sb.length() > separator.length()){
            return sb.substring(0, sb.length()-separator.length());
        } else {
            return "";
        }
    }

    public static String stringMap(Map<?, ?> ids, String conjunction, String separator) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<?, ?> entry : ids.entrySet()){
            sb.append(entry.getKey()).append(conjunction).append(entry.getValue().toString()).append(separator);
        }
        if(sb.length() < separator.length()){
            return "";
        }
        return sb.substring(0, sb.length()-separator.length());
    }

    public static String stringSet(Set<String> ids, String separator) {
        StringBuilder sb = new StringBuilder();
        for(String id: ids){
            sb.append(id).append(separator);
        }
        return sb.substring(0, sb.length()-separator.length());
    }   
 
    public static Set<String> getBestMatches(String str, Set<String> words, int maxDistance){
        int limit = maxDistance + 1;
        int currentLimit = 1;
        Set<String> result = new HashSet<>();
        while(result.isEmpty() && currentLimit < limit){
            for(String word: words){
                int distance = StringUtils.getLevenshteinDistance(str, word, maxDistance);
                if((distance>-1) && (distance < currentLimit)){
                    result.add(word);
                }
            }
            currentLimit++;
        }

        return result;
    }
    
    public static Integer getCharPosition(AntlrError antlrError) {
        Integer result = -1;
        if(antlrError.getHeader().contains(":")
                && antlrError.getHeader().split(":").length > 1) {
            result = Integer.valueOf(antlrError.getHeader().split(":")[1]);
        }
        return result;
    }
    
    public static String getQueryWithSign(String query, AntlrError ae) {
        StringBuilder sb = new StringBuilder(query);
        int pos = getCharPosition(ae);
        if(pos >= 0){
            sb.insert(getCharPosition(ae), "\033[35m|\033[0m");
        }
        return sb.toString();
    }

    private static String createSuggestion(Set<String> bestMatches,
                                           AntlrError antlrError,
                                           int charPosition,
                                           String suggestionFromToken,
                                           String errorWord){
        StringBuilder sb = new StringBuilder();
        if((bestMatches.isEmpty() || antlrError == null) && (charPosition<1)){
            //Append all the initial tokens because we didn't find a good match for first token
            sb.append(MetaUtils.getInitialsStatements()).append("?").append(System.lineSeparator());
        } else if(!"".equalsIgnoreCase(suggestionFromToken)){
            //Antlr returned a T_... token which have a equivalence with the reserved tokens of Meta
            sb.append("\"").append(suggestionFromToken).append("\"").append("?");
            sb.append(System.lineSeparator());
        } else if(errorWord.matches("[QWERTYUIOPASDFGHJKLZXCVBNM_]+")){
            // There is no a perfect equivalence with the reserved tokens of Meta but
            // there might be a similar word
            for(String match: bestMatches){
                sb.append("\"").append(match).append("\"").append(", ");
            }
            sb.append("?").append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static String getSuggestion(String query, AntlrError antlrError){
        // We initialize the errorWord with the first word of the query
        String errorWord = query.trim().split(" ")[0].toUpperCase();
        // We initialize the token words with the initial tokens
        Set<String> statementTokens = MetaUtils.INITIALS;
        // We initialize the char position of the first word
        int charPosition = 0;
        // We initialize the suggestion from exception messages containing "T_..."
        String suggestionFromToken = "";
                
        if(antlrError != null){ // Antlr exception message provided information
            // Update char position with the information provided by antlr
            charPosition = getCharPosition(antlrError);
            // It's not a initial token
            if(charPosition>0){
                statementTokens = MetaUtils.NON_INITIALS;
            }

            // Antlr exception message is not provided
            String errorMessage = antlrError.getMessage();
            if(errorMessage == null){
                return "";
            }

            // Antlr didn't recognize a token enclosed between single quotes
            errorWord = errorMessage.substring(errorMessage.indexOf("'")+1, errorMessage.lastIndexOf("'"));
            errorWord = errorWord.toUpperCase();

            // Antlr was expecting a determined token
            int positionToken = errorMessage.indexOf("T_");
            if(positionToken>-1){

                // We get the expecting token removing the 'T_' part and the rest of the message
                suggestionFromToken = errorMessage.substring(positionToken+2);
                suggestionFromToken = suggestionFromToken.trim().split(" ")[0].toUpperCase();

                // We check if there is a reserved word of Meta grammar equivalent to the expecting token
                if(!statementTokens.contains(suggestionFromToken)){
                    suggestionFromToken = "";
                }
            }
        }

        // Get best suggestion words for the incorrect token
        Set<String> bestMatches = getBestMatches(errorWord, statementTokens, 2);

        StringBuilder sb = new StringBuilder("Did you mean: ");
        sb.append(createSuggestion(bestMatches, antlrError, charPosition, suggestionFromToken, errorWord));

        // No suggestion was found
        if("Did you mean: ?".startsWith(sb.toString())){
            return "";
        }

        return sb.substring(0, sb.length()).replace(", ?", "?");
    }    

    public static String translateToken(String message) {     
        if(message == null){
            return "";
        }
        int tokenPosition = message.indexOf("T_");        
        if(tokenPosition>-1){            
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
            if(workingDir.endsWith("stratio-meta")){
                workingDir = workingDir.concat("/meta-core/");
            } else if(!workingDir.endsWith("meta-core")){
                workingDir = workingDir.substring(0, workingDir.lastIndexOf("/"));
                workingDir = workingDir.concat("/meta-core/");
            } else {
                workingDir = workingDir.concat("/");
            }
            
            String metaTokens = workingDir+"src/main/resources/com/stratio/meta/parser/tokens.txt";

            bufferedReaderF = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(metaTokens), Charset.forName("UTF-8")));


            String line = bufferedReaderF.readLine();
            while (line != null){
                if(line.startsWith(targetToken)){
                    replacement = line.substring(line.indexOf(":")+1);
                    replacement = replacement.replace("[#", "\"");
                    replacement = replacement.replace("#]", "\"");
                    replacement = replacement.replace(" ", "");
                    break;
                }
                line = bufferedReaderF.readLine();
            }
        } catch (IOException ex) {
            LOG.error("Cannot read replacement file", ex);
        }finally{
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
        int startSquareBracketPosition = metaStr.indexOf("{");
        int endSquareBracketPosition = metaStr.indexOf("}");
        String propsStr = metaStr.substring(startSquareBracketPosition+1, endSquareBracketPosition).trim();
        if(propsStr.length() < 3){
            return metaStr;
        }
        sb.append(metaStr.substring(0, startSquareBracketPosition+1));
        String[] props = propsStr.split(",");
        for(String prop: props){
            String[] keyAndValue = prop.trim().split(":");
            if(keyAndValue[0].contains("'")) {
                sb.append(keyAndValue[0].trim()).append(": ");
            }else{
                sb.append("'").append(keyAndValue[0].trim()).append("'").append(": ");
            }

            if(keyAndValue[1].trim().matches("[0123456789.]+")){
                sb.append(keyAndValue[1].trim()).append(", ");
            } else {
                if(keyAndValue[1].contains("'")) {
                    sb.append(keyAndValue[1].trim()).append(", ");
                }else{
                    sb.append("'").append(keyAndValue[1].trim()).append("'").append(", ");
                }

            }
        }
        sb = sb.delete(sb.length()-2, sb.length());
        sb.append(metaStr.substring(endSquareBracketPosition));
        return sb.toString();
    }
    
    public static String addSingleQuotesToString(String strList, String separator){
        StringBuilder sb = new StringBuilder();
        String[] eltos = strList.split(separator);
        for(String elto: eltos){
            elto = elto.trim();
            if(elto.matches("[0123456789.]+")){
                sb.append(elto).append(", ");
            } else {
                sb.append("'").append(elto).append("'").append(", ");
            }
        }
        if(sb.charAt(sb.length()-2)==','){
            return sb.substring(0, sb.length()-2);
        } else {
            return sb.substring(0, sb.length());
        }
        
    }
    
}
