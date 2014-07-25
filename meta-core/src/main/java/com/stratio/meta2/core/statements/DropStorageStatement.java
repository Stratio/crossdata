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
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code DROP STORAGE} statement from the META language. In order to remove
 * an active storage from the system, the user is required to delete first the existing tables.
 */
public class DropStorageStatement extends MetaStatement{

  /**
   * Storage name given by the user.
   */
  private final String storageName;

  /**
   * Default constructor.
   * @param storageName The name of the storage to be removed.
   */
  public DropStorageStatement(String storageName){
    this.storageName = storageName;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("DROP STORAGE ");
    sb.append(storageName);
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
