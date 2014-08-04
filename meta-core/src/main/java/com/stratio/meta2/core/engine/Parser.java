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

package com.stratio.meta2.core.engine;

import com.stratio.meta.common.exceptions.ParsingException;
import com.stratio.meta.core.grammar.generated.MetaLexer;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.MetaStatement;
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
