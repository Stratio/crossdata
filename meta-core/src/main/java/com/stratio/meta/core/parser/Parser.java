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

package com.stratio.meta.core.parser;

import com.stratio.meta.core.grammar.generated.MetaLexer;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.utils.AntlrError;
import com.stratio.meta.core.utils.ErrorsHelper;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.QueryStatus;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

public class Parser {
    
    private final Logger logger = Logger.getLogger(Parser.class);
    
    /**
     * Parse a input text and return the equivalent Statement.
     * @param inputText The input text.
     * @return An AntlrResult object with the parsed Statement (if any) and the found errors (if any).
     */ 
    public MetaQuery parseStatement(String inputText){
        MetaQuery metaQuery = new MetaQuery(inputText);
        metaQuery.setStatus(QueryStatus.PARSED);
        MetaStatement resultStatement;
        System.out.println("Parsing: {"+inputText+"}");
        ANTLRStringStream input = new ANTLRStringStream(inputText);
        MetaLexer lexer = new MetaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        com.stratio.meta.core.grammar.generated.MetaParser parser = new com.stratio.meta.core.grammar.generated.MetaParser(tokens);        
        ErrorsHelper foundErrors = null;                
        try {
            resultStatement = parser.query();
            foundErrors = parser.getFoundErrors();
            //System.out.println("ResultStmt: " + resultStatement + " foundErrors: =" + foundErrors + "=");
        } catch (Exception e) {
            logger.error("Cannot parse statement", e);
            if(foundErrors == null){                                    
                foundErrors = new ErrorsHelper();
            }
            if(foundErrors.isEmpty()){
                foundErrors.addError(new AntlrError("Unknown parser error", e.getMessage()));
            }
            metaQuery.setErrorMessage(foundErrors.toString());
            return metaQuery;
        }
        metaQuery.setStatement(resultStatement);
        if((foundErrors!=null) && (!foundErrors.isEmpty())){
            String foundErrorsStr = foundErrors.toString(inputText, resultStatement);
            //logger.error("Recoverable: " + foundErrorsStr);
            metaQuery.setErrorMessage(foundErrorsStr);
        }
        return metaQuery;                 
    }
}
