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

package com.stratio.meta.driver;

import com.stratio.meta.common.result.IResultHandler;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.common.result.Result;

import org.apache.log4j.Logger;

/**
 * IResultHandler wrapper for test purposes.
 */
public class ResultHandlerWrapper implements IResultHandler{

  private boolean ackReceived = false;
  private boolean errorReceived = false;
  private boolean resultReceived = false;

  private QueryStatus status = null;

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(ResultHandlerWrapper.class);

  @Override
  public void processAck(String queryId, QueryStatus status) {
    LOG.info("Query: " + queryId + " status: " + status);
    ackReceived = true;
    this.status = status;
  }

  @Override
  public void processError(Result errorResult) {
    LOG.error("Error reported: " + errorResult);
    errorReceived = true;
  }

  @Override
  public void processResult(Result result){
    LOG.info("Result: " + result);
    resultReceived = true;
  }

  public boolean isAckReceived() {
    return ackReceived;
  }

  public boolean isErrorReceived() {
    return errorReceived;
  }

  public boolean isResultReceived() {
    return resultReceived;
  }

  public QueryStatus getStatus() {
    return status;
  }
}
