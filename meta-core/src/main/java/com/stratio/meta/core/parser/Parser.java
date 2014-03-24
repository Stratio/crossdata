package com.stratio.meta.core.parser;

import com.stratio.meta.core.grammar.generated.MetaLexer;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.utils.AntlrError;
import com.stratio.meta.core.utils.ErrorsHelper;
import com.stratio.meta.core.utils.MetaQuery;
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
            logger.error("Cannot parse statement", e);
            if(foundErrors == null){                                    
                foundErrors = new ErrorsHelper();
            }
            if(foundErrors.isEmpty()){
                foundErrors.addError(new AntlrError("Unkown parser error", e.getMessage()));
            }
            metaQuery.setErrorMessage(foundErrors.toString());
            return metaQuery;
        }
        metaQuery.setStatement(resultStatement);
        if((foundErrors!=null) && (!foundErrors.isEmpty())){
            logger.error(foundErrors.toString(inputText, resultStatement));
            metaQuery.setErrorMessage(foundErrors.toString(inputText, resultStatement));
        }
        return metaQuery;                 
    }
}
