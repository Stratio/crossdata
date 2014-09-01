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

import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.core.query.NormalizedQuery;
import com.stratio.meta2.core.query.PlannedQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class in charge of defining the set of {@link com.stratio.meta.common.logicalplan.LogicalStep}
 * required to execute a statement. This set of steps are ordered as a workflow on a {@link
 * com.stratio.meta.common.logicalplan.LogicalWorkflow} structure. Notice that the LogicalWorkflow
 * may contain several initial steps, but it will always finish in a single operation.
 */
public class Planner {

  /**
   * Create a PlannedQuery with the {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}
   * required to execute the user statement. This method is intended to be used only with Select
   * statements as any other can be directly executed.
   *
   * @param query A {@link com.stratio.meta2.core.query.NormalizedQuery}.
   * @return A {@link com.stratio.meta2.core.query.PlannedQuery}.
   */
  public PlannedQuery planQuery(NormalizedQuery query) {
    //Build the workflow.
    LogicalWorkflow workflow = buildWorkflow(query);

    //Plan the workflow execution into different connectors.

    //Return the planned query.
    PlannedQuery pq = new PlannedQuery(query, workflow);
    return pq;
  }

  /**
   * Build a workflow with the {@link com.stratio.meta.common.logicalplan.LogicalStep} required to
   * execute a query. This method does not determine which connector will execute which part of the
   * workflow.
   *
   * @param query The query to be planned.
   * @return A Logical workflow.
   */
  protected LogicalWorkflow buildWorkflow(NormalizedQuery query) {
    //Define the list of projects
    Map<String, Project> projectSteps = getProjects(query);
    addProjectedColumns(projectSteps, query);

    //Prepare the result.
    List<LogicalStep> initialSteps = new ArrayList<>();
    for (Project p : projectSteps.values()) {
      initialSteps.add(p);
    }
    LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);
    return workflow;
  }


  /**
   * Add the columns that need to be retrieved to the initial steps map.
   *
   * @param projectSteps The map associating table names to Project steps.
   * @param query        The query to be planned.
   */
  private void addProjectedColumns(Map<String, Project> projectSteps, NormalizedQuery query) {
    for (ColumnName cn : query.getColumns()) {
      projectSteps.get(cn.getTableName().getQualifiedName()).addColumn(cn);
    }
  }

  private void addFilter(Map<String, Project> projectMap, NormalizedQuery query){
    //Add filter from where
    //Add filter from Join
  }

  /**
   * Get a Map associating fully qualified table names with their Project logical step.
   *
   * @param query The query to be planned.
   * @return A map with the projections.
   */
  protected Map<String, Project> getProjects(NormalizedQuery query) {
    Map<String, Project> projects = new HashMap<>();
    for (TableName tn : query.getTables()) {
      Project p = new Project(tn);
      projects.put(tn.getQualifiedName(), p);
    }
    return projects;
  }

}
