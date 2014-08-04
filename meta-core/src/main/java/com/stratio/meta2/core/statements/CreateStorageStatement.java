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

package com.stratio.meta2.core.statements;

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.ArrayList;
import java.util.List;

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
   * Name of the datastore associated with the storage and the connectors.
   */
  private final String datastoreName;

  /**
   * List of connectors classes.
   */
  private final List<String> connectorList = new ArrayList<>();

  /**
   * A list JSON with the options specified by the user for each connector.
   */
  private final List<String> optionList = new ArrayList<>();

  /**
   * Create a new storage on the system.
   * @param storageName The name of the storage.
   * @param ifNotExists Whether it should be created only if not exists.
   * @param datastoreName The name of the datastore.
   * @param connectorList List of connector classes.
   * @param optionList List of connector options in JSON.
   */
  public CreateStorageStatement(
      String storageName, boolean ifNotExists, String datastoreName,
      List<String> connectorList, List<String> optionList){
    this.storageName = storageName;
    this.ifNotExists = ifNotExists;
    this.datastoreName = datastoreName;
    this.connectorList.addAll(connectorList);
    this.optionList.addAll(optionList);

    System.out.println("storageName: " + storageName);
    System.out.println("datastoreName: " + datastoreName);
    for(String s : connectorList){
      System.out.println("connector: " + s);
    }
    for(String s: optionList){
      System.out.println("option: " + s);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("CREATE STORAGE ");
    if(ifNotExists){
      sb.append("IF NOT EXISTS ");
    }
    sb.append(storageName);
    sb.append(" ON DATASTORE ").append(datastoreName);
    sb.append(" USING CONNECTOR ");
    if(connectorList.size() == optionList.size() && connectorList.size() > 0) {
      System.out.println(connectorList.get(0) + " WITH OPTIONS " + optionList.get(0));
      sb.append(connectorList.get(0)).append(" WITH OPTIONS ").append(optionList.get(0));
      for (int index = 1; index < connectorList.size(); index++) {
        sb.append(" AND CONNECTOR ").append(connectorList.get(index)).append(" WITH OPTIONS ").append(optionList.get(index));
      }
    }
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
