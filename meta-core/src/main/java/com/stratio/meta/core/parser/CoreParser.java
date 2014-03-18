package com.stratio.meta.core.parser;

import com.stratio.meta.core.grammar.generated.MetaLexer;
import com.stratio.meta.common.statements.MetaStatement;
import com.stratio.meta.common.utils.AntlrError;
import com.stratio.meta.common.utils.AntlrResult;
import com.stratio.meta.common.utils.ErrorsHelper;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

public class CoreParser {
    
    private static final Logger logger = Logger.getLogger(CoreParser.class);
    
    /**
     * Parse a input text and return the equivalent Statement.
     * @param inputText The input text.
     * @return An AntlrResult object with the parsed Statement (if any) and the found errors (if any).
     */ 
    public static AntlrResult parseStatement(String inputText){
        MetaStatement result = null;
        ANTLRStringStream input = new ANTLRStringStream(inputText);
        MetaLexer lexer = new MetaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        com.stratio.meta.core.grammar.generated.MetaParser parser = new com.stratio.meta.core.grammar.generated.MetaParser(tokens);        
        ErrorsHelper foundErrors = null;                
        try {
            result = parser.query();
            foundErrors = parser.getFoundErrors();
        } catch (Exception e) {
            logger.error("Cannot parse statement", e);
            if(foundErrors == null){                                    
                foundErrors = new ErrorsHelper();
            }
            if(foundErrors.isEmpty()){
                foundErrors.addError(new AntlrError("Unkown parser error", e.getMessage()));
            }
            return new AntlrResult(result, foundErrors);
        } 
        return new AntlrResult(result, foundErrors);                 
    }
}
