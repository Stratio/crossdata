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

package com.stratio.meta2.common.metadata;

import java.util.List;
import java.util.Map;

import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.common.statements.structures.selectors.Selector;

public class IndexMetadata implements IMetadata {
  private final IndexName name;
  private final List<ColumnMetadata> columns;
  private final IndexType type;
  private final Map<Selector, Selector> options;

  public IndexMetadata(IndexName name, List<ColumnMetadata> columns, IndexType type,
      Map<Selector, Selector> options) {
    this.name = name;
    this.columns = columns;
    this.type = type;
    this.options = options;
  }

  public IndexName getName() {
    return name;
  }

  public List<ColumnMetadata> getColumns() {
    return columns;
  }

  public IndexType getType() {
    return type;
  }

  public Map<Selector, Selector> getOptions() {
    return options;
  }

}
