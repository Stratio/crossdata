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

package com.stratio.meta2.core.query;

import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.common.statements.structures.assignations.Assignation;
import com.stratio.meta2.common.data.*;
import com.stratio.meta2.core.engine.validator.ValidationRequirements;
import com.stratio.meta2.core.statements.MetaStatement;

import java.util.List;

public class ParsedQuery extends BaseQuery {
  private final MetaStatement statement;
  public ParsedQuery(BaseQuery baseQuery, MetaStatement statement){
    super(baseQuery);
    this.statement=statement;
  }

  ParsedQuery(ParsedQuery parsedQuery){
    this(parsedQuery,parsedQuery.getStatement());
  }

  public MetaStatement getStatement(){
    return this.statement;
  }

  public QueryStatus getStatus() {
    return QueryStatus.PARSED;
  }


  //IStatement Methods
  public List<CatalogName> getCatalogs(){
    throw new UnsupportedOperationException();
  }


  public List<TableName> getTables(){
    throw new UnsupportedOperationException();
  }


  public List<ColumnName> getColumns(){
    throw new UnsupportedOperationException();
  }


  public List<Assignation> getAssignations(){
    throw new UnsupportedOperationException();
  }

  //public List<OrderBy> getOrderByColumns();
  //OrderBy-> List<Columns> order (ASC, DESC)

  //public List<Selector> groupBy

  public int getLimit(){
    //TODO set limit
    return 1000;
  }

  //public ???? getOptions(); //Quorum, consistency, ...


  public boolean getIfExists(){
    throw new UnsupportedOperationException();
  }

  public boolean getIfNotExists(){
    throw new UnsupportedOperationException();
  }

  public ValidationRequirements getValidationRequirements(){
    return statement.getValidationRequirements();
  }

  public List<ClusterName> getClusters() {
    throw new UnsupportedOperationException();
  }

  public List<ConnectorName> getConnectors() {
    throw new UnsupportedOperationException();
  }

  public List<DataStoreName> getDatastores() {
    throw new UnsupportedOperationException();
  }
}
