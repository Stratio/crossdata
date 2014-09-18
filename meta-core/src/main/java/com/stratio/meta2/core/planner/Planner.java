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
import com.stratio.meta.common.logicalplan.Filter;
import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta.common.statements.structures.relationships.Operator;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.common.statements.structures.selectors.SelectorType;
import com.stratio.meta2.core.query.NormalizedQuery;
import com.stratio.meta2.core.query.SelectPlannedQuery;
import com.stratio.meta2.core.query.SelectValidatedQuery;

import org.apache.log4j.Logger;

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
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Planner.class);

  /**
   * Create a PlannedQuery with the {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}
   * required to execute the user statement. This method is intended to be used only with Select
   * statements as any other can be directly executed.
   *
   * @param query A {@link com.stratio.meta2.core.query.NormalizedQuery}.
   * @return A {@link com.stratio.meta2.core.query.PlannedQuery}.
   */
  public SelectPlannedQuery planQuery(SelectValidatedQuery query) {
    //Build the workflow.
    LogicalWorkflow workflow = buildWorkflow(query);

    //Plan the workflow execution into different connectors.

    //Return the planned query.
    SelectPlannedQuery pq = new SelectPlannedQuery(query, workflow);
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
  protected LogicalWorkflow buildWorkflow(SelectValidatedQuery query) {
    //Define the list of projects
    Map<String, Project> projectSteps = getProjects(query);
    addProjectedColumns(projectSteps, query);
    Map<String, TableMetadata> tableMetadataMap = new HashMap<>();
    for (TableMetadata tm : query.getTableMetadata()) {
      tableMetadataMap.put(tm.getName().getQualifiedName(), tm);
    }

    Map<String, LogicalStep> filtered = addFilter(projectSteps, tableMetadataMap, query);

    //Prepare the result.
    List<LogicalStep> initialSteps = new ArrayList<>();
    for (LogicalStep ls : filtered.values()) {
      initialSteps.add(ls);
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
  private void addProjectedColumns(Map<String, Project> projectSteps, SelectValidatedQuery query) {
    for (ColumnName cn : query.getColumns()) {
      projectSteps.get(cn.getTableName().getQualifiedName()).addColumn(cn);
    }
  }

  protected Operations getFilterOperation(final TableMetadata tableName,
                                          final Selector selector,
                                          final Operator operator){
    StringBuilder sb = new StringBuilder("FILTER_");
    if(SelectorType.FUNCTION.equals(selector.getType())){
      sb.append("FUNCTION_");
    }else{
      ColumnSelector cs = ColumnSelector.class.cast(selector);
      if(tableName.isPK(cs.getName())){
        sb.append("PK_");
      }else if(tableName.isIndexed(cs.getName())){
        sb.append("INDEXED_");
      }else{
        sb.append("NON_INDEXED_");
      }
    }
    sb.append(operator.name());
    return Operations.valueOf(sb.toString());
  }

  /**
   * Add Filter operations after the Project. The Filter operations to be applied are associated
   * with the where clause found.
   *
   * @param projectMap       The map associating table names to Project steps.
   * @param tableMetadataMap A map with the table metadata indexed by table name.
   * @param query            The query to be planned.
   */
  private Map<String, LogicalStep> addFilter(Map<String, Project> projectMap,
                                             Map<String, TableMetadata> tableMetadataMap,
                                             SelectValidatedQuery query) {
    Map<String, LogicalStep> lastSteps = new HashMap<>();
    for (Map.Entry<String, Project> e : projectMap.entrySet()) {
      lastSteps.put(e.getKey(), e.getValue());
    }

    //Add filter from where.
    LogicalStep previous = null;
    TableMetadata tm = null;
    Selector s = null;
    for (Relation r : query.getRelationships()) {

      s = r.getLeftTerm();

      //TODO Support left-side functions that contain columns of several tables.
      tm = tableMetadataMap.get(s.getSelectorTablesAsString());
      if(tm != null){
        Operations op = getFilterOperation(tm, s, r.getOperator());
        Filter f = new Filter(op, r);
        previous = lastSteps.get(s.getSelectorTablesAsString());
        previous.setNextStep(f);
        lastSteps.put(s.getSelectorTablesAsString(), f);
      }else{
        LOG.error("Cannot determine Filter for relation " + r.toString());
      }

    }
    return lastSteps;
  }



  /**
   * Add Filter operations after the Project.
   */
  private void addJoin(Map<String, Project> projectMap, NormalizedQuery query) {
    //Add filter from Join
  }

  /**
   * Get a Map associating fully qualified table names with their Project logical step.
   *
   * @param query The query to be planned.
   * @return A map with the projections.
   */
  protected Map<String, Project> getProjects(SelectValidatedQuery query) {
    Map<String, Project> projects = new HashMap<>();
    for (TableName tn : query.getTables()) {
      Project p = new Project(Operations.PROJECT, tn);
      projects.put(tn.getQualifiedName(), p);
    }
    return projects;
  }

}
