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
 * Class that models a {@code ALTER STORAGE} statement from the META language.
 */
public class AlterStorageStatement extends MetaStatement{

  /**
   * Storage name given by the user.
   */
  private final String storageName;

  /**
   * A JSON with the options specified by the user.
   */
  private final String options;

  /**
   * Alter an existing storage configuration.
   * @param storageName The name of the storage.
   * @param JSON A JSON with the storage options.
   */
  public AlterStorageStatement(String storageName, String JSON){
    this.storageName = storageName;
    this.options = JSON;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ALTER STORAGE ");
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
