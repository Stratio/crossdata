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

package com.stratio.meta.common.result;

/**
 * Callback interface for classes receiving asynchronous results from meta servers.
 */
public interface IResultHandler {

  /**
   * Process an acknowledgement message for a specific query.
   *
   * @param queryId The query identifier.
   * @param status  The query status.
   */
  public void processAck(String queryId, QueryStatus status);

  /**
   * Process an error result.
   *
   * @param errorResult The error.
   */
  public void processError(Result errorResult);

  /**
   * Process a successful result.
   *
   * @param result The result.
   */
  public void processResult(Result result);
}
