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

package com.stratio.meta.rest.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.exceptions.ConnectionException;
import com.stratio.meta.common.exceptions.ExecutionException;
import com.stratio.meta.common.exceptions.ParsingException;
import com.stratio.meta.common.exceptions.UnsupportedException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.result.IResultHandler;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.driver.BasicDriver;

public class DriverHelper {
  private static DriverHelper instance = null;
  private String user;
  private Result queryResult;
  private String asyncQueryResult;
  private BasicDriver metaDriver;


  public DriverHelper() {

  }

  public static DriverHelper getInstance() {
    if (instance == null)
      instance = new DriverHelper();

    return instance;
  }


  public Result connect() throws ConnectionException {
    this.user = System.getProperty("user.name");
    if (user == null)
      this.user = "user";
    metaDriver = new BasicDriver();
    return metaDriver.connect(user);
  }

  public void executeSyncQuery(String query, String targetKeyspace) throws UnsupportedException,
      ParsingException, ValidationException, ExecutionException, ConnectionException {
    queryResult = null;
    queryResult = metaDriver.executeQuery(targetKeyspace, query);

  }

  public void executeAsyncQuery(String query, String targetCatalog, IResultHandler callback)
      throws UnsupportedException, ParsingException, ValidationException, ExecutionException,
      ConnectionException {
    asyncQueryResult = metaDriver.asyncExecuteQuery(targetCatalog, query, callback);

  }

  public void close() {
    metaDriver.close();
  }


  public String getUser() {
    return user;
  }

  public Result getResult() {
    return queryResult;
  }

  public String getAsyncResult() {
    return asyncQueryResult;
  }
  
  public IResultHandler getCallback(String queryId){
    return metaDriver.getResultHandler(queryId);
  }
  
  public boolean removeCallback(String queryId){
    return metaDriver.removeResultHandler(queryId);
  }

  public String toStringResult() {
    ResultSet metaResultSet = ((QueryResult) queryResult).getResultSet();
    if (metaResultSet.isEmpty()) {
      return "OK";
    }

    com.stratio.meta.common.data.ResultSet resultSet = metaResultSet;

    Map<String, Integer> colWidths = calculateColWidths(resultSet);

    String bar =
        StringUtils.repeat('-', getTotalWidth(colWidths) + (colWidths.values().size() * 3) + 1);

    StringBuilder sb = new StringBuilder(System.lineSeparator());
    sb.append(bar).append(System.lineSeparator());
    boolean firstRow = true;
    for (com.stratio.meta.common.data.Row row : resultSet) {
      sb.append("| ");

      if (firstRow) {
        for (String key : row.getCells().keySet()) {
          sb.append(StringUtils.rightPad(key, colWidths.get(key))).append("| ");
        }
        sb.append(System.lineSeparator());
        sb.append(bar);
        sb.append(System.lineSeparator());
        sb.append("| ");
        firstRow = false;
      }

      for (Map.Entry<String, Cell> entry : row.getCells().entrySet()) {
        String str = String.valueOf(entry.getValue().getValue());
        sb.append(StringUtils.rightPad(str, colWidths.get(entry.getKey())));
        sb.append(" | ");
      }
      sb.append(System.lineSeparator());
    }
    sb.append(bar).append(System.lineSeparator());
    return sb.toString();
  }

  private static Map<String, Integer> calculateColWidths(
      com.stratio.meta.common.data.ResultSet resultSet) {
    Map<String, Integer> colWidths = new HashMap<>();
    // Get column names
    com.stratio.meta.common.data.Row firstRow = resultSet.iterator().next();
    for (String key : firstRow.getCells().keySet()) {
      colWidths.put(key, key.length());
    }
    // Find widest cell content of every column
    for (com.stratio.meta.common.data.Row row : resultSet) {
      for (String key : row.getCells().keySet()) {
        String cellContent = String.valueOf(row.getCell(key).getValue());
        int currentWidth = colWidths.get(key);
        if (cellContent.length() > currentWidth) {
          colWidths.put(key, cellContent.length());
        }
      }
    }
    return colWidths;
  }

  private static int getTotalWidth(Map<String, Integer> colWidths) {
    int totalWidth = 0;
    for (int width : colWidths.values()) {
      totalWidth += width;
    }
    return totalWidth;
  }

}
