/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.core.planner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.logicalplan.Filter;
import com.stratio.meta.common.logicalplan.Join;
import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta.common.logicalplan.Select;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.IndexMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.grammar.ParsingTest;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.query.SelectParsedQuery;
import com.stratio.meta2.core.query.SelectValidatedQuery;
import com.stratio.meta2.core.statements.SelectStatement;

public class PlannerWorkflowTest {

    private static final Logger LOG = Logger.getLogger(PlannerWorkflowTest.class);

    ParsingTest helperPT = new ParsingTest();

    Planner planner = new Planner();

    public TableMetadata getTestTableMetadata(String table, String[] columns) {
        String catalog = "c";

        TableName tn = new TableName(catalog, table);
        ClusterName clusterRef = new ClusterName("test_cluster");
        List<ColumnName> partitionKey = new ArrayList<>();
        partitionKey.add(new ColumnName(catalog, table, columns[0]));
        List<ColumnName> clusterKey = new ArrayList<>();

        TableMetadata tm = new TableMetadata(tn,//TableName
                null, //Map<String, Object> options,
                null, //Map<ColumnName, ColumnMetadata> columns,
                new HashMap<IndexName, IndexMetadata>(),
                clusterRef,
                partitionKey,
                clusterKey);
        return tm;
    }

    public LogicalWorkflow getWorkflow(String statement, String methodName,
            TableMetadata... tableMetadataList) {
        ParsedQuery stmt = helperPT.testRegularStatement(statement, methodName);
        SelectParsedQuery spq = SelectParsedQuery.class.cast(stmt);
        SelectStatement ss = spq.getStatement();

        //SelectValidatedQueryWrapper nq = new SelectValidatedQueryWrapper(
        //        ss, new SelectParsedQuery(new BaseQuery("42", statement, null), ss));
        SelectValidatedQueryWrapper svqw = new SelectValidatedQueryWrapper(ss, spq);
        for (TableMetadata tm : tableMetadataList) {
            SelectValidatedQueryWrapper.class.cast(svqw).addTableMetadata(tm);
        }
        LogicalWorkflow workflow = planner.buildWorkflow(svqw);
        System.out.println(workflow.toString());
        return workflow;
    }

    public void assertNumberInitialSteps(LogicalWorkflow workflow, int expected) {
        assertNotNull(workflow, "Expecting workflow");
        assertEquals(workflow.getInitialSteps().size(), expected, "Expecting a single initial step.");
    }

    public Project assertColumnsInProject(LogicalWorkflow workflow, String tableName, String[] columns) {
        Project targetProject = null;
        Iterator<LogicalStep> initialSteps = workflow.getInitialSteps().iterator();
        while (targetProject == null && initialSteps.hasNext()) {
            LogicalStep ls = initialSteps.next();
            Project p = Project.class.cast(ls);
            if (tableName.equalsIgnoreCase(p.getTableName().getQualifiedName())) {
                targetProject = p;
            }
        }
        assertNotNull(targetProject, "Table " + tableName + " not found.");
        assertEquals(columns.length, targetProject.getColumnList().size(), "Number of columns differs.");
        List<String> columnList = Arrays.asList(columns);
        for (ColumnName cn : targetProject.getColumnList()) {
            assertTrue(columnList.contains(cn.getQualifiedName()), "Column " + cn + " not found");
        }
        return targetProject;
    }

    public void assertFilterInPath(Project initialStep, Operations operation) {
        LogicalStep step = initialStep;
        boolean found = false;
        while (step != null && !found) {
            if (Filter.class.isInstance(step)) {
                found = operation.equals(Filter.class.cast(step).getOperation());
            }
            step = step.getNextStep();
        }
        assertTrue(found, "Filter " + operation + " not found.");
    }

    /**
     * Match a join with the source identifiers.
     *
     * @param step
     * @param tables
     * @return
     */
    public boolean matchJoin(LogicalStep step, String... tables) {
        boolean result = false;
        if (Join.class.isInstance(step)) {
            Join j = Join.class.cast(step);
            result = j.getSourceIdentifiers().containsAll(Arrays.asList(tables));
        }
        return result;
    }

    public void assertJoin(LogicalWorkflow workflow, String t1, String t2, String... relations) {
        //Find the workflow

        Iterator<LogicalStep> it = workflow.getInitialSteps().iterator();
        LogicalStep step = null;
        boolean found = false;
        //For each initial logical step try to find the join.
        while (it.hasNext() && !found) {
            step = it.next();
            if (!(found = matchJoin(step, t1, t2))) {
                while (step.getNextStep() != null && !found) {
                    step = step.getNextStep();
                    found = matchJoin(step, t1, t2);
                }
            }
        }
        assertTrue(found, "Join between " + t1 + " and " + t2 + " not found");

    }

    public void assertSelect(LogicalWorkflow workflow) {
        assertTrue(Select.class.isInstance(workflow.getLastStep()), "Select step not found.");
    }

    @Test
    public void selectSingleColumn() {
        String inputText = "SELECT c.t.a FROM c.t;";
        String[] expectedColumns = { "c.t.a" };
        LogicalWorkflow workflow = getWorkflow(inputText, "selectSingleColumn");
        assertNumberInitialSteps(workflow, 1);
        assertColumnsInProject(workflow, "c.t", expectedColumns);
        assertSelect(workflow);
    }

    @Test
    public void selectMultipleColumns() {
        String inputText = "SELECT c.t.a, c.t.b, c.t.c FROM c.t;";
        String[] expectedColumns = { "c.t.a", "c.t.b", "c.t.c" };
        LogicalWorkflow workflow = getWorkflow(inputText, "selectSingleColumn");
        assertColumnsInProject(workflow, "c.t", expectedColumns);
        assertSelect(workflow);
    }

    @Test
    public void selectJoinMultipleColumns() {
        //TODO update on clause when fullyqualifed names are supported in the JOIN.
        String inputText = "SELECT c.t1.a, c.t1.b, c.t2.c, c.t2.d FROM c.t1 INNER JOIN c.t2 ON c.t1.aa = \"aa\";";
        String[] expectedColumnsT1 = { "c.t1.a", "c.t1.b", "c.t1.aa" };
        String[] expectedColumnsT2 = { "c.t2.c", "c.t2.d" };

        LogicalWorkflow workflow = getWorkflow(inputText, "selectSingleColumn");
        assertNumberInitialSteps(workflow, 2);
        assertColumnsInProject(workflow, "c.t1", expectedColumnsT1);
        assertColumnsInProject(workflow, "c.t2", expectedColumnsT2);

        assertJoin(workflow, "c.t1", "c.t2", "c.t1.aa = \"aa\"");
        assertSelect(workflow);
    }

    @Test
    public void selectJoinMultipleColumnsWithWhere() {
        //TODO update on clause when fullyqualifed names are supported in the JOIN.
        String inputText = "SELECT c.t1.a, c.t1.b, c.t2.c, c.t2.d FROM c.t1 INNER JOIN c.t2 ON c.t1.aa = \"aa\" WHERE c.t1.aa > 10 AND c.t2.d < 10;";

        String[] columnsT1 = { "a", "b", "aa" };
        TableMetadata t1 = getTestTableMetadata("t1", columnsT1);

        String[] columnsT2 = { "c", "d" };
        TableMetadata t2 = getTestTableMetadata("t2", columnsT2);

        String[] expectedColumnsT1 = { "c.t1.a", "c.t1.b", "c.t1.aa" };
        String[] expectedColumnsT2 = { "c.t2.c", "c.t2.d" };

        LogicalWorkflow workflow = getWorkflow(inputText, "selectSingleColumn", t1, t2);
        assertNumberInitialSteps(workflow, 2);
        Project project1 = assertColumnsInProject(workflow, "c.t1", expectedColumnsT1);
        Project project2 = assertColumnsInProject(workflow, "c.t2", expectedColumnsT2);
        assertJoin(workflow, "c.t1", "c.t2", "c.t1.aa = \"aa\"");
        assertFilterInPath(project1, Operations.FILTER_NON_INDEXED_GT);
        assertFilterInPath(project2, Operations.FILTER_NON_INDEXED_LT);

        assertSelect(workflow);

    }

    @Test
    public void selectBasicWhere() {
        String inputText = "SELECT c.t.a, c.t.b, c.t.c FROM c.t WHERE c.t.a = 3;";
        String[] columnsT = { "a", "b", "c" };
        TableMetadata t = getTestTableMetadata("t", columnsT);
        String[] expectedColumns = { "c.t.a", "c.t.b", "c.t.c" };
        LogicalWorkflow workflow = getWorkflow(inputText, "selectBasicWhere", t);
        assertColumnsInProject(workflow, "c.t", expectedColumns);
        assertSelect(workflow);
    }

    public class SelectValidatedQueryWrapper extends SelectValidatedQuery {
        private SelectStatement stmt = null;

        private List<TableMetadata> tableMetadataList = new ArrayList<>();

        public SelectValidatedQueryWrapper(SelectStatement stmt, SelectParsedQuery parsedQuery) {

            super(parsedQuery);
            this.stmt = stmt;
        }

        public void addTableMetadata(TableMetadata tm) {
            tableMetadataList.add(tm);
        }

        @Override
        public List<TableName> getTables() {
            List<TableName> tableNames = new ArrayList<>();
            tableNames.add(stmt.getTableName());
            InnerJoin join = stmt.getJoin();
            if (join != null) {
                tableNames.add(join.getTablename());
            }
            return tableNames;
        }

        @Override
        public List<ColumnName> getColumns() {
            List<ColumnName> columnNames = new ArrayList<>();
            for (Selector s : stmt.getSelectExpression().getSelectorList()) {
                columnNames.addAll(getSelectorColumns(s));
            }
            InnerJoin join = stmt.getJoin();
            if (join != null) {
                for (Relation r : join.getRelations()) {
                    columnNames.addAll(getRelationColumns(r));
                }
            }
            return columnNames;
        }

        private List<ColumnName> getSelectorColumns(Selector r) {
            List<ColumnName> result = new ArrayList<>();
            if (ColumnSelector.class.isInstance(r)) {
                result.add(ColumnSelector.class.cast(r).getName());
            }
            return result;
        }

        private List<ColumnName> getRelationColumns(Relation r) {
            List<ColumnName> result = new ArrayList<>();
            result.addAll(getSelectorColumns(r.getLeftTerm()));
            return result;
        }

        @Override
        public InnerJoin getJoin() {
            return stmt.getJoin();
        }

        @Override
        public List<Relation> getRelationships() {
            return stmt.getWhere();
        }

        @Override
        public List<TableMetadata> getTableMetadata() {
            return tableMetadataList;
        }
    }

    @Test
    public void connectorChoice(){
        /*
        List<LogicalStep> initialSteps = new LinkedList<>();
        Operations operation = Operations.PROJECT;
        TableName tableName = new TableName("demo", "myTable");
        ClusterName clusterName = new ClusterName("clusterTest");
        Project project = new Project(operation, tableName, clusterName);
        operation = Operations.SELECT_OPERATOR;
        Map<String, String> columnMap = new LinkedHashMap<>();
        columnMap.put();
        columnMap.put();
        Map<String, ColumnType> typeMap;
        Select select = new Select(operation, );
        project.setNextStep(select);
        Filter filter = new Filter();
        project.setNextStep(filter);
        initialSteps.add(project);
        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);
        try {
            planner.buildExecutionWorkflow(workflow);
        } catch (PlanningException e) {
            LOG.error("connectorChoice test failed", e);
        }
        */
    }

}
