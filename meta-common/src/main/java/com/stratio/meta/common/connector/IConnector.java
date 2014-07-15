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

package com.stratio.meta.common.connector;

import com.stratio.meta.common.exceptions.ConnectionException;
import com.stratio.meta.common.exceptions.InitializationException;
import com.stratio.meta.common.exceptions.UnsupportedException;
import com.stratio.meta.common.security.ICredentials;

/**
 * Common interface for META connectors.
 * A connector provides implementations for storage and query engines. Notice that connectors do not
 * need to provide both functionalities at the same time.
 */
public interface IConnector {

  /**
   * Get the name of the datastore required by the connector. Several connectors may declare the
   * same datastore name.
   * @return The name.
   */
  public String getDatastoreName();

  /**
   * Initialize the underlying datastore. Notice that this method maybe called several times by META
   * and it is responsability of the connector to manage the number of existing connections.
   * @param credentials The user credentials.
   * @param configuration The configuration.
   * @throws InitializationException If the connector initialization fails.
   */
  public void init(ICredentials credentials, IConfiguration configuration) throws
                                                                           InitializationException;

  /**
   * Close the connection with the underlying datastore.
   * @throws ConnectionException If the close operation cannot be performed.
   */
  public void close() throws ConnectionException;

  /**
   * Retrieve the connectivity status with the datastore.
   * @return Whether it is connected or not.
   */
  public boolean isConnected();

  /**
   * Get the storage engine.
   * @return An implementation of {@link com.stratio.meta.common.connector.IStorageEngine}.
   * @throws UnsupportedException If the connector does not provide this functionality.
   */
  public IStorageEngine getStorageEngine() throws UnsupportedException;

  /**
   * Get the query engine.
   * @return An implementation of {@link com.stratio.meta.common.connector.IQueryEngine}.
   * @throws UnsupportedException If the connector does not provide this functionality.
   */
  public IQueryEngine getQueryEngine() throws UnsupportedException;
}
