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
