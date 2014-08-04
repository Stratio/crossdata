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

package com.stratio.meta.core.parser;

import com.stratio.meta.common.result.ErrorType;
import com.stratio.meta.core.grammar.generated.MetaLexer;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.meta.core.utils.AntlrError;
import com.stratio.meta.core.utils.ErrorsHelper;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.common.result.QueryStatus;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

public class Parser {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Parser.class);
    
    /**
     * Parse a input text and return the equivalent Statement.
     * @param inputText The input text.
     * @return An AntlrResult object with the parsed Statement (if any) and the found errors (if any).
     */ 
    public MetaQuery parseStatement(String inputText){
        MetaQuery metaQuery = new MetaQuery(inputText);
        metaQuery.setStatus(QueryStatus.PARSED);
        MetaStatement resultStatement;
        ANTLRStringStream input = new ANTLRStringStream(inputText);
        MetaLexer lexer = new MetaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        com.stratio.meta.core.grammar.generated.MetaParser parser = new com.stratio.meta.core.grammar.generated.MetaParser(tokens);        
        ErrorsHelper foundErrors = null;                
        try {
            resultStatement = parser.query();
            foundErrors = parser.getFoundErrors();
        } catch (Exception e) {
            LOG.error("Cannot parse statement", e);
            metaQuery.setErrorMessage(ErrorType.PARSING, e.getMessage());
            if(foundErrors == null){                                    
                foundErrors = new ErrorsHelper();
            }
            if(foundErrors.isEmpty()){
                foundErrors.addError(new AntlrError("Unknown parser error", e.getMessage()));
            } else if(foundErrors.getAntlrErrors().iterator().next().getMessage().contains("missing")){
                metaQuery.setErrorMessage(ErrorType.PARSING, e.getMessage());
            } else {
                metaQuery.setErrorMessage(ErrorType.PARSING, foundErrors.toString(inputText));
            }
            return metaQuery;
        }
        metaQuery.setStatement(resultStatement);
        if((foundErrors!=null) && (!foundErrors.isEmpty())){
            String foundErrorsStr = foundErrors.toString(inputText);
            metaQuery.setErrorMessage(ErrorType.PARSING, foundErrorsStr);
        }
        return metaQuery;                 
    }
}
