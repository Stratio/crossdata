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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParserUtils {    
    
    private static final Logger logger = Logger.getLogger(ParserUtils.class);
    
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
    
    public static String stringMap(Map ids, String conjunction, String separator) {
        //StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(Object key: ids.keySet()){
            Object vp = ids.get(key);
            sb.append(key).append(conjunction).append(vp.toString()).append(separator);
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
 
    public static Set<LevenshteinMatch> getBestMatches(String str, Set<String> words, int thresold){
        int limit = thresold+1;
        Set<LevenshteinMatch> result = new HashSet<>();
        for(String word: words){
            int distance = StringUtils.getLevenshteinDistance(str, word, thresold);
            //System.out.println("'"+str+"' vs '"+word+"' = "+distance);
            if((distance>-1) && (distance<limit)){
                result.clear();
                result.add(new LevenshteinMatch(word, distance));
                limit = distance;
            } else if (distance==limit){
                result.add(new LevenshteinMatch(word, distance));
            }
        }
        return result;
    }
    
    public static int getCharPosition(AntlrError antlrError) {
        if(antlrError.getHeader().contains(":")){
            return Integer.parseInt(antlrError.getHeader().split(":")[1]);
        } else {
            return -1;
        }
    }
    
    public static String getQueryWithSign(String query, AntlrError ae) {
        StringBuilder sb = new StringBuilder(query);
        try {
            int pos = getCharPosition(ae);
            if(pos >= 0){
                sb.insert(getCharPosition(ae), "\033[35m|\033[0m");
            }
        } catch (NullPointerException npex){
            return query;
        }
        return sb.toString();
    }
    
    public static String getSuggestion(String query, AntlrError antlrError){
        String errorWord = query.trim().split(" ")[0].toUpperCase();
        Set<String> statementTokens = MetaUtils.initials;
        int charPosition = 0;
        String suggestionFromToken = "";
                
        if(antlrError != null){
            charPosition = getCharPosition(antlrError);
            if(charPosition>0){
                statementTokens = MetaUtils.noInitials;
            }
            
            /* OLD get error word method */
            //System.out.println("charPosition: "+charPosition);
            //errorWord = query.substring(getCharPosition(antlrError));
            //System.out.println("partial query: "+errorWord);
            //errorWord = errorWord.trim().split(" ")[0].toUpperCase();
            //System.out.println("Erroneous word: "+errorWord);
            
            /* NEW get error word method */
            String errorMessage = antlrError.getMessage();
            if(errorMessage == null){
                return "";
            }
            errorWord = errorMessage.substring(errorMessage.indexOf("'")+1, errorMessage.lastIndexOf("'"));
            errorWord = errorWord.toUpperCase();
            //System.out.println("Erroneous word (NEW): "+errorWord);
            
            //String errorMessage = antlrError.getMessage(); 
            int positionToken = errorMessage.indexOf("T_");
            if(positionToken>-1){
                suggestionFromToken = errorMessage.substring(positionToken+2);
                suggestionFromToken = suggestionFromToken.trim().split(" ")[0].toUpperCase();
                if(!(MetaUtils.initials.contains(suggestionFromToken) || MetaUtils.noInitials.contains(suggestionFromToken))){
                    suggestionFromToken = "";
                }
            }
            
            //System.out.println("Suggestion from token: "+suggestionFromToken);
        }                                                  
        
        Set<LevenshteinMatch> bestMatches = getBestMatches(errorWord, statementTokens, 2);
        StringBuilder sb = new StringBuilder();        
        if((bestMatches.isEmpty() || antlrError == null) && (charPosition<1)){
            sb.append("Did you mean: ");
            sb.append(MetaUtils.getInitialsStatements()).append("?").append(System.getProperty("line.separator"));
        } else if(!suggestionFromToken.equalsIgnoreCase("")){
            //System.out.println("Result from suggestion");
            sb.append("Did you mean: ");
            sb.append("\"").append(suggestionFromToken).append("\"").append("?");
            sb.append(System.getProperty("line.separator"));
        } else if (errorWord.matches("[QWERTYUIOPASDFGHJKLZXCVBNM_]+") /*&& query.contains("T_")*/){            
            for(LevenshteinMatch match: bestMatches){
                //System.out.println(match.getWord()+":"+match.getDistance());
                if(match.getDistance()<1){
                    break;
                }
                sb.append("Did you mean: ");
                sb.append("\"").append(match.getWord()).append("\"").append("?");
                sb.append(System.getProperty("line.separator")).append("\t");              
            }
        }
        return sb.substring(0, sb.length());
    }    
    
    public static String getSuggestion() {
        return getSuggestion("", null);
    }

    public static void printParserErrors(String cmd, AntlrResult antlrResult, boolean printSuggestions) {
        if(printSuggestions){
            //System.out.println("Print suggestion");
            logger.error(antlrResult.toString(cmd));
        } else {
            //System.out.println("Omit suggestion");
            logger.error(antlrResult.toString(""));
        }
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
        target = target.substring(2);
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
            
            bufferedReaderF = new BufferedReader(new FileReader(new File(metaTokens)));
            String line = bufferedReaderF.readLine();
            while (line != null){
                if(line.startsWith(target)){
                    replacement = line.substring(line.indexOf(":")+1);
                    replacement = replacement.replace("[#", "\"");
                    replacement = replacement.replace("#]", "\"");
                    replacement = replacement.replace(" ", "");
                    break;
                }
                line = bufferedReaderF.readLine();
            }
        } catch (IOException ex) {
            logger.error("Cannot read replacement file", ex);
        }finally{
            try {
                bufferedReaderF.close();
            } catch (IOException e) {
                logger.error("Cannot close replacement file", e);
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
            sb.append("'").append(keyAndValue[0].trim()).append("'").append(": ");
            if(keyAndValue[1].trim().matches("[0123456789.]+")){
                sb.append(keyAndValue[1].trim()).append(", ");
            } else {
                sb.append("'").append(keyAndValue[1].trim()).append("'").append(", ");
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
    
    public static String addSingleQuotesToStringList(String strList){
        String[] opts = strList.split("=");
        strList = new String();
        for(int i=0; i<opts.length; i++){
            String currentStr = opts[i];
            if(currentStr.matches("[0123456789.]+")){
                strList = strList.concat(opts[i]);
            } else {
                strList = strList.concat("\'").concat(opts[i]).concat("\'");
            }
            if(i % 2 == 0){
                strList = strList.concat(": ");
            } else {
                if(i<(opts.length-1)){
                    strList = strList.concat(", ");
                }
            }
        }
        return strList;
    }    

    public static String[] fromStringListToArray(List<String> names) {
        String[] result = new String[names.size()];
        int n=0;
        for(String str: names){
            result[n] = str;
            n++;
        }
        return result;
    }

    public static String addAllowFiltering(String translatedToCQL) {
        return translatedToCQL.replace(" ;", ";").replace(";", " ALLOW FILTERING;");
    }
    
}
