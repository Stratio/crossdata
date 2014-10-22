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

package com.stratio.crossdata.core.parser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.core.grammar.generated.MetaLexer;
import com.stratio.crossdata.core.grammar.generated.MetaParser;
import com.stratio.crossdata.core.utils.AntlrError;
import com.stratio.crossdata.core.utils.ErrorsHelper;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.query.ParsedQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.query.StorageParsedQuery;
import com.stratio.crossdata.core.statements.MetaStatement;
import com.stratio.crossdata.core.statements.MetadataStatement;
import com.stratio.crossdata.core.statements.SelectStatement;
import com.stratio.crossdata.core.statements.StorageStatement;

public class Parser {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Parser.class);

    public ParsedQuery parse(BaseQuery baseQuery) throws ParsingException {
        ParsedQuery result = null;
        MetaStatement metaStatement = this
                .parseStatement(baseQuery.getDefaultCatalog().toString(), baseQuery.getQuery());
        if (metaStatement instanceof SelectStatement) {
            result = new SelectParsedQuery(baseQuery, (SelectStatement) metaStatement);
        } else if (metaStatement instanceof StorageStatement) {
            result = new StorageParsedQuery(baseQuery, (StorageStatement) metaStatement);
        } else if (metaStatement instanceof MetadataStatement) {
            result = new MetadataParsedQuery(baseQuery, (MetadataStatement) metaStatement);
        }
        return result;
    }

    /**
     * Parse a input text and return the equivalent Statement.
     *
     * @param query The input text.
     * @return An AntlrResult object with the parsed Statement (if any) and the found errors (if any).
     */
    public MetaStatement parseStatement(String sessionCatalog, String query) throws ParsingException {
        String modifiedQuery = "[" + sessionCatalog + "], " + query;
        ANTLRStringStream input = new ANTLRStringStream("[" + sessionCatalog + "], " + query);
        if (sessionCatalog == null || sessionCatalog.isEmpty()) {
            modifiedQuery = query;
            input = new ANTLRStringStream(query);
        }

        MetaLexer lexer = new MetaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MetaParser parser = new MetaParser(tokens);
        ErrorsHelper foundErrors = new ErrorsHelper();
        MetaStatement resultStatement = null;
        try {
            resultStatement = parser.query();
            foundErrors = parser.getFoundErrors();
        } catch (Exception e) {
            LOG.error("Cannot parse statement", e);
            if (foundErrors.isEmpty()) {
                foundErrors.addError(new AntlrError("Unknown parser error", e.getMessage()));
            } else if (foundErrors.getAntlrErrors().iterator().next().getMessage().contains("missing")) {
                throw new ParsingException(e);
            }
        }
        if (!foundErrors.isEmpty()) {
            throw new ParsingException(foundErrors.toString(modifiedQuery));
        }
        return resultStatement;
    }
}
