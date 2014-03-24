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

import com.stratio.meta.core.statements.MetaStatement;

import java.util.ArrayList;
import java.util.List;

public class ErrorsHelper {
    
    private List<AntlrError> antlrErrors = new ArrayList<>();

    public List<AntlrError> getAntlrErrors(){
        return antlrErrors;
    }

    public void setAntlrErrors(List<AntlrError> antlrErros){
        this.antlrErrors = antlrErros;
    }        
    
    public void clearErrors(){
        antlrErrors.clear();
        antlrErrors = new ArrayList<>();
    }
    
    public void addError(AntlrError antlrError){
        antlrErrors.add(antlrError);
    }
    
    public AntlrError getError(int n){
        return antlrErrors.get(n);
    }
    
    public void removeError(int n){
        antlrErrors.remove(n);
    }   
    
    public boolean isEmpty(){
        return antlrErrors.isEmpty();
    }   
 
    public int getNumberOfErrors(){
        return antlrErrors.size();
    }
    
    @Override
    public String toString(){
        return toString(null, null); 
    }    
    
    public String toString(String query, MetaStatement stmt){        
        StringBuilder sb = new StringBuilder("\033[31mParser exception: \033[0m");
        for(AntlrError ae: antlrErrors){
            sb.append(System.getProperty("line.separator"));
            sb.append(ae.toString()).append(System.getProperty("line.separator"));
            sb.append(ae.toStringWithTokenTranslation()).append(System.getProperty("line.separator"));
            sb.append("\t").append(ParserUtils.getQueryWithSign(query, ae));
            if(!query.equalsIgnoreCase("")){
                sb.append(System.getProperty("line.separator")).append("\t");
                sb.append(ParserUtils.getSuggestion(query, ae));
            }
            break;
        }
        return sb.toString(); 
    }
    
}
