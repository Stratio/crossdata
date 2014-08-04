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

import java.util.List;

/**
 * Utility class for String transformation operations.
 */
public class StringUtils {

  /**
   * Private constructor as all methods are static.
   */
  private StringUtils(){
  };

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

}
