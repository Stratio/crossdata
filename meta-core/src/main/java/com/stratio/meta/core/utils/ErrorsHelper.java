/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.utils;

import java.util.ArrayList;
import java.util.List;

public class ErrorsHelper {

  private List<AntlrError> antlrErrors = new ArrayList<>();

  public void addError(AntlrError antlrError) {
    antlrErrors.add(antlrError);
  }

  public boolean isEmpty() {
    return antlrErrors.isEmpty();
  }

  public List<AntlrError> getAntlrErrors() {
    return antlrErrors;
  }

  public String toString(String query) {
    String result="\033[31mParser exception: \033[0m";
    if (!antlrErrors.isEmpty()) {
      AntlrError ae = antlrErrors.get(0);
      result = parseAntlrErrorToString(ae,query);
    }
    return result;
  }

  public static String parseAntlrErrorToString(AntlrError ae, String query){
    StringBuilder sb = new StringBuilder("\033[31mParser exception: \033[0m");
    sb.append(System.lineSeparator());
    sb.append(ae.toStringWithTokenTranslation()).append(System.lineSeparator());
    sb.append("\t").append(ParserUtils.getQueryWithSign(query, ae));
    if (!"".equalsIgnoreCase(query)) {
      sb.append(System.lineSeparator()).append("\t");
      sb.append(ParserUtils.getSuggestion(query, ae));
    }
    return sb.toString();
  }

  public List<String> getListErrors(String query){
    List<String> results=new ArrayList<>();
    if(antlrErrors!=null && !antlrErrors.isEmpty()){
      for(AntlrError error:antlrErrors){
        results.add(parseAntlrErrorToString(error,query));
      }
    }
    return results;
  }



}
