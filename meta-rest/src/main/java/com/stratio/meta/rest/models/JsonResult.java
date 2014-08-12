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

package com.stratio.meta.rest.models;

import java.io.Serializable;

/**
 * Class that models a generic result of an action executed in META.
 */
public abstract class JsonResult implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7125004801071861307L;



  /**
   * Query identifier associated with the result.
   */
  private String queryId;

  /**
   * Whether an error occurred during the execution of an action.
   */
  protected boolean error = false;

  /**
   * Set the query identifier.
   *
   * @param queryId The query identifier.
   */
  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  /**
   * Get the query identifier.
   *
   * @return The query identifier.
   */
  public String getQueryId() {
    return queryId;
  }

  /**
   * Whether the result contains an error.
   *
   * @return True if the result contains errors.
   */
  public boolean hasError() {
    return error;
  }
  
  public void setError(boolean error) {
    this.error = error;
  }
}
