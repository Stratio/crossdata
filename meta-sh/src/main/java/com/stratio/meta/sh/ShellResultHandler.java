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

package com.stratio.meta.sh;

import com.stratio.meta.common.result.IResultHandler;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.sh.utils.ConsoleUtils;

/**
 * Results handler for the Meta shell when the asynchronous interface is used.
 */
public class ShellResultHandler implements IResultHandler{

  /**
   * Parent Meta shell.
   */
  private final Metash parent;

  /**
   * Class constructor.
   * @param parent Parent metashell that will be informed when results are available.
   */
  public ShellResultHandler(Metash parent){
    this.parent = parent;
  }

  @Override
  public void processAck(String queryId, QueryStatus status) {
  }

  @Override
  public void processError(Result errorResult) {
    parent.println("");
    parent.println("\033[32mResult:\033[0m " + ConsoleUtils.stringResult(errorResult));
    parent.flush();
  }

  @Override
  public void processResult(Result result) {
    parent.updatePrompt(result);
    parent.println("");
    parent.println("\033[32mResult:\033[0m " + ConsoleUtils.stringResult(result));
    parent.flush();
    if(QueryResult.class.isInstance(result)){
      QueryResult r = QueryResult.class.cast(result);
      if(r.isLastResultSet()){
        parent.removeResultsHandler(result.getQueryId());
      }
    }
  }
}
