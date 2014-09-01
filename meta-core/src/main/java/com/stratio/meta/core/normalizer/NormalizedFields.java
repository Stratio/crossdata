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

package com.stratio.meta.core.normalizer;

import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.Selector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NormalizedFields {
  private final Set<ColumnName> columnNames=new HashSet<>();
  private final Set<TableName> tableNames=new HashSet<>();
  private final Set<TableName> searchTableNames = new HashSet<>();
  private final Set<CatalogName> catalogNames=new HashSet<>();
  private final List<Selector> selectors=new ArrayList<>();
  private final List<Relation> relations=new ArrayList<>();
  private GroupBy groupBy=null;

  private boolean distinctSelect=false;




  public Set<ColumnName> getColumnNames() {
    return columnNames;
  }

  public Set<TableName> getTableNames() {
    return tableNames;
  }

  public Set<TableName> getSearchTableNames() { return searchTableNames;}

  public Set<CatalogName> getCatalogNames() {
    return catalogNames;
  }

  public List<Selector> getSelectors() {
    return selectors;
  }



  public List<Relation> getRelations() {
    return relations;
  }

  public boolean isDistinctSelect() {
    return distinctSelect;
  }

  void setDistinctSelect(boolean distinctSelect) {
    this.distinctSelect = distinctSelect;
  }

  public GroupBy getGroupBy() {
    return groupBy;
  }

  public void setGroupBy(GroupBy groupBy) {
    this.groupBy = groupBy;
  }
}
