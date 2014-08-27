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

package com.stratio.meta.common.utils;

import com.stratio.meta2.common.statements.structures.terms.GenericTerm;
import com.stratio.meta2.common.statements.structures.terms.StringTerm;
import com.stratio.meta2.common.statements.structures.terms.Term;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Utility class for String transformation operations.
 */
public class StringUtils {

  /**
   * Create a string from a list of objects using a separator between objects.
   * @param ids The list of objects.
   * @param separator The separator.
   * @return A String.
   */
  public static String stringList(List<?> ids, String separator) {
    StringBuilder sb = new StringBuilder();
    for(Object value: ids){
      sb.append(value.toString()).append(separator);
    }
    if(sb.length() > separator.length()){
      return sb.substring(0, sb.length()-separator.length());
    } else {
      return "";
    }
  }

  public static Map<Term, GenericTerm> convertJsonToOptions(String json){
    Map<Term, GenericTerm> options = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    JsonFactory factory = mapper.getJsonFactory();
    JsonParser jp;
    try {
      jp = factory.createJsonParser(json);
      JsonNode root = mapper.readTree(jp);
      Iterator<Map.Entry<String, JsonNode>> iter = root.getFields();
      while(iter.hasNext()){
        Map.Entry<String, JsonNode> entry = iter.next();
        options.put(new StringTerm(entry.getKey()), GenericTerm.CreateGenericTerm(entry.getValue()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return options;
  }

  public static String getStringFromOptions(Map<Term, GenericTerm> options){
    StringBuilder sb = new StringBuilder("{");
    Iterator<Map.Entry<Term, GenericTerm>> entryIt = options.entrySet().iterator();
    Map.Entry<Term, GenericTerm> e;
    while(entryIt.hasNext()){
      e = entryIt.next();
      sb.append(e.getKey()).append(": ").append(e.getValue());
      if(entryIt.hasNext()){
        sb.append(", ");
      }
    }
    sb.append("}");
    return sb.toString();
  }

}
