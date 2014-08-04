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

package com.stratio.meta.core.utils;

import com.stratio.meta.core.statements.MetaStatement;

import java.util.ArrayList;
import java.util.List;

public class ErrorsHelper {
    
    private List<AntlrError> antlrErrors = new ArrayList<>();

    public void addError(AntlrError antlrError){
        antlrErrors.add(antlrError);
    }
    
    public boolean isEmpty(){
        return antlrErrors.isEmpty();
    }

    public List<AntlrError> getAntlrErrors(){
        return antlrErrors;
    }

    public String toString(String query, MetaStatement stmt){        
        StringBuilder sb = new StringBuilder("\033[31mParser exception: \033[0m");
        if(!antlrErrors.isEmpty()){
            AntlrError ae = antlrErrors.get(0);
            sb.append(System.lineSeparator());
            sb.append(ae.toStringWithTokenTranslation()).append(System.lineSeparator());
            sb.append("\t").append(ParserUtils.getQueryWithSign(query, ae));
            if(!"".equalsIgnoreCase(query)){
                sb.append(System.lineSeparator()).append("\t");
                sb.append(ParserUtils.getSuggestion(query, ae));
            }
        }
        return sb.toString(); 
    }

}
