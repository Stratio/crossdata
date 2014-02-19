package com.stratio.sdh.meta.utils;

import com.google.common.collect.Sets;
import com.stratio.sdh.meta.generated.MetaLexer;
import com.stratio.sdh.meta.generated.MetaParser;
import com.stratio.sdh.meta.statements.Statement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang3.StringUtils;

public class MetaUtils {

    public static Set<String> initials = Sets.newHashSet(
            "CREATE",
            "ALTER",
            "DROP",
            "SET",
            "EXPLAIN",
            "TRUNCATE",
            "INSERT",
            "UPDATE",
            "DELETE",
            "SELECT",
            "ADD",
            "LIST",
            "REMOVE",
            "STOP");    
    
    public static Set<String> noInitials = Sets.newHashSet(                      
            "KEYSPACE",
            "NOT",
            "WITH",
            "TABLE",
            "IF",
            "EXISTS",
            "AND",
            "USE",
            "SET",
            "OPTIONS",
            "ANALYTICS",
            "TRUE",
            "FALSE",
            "CONSISTENCY",
            "ALL",
            "ANY",
            "QUORUM",
            "ONE",
            "TWO",
            "THREE",
            "EACH_QUORUM",
            "LOCAL_ONE",
            "LOCAL_QUORUM",
            "PLAN",
            "FOR",
            "INDEX",
            "UDF",
            "PROCESS",
            "TRIGGER",
            "ON",
            "USING",
            "TYPE",
            "PRIMARY",
            "KEY",
            "INTO",
            "COMPACT",
            "STORAGE",
            "CLUSTERING",
            "ORDER",
            "VALUES",
            "WHERE",
            "IN",
            "FROM",
            "WINDOW",
            "LAST",
            "ROWS",
            "INNER",
            "JOIN",
            "BY",
            "LIMIT",
            "DISABLE",
            "DISTINCT",
            "COUNT",
            "AS",
            "BETWEEN",
            "ASC",
            "DESC",
            "LIKE",
            "HASH", 
            "FULLTEXT",
            "CUSTOM",
            "GROUP",
            "AGGREGATION",
            "MAX",
            "MIN",
            "AVG",
            "TOKEN");
   
    public static String StringList(List<?> ids, String separator) {
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
    
    public static String StringMap(Map ids, String conjunction, String separator) {
        //StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(Object key: ids.keySet()){
            Object vp = ids.get(key);
            sb.append(key).append(conjunction).append(vp.toString()).append(separator);
        }        
        return sb.substring(0, sb.length()-separator.length());
    }

    public static String StringSet(Set<String> ids, String separator) {
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
    
    private static String getInitialsStatements() {
        StringBuilder sb = new StringBuilder(initials.toString());
        return sb.substring(1, sb.length()-1);
    }
    
    private static int getCharPosition(AntlrError antlrError) {
        return Integer.parseInt(antlrError.getHeader().split(":")[1]);
    }
    
    public static String getSuggestion(String query, AntlrError antlrError){
        String errorWord = query.trim().split(" ")[0].toUpperCase();
        Set<String> statementTokens = initials;
        int charPosition = 0;
        String suggestionFromToken = "";
                
        if(antlrError != null){
            charPosition = getCharPosition(antlrError);
            if(charPosition>0){
                statementTokens = noInitials;
            }
            
            /* OLD get error word method */
            //System.out.println("charPosition: "+charPosition);
            //errorWord = query.substring(getCharPosition(antlrError));
            //System.out.println("partial query: "+errorWord);
            //errorWord = errorWord.trim().split(" ")[0].toUpperCase();
            //System.out.println("Erroneous word: "+errorWord);
            
            /* NEW get error word method */
            String errorMessage = antlrError.getMessage();
            errorWord = errorMessage.substring(errorMessage.indexOf("'")+1, errorMessage.lastIndexOf("'"));
            errorWord = errorWord.toUpperCase();
            //System.out.println("Erroneous word (NEW): "+errorWord);
            
            //String errorMessage = antlrError.getMessage(); 
            int positionToken = errorMessage.indexOf("T_");
            if(positionToken>-1){
                suggestionFromToken = errorMessage.substring(positionToken+2);
                suggestionFromToken = suggestionFromToken.trim().split(" ")[0].toUpperCase();
                if(!(initials.contains(suggestionFromToken) || noInitials.contains(suggestionFromToken))){
                    suggestionFromToken = "";
                }
            }
            
            //System.out.println("Suggestion from token: "+suggestionFromToken);
        }                                                  
        
        Set<LevenshteinMatch> bestMatches = getBestMatches(errorWord, statementTokens, 2);
        StringBuilder sb = new StringBuilder();        
        if((bestMatches.isEmpty() || antlrError == null) && (charPosition<1)){
            sb.append("Did you mean: ");
            sb.append(getInitialsStatements()).append("?").append(System.getProperty("line.separator"));
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
                sb.append(System.getProperty("line.separator"));              
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
            System.err.println(antlrResult.toString(cmd));
        } else {
            //System.out.println("Omit suggestion");
            System.err.println(antlrResult.toString(""));
        }
    }      

    public static String getQueryWithSign(String query, AntlrError ae) {
        StringBuilder sb = new StringBuilder(query);
        sb.insert(getCharPosition(ae), "\033[35m|\033[0m");
        return sb.toString();
    }

    public static String translateToken(String message) {        
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
        /*File dir = new File(".");
        File[] filesList = dir.listFiles();
        for (File file: filesList) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }*/
        String replacement = "";
        try {
            String metaGrammarPath = "src/main/java/com/stratio/sdh/meta/Meta.g";
            BufferedReader bufferedReaderF = new BufferedReader(new FileReader(new File(metaGrammarPath)));
            String line = bufferedReaderF.readLine();
            while (line != null){
                if(line.contains(target)){
                    //T_END_PARENTHESIS: ')';
                    replacement = line.substring(line.indexOf(":")+1);
                    replacement = replacement.replace(";", "");
                    replacement = replacement.replace("'", "");
                    replacement = replacement.replace(" ", "");
                    replacement = "\""+replacement+"\"";
                    break;
                }
                line = bufferedReaderF.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(MetaUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return replacement;
    }
    
    /**
     * Parse a input text and return the equivalent Statement.
     * @param inputText The input text.
     * @param _logger where to print the result on
     * @return An AntlrResult object with the parsed Statement (if any) and the found errors (if any).
     */ 
    public static AntlrResult parseStatement(String inputText, org.apache.log4j.Logger _logger){
        Statement result = null;
        ANTLRStringStream input = new ANTLRStringStream(inputText);
        MetaLexer lexer = new MetaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MetaParser parser = new MetaParser(tokens);   
        ErrorsHelper foundErrors = null;
        try {
            result = parser.query();
            foundErrors = parser.getFoundErrors();
        } catch (RecognitionException e) {
            _logger.error("Cannot parse statement", e);
        }            
        return new AntlrResult(result, foundErrors);
    }
    
}
