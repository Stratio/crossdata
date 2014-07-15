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

package com.stratio.meta.common.exceptions;

/**
 * Exception thrown by the connectors to signal a problem during the initialization
 * phase. The error may be related to the configuration, the connectivity or any other
 * problem the connector may encounter during the initialization.
 */
public class InitializationException extends Exception{

  private static final long serialVersionUID = -3453090024561154440L;

  public InitializationException(String msg){
    super(msg);
  }

  public InitializationException(String msg, Throwable cause){
    super(msg, cause);
  }

}
