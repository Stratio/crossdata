/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.core.planner;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.statements.structures.relationships.RelationType;
import com.stratio.meta2.core.query.PlannedQuery;
import com.stratio.meta2.core.query.ValidatedQuery;
import com.stratio.meta2.core.statements.MetaStatement;

/**
 * Class in charge of defining the set of {@link com.stratio.meta.common.logicalplan.LogicalStep}
 * required to execute a statement. This set of steps are ordered as a workflow on a
 * {@link com.stratio.meta.common.logicalplan.LogicalWorkflow} structure. Notice that the
 * LogicalWorkflow may contain several initial steps, but it will always finish in a single
 * operation.
 */
public class Planner {

  public Planner() {}

  /**
   * Create a PlannedQuery with the {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}
   * required to execute the user statement.
   * @param validatedQuery The validated query.
   * @return A {@link com.stratio.meta2.core.query.PlannedQuery}.
   */
  public PlannedQuery planQuery(ValidatedQuery validatedQuery) {
    //LogicalWorkflow workflow = new LogicalWorkflow();
/*
    // crear la proyección necesaria
    MetaStatement statement = validatedQuery.getStatement();
    if (statement.getTables().size() == 1) { // en caso de que haya una tabla como target
      TableName table = statement.getTables().get(0);
      Project project = new Project(statement.getEffectiveCatalog(), table, statement.getColumns());

    } else { // en caso de que haya más tablas (ex. join, delete multi columna...) no está contemplado de momento

    }
    // crear el filtro
    Relation relation = getRelation(statement);
    Filter filter = new Filter(getOperation(statement), getRelationType(relation), relation);
    
    
    // crear la ventana para streaming
*/
    return null;
  }

  private Operations getOperation(MetaStatement statement) {
    // switch en funcion del statement (instanceof) devuelve la operación a la que corresponde? -> o polimorfismo ( en cada statement poner un método getOperation ) 
    

    return null;
  }

  private Relation getRelation(MetaStatement statement) {
    // statement.getAssignations contiene la información necesaria para crear una Relation


    return null;
  }

  private RelationType getRelationType(Relation relation) {


    return null;
  }

}
