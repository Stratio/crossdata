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

package com.stratio.meta2.core.engine;

import com.stratio.meta.common.exceptions.ParsingException;
import com.stratio.meta.core.grammar.generated.MetaLexer;
import com.stratio.meta.core.query.BaseQuery;
import com.stratio.meta.core.query.ParsedQuery;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.utils.AntlrError;
import com.stratio.meta.core.utils.ErrorsHelper;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;


public class Parser {
  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Parser.class);


  public ParsedQuery parse(BaseQuery baseQuery) throws ParsingException {
    MetaStatement resultStatement=null;
    ANTLRStringStream input = new ANTLRStringStream(baseQuery.getQuery());
    MetaLexer lexer = new MetaLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    com.stratio.meta.core.grammar.generated.MetaParser parser = new com.stratio.meta.core.grammar.generated.MetaParser(tokens);
    ErrorsHelper foundErrors = new ErrorsHelper();
    try {
      resultStatement = parser.query();
    } catch (Exception e) {
      LOG.error("Cannot parse statement", e);
      foundErrors.addError(new AntlrError("Unknown parser error", e.getMessage()));
    } finally {
      if(parser.getFoundErrors()!= null && !parser.getFoundErrors().isEmpty()){
        for(AntlrError error: parser.getFoundErrors().getAntlrErrors()) {
          foundErrors.addError(error);
        }
      }
    }
    if(foundErrors.isEmpty()){
      return new ParsedQuery(baseQuery,resultStatement);
    }else {
      throw new ParsingException(foundErrors.toString(baseQuery.getQuery()),
          foundErrors.getListErrors(baseQuery.getQuery()));
    }
  }
}
