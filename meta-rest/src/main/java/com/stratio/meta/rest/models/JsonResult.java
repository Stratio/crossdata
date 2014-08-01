package com.stratio.meta.rest.models;

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
}
