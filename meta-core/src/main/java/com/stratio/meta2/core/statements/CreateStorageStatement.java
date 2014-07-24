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

package com.stratio.meta2.core.statements;

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code CREATE STORAGE} statement from the META language. A storage represents
 * a logical cluster of storage machines that target the same datastore technology. To create a
 * storage, the user must specify the hosts and ports where the datastore is available.
 * Additionally, the user must specify at least one connector to access the datastore.
 */
public class CreateStorageStatement extends MetaStatement{

  /**
   * Storage name given by the user. This name will be used to refer to the storage when creating
   * new tables.
   */
  private final String storageName;

  /**
   * Whether the storage should be created only if not exists.
   */
  private final boolean ifNotExists;

  /**
   * A JSON with the options specified by the user.
   */
  private final String options;

  /**
   * Create a new storage on the system.
   * @param storageName The name of the storage.
   * @param ifNotExists Whether it should be created only if not exists.
   * @param JSON A JSON with the storage options.
   */
  public CreateStorageStatement(String storageName, boolean ifNotExists, String JSON){
    this.storageName = storageName;
    this.ifNotExists = ifNotExists;
    this.options = JSON;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("CREATE STORAGE ");
    if(ifNotExists){
      sb.append("IF NOT EXISTS ");
    }
    sb.append(storageName);
    sb.append(" WITH ").append(options);
    return sb.toString();
  }

  @Override
  public String translateToCQL() {
    return null;
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    return null;
  }
}
