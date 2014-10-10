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
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.exceptions.PlanningException;
import com.stratio.meta.common.logicalplan.Filter;
import com.stratio.meta.common.logicalplan.Join;
import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta.common.logicalplan.Select;
import com.stratio.meta.common.statements.structures.relationships.Operator;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.ConnectorAttachedMetadata;
import com.stratio.meta2.common.metadata.ConnectorMetadata;
import com.stratio.meta2.common.metadata.IndexMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.IntegerSelector;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.grammar.ParsingTest;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.metadata.MetadataManagerTestHelper;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.query.SelectParsedQuery;
import com.stratio.meta2.core.query.SelectValidatedQuery;
import com.stratio.meta2.core.statements.SelectStatement;

public class PlannerWorkflowTest extends MetadataManagerTestHelper {

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
        assertNotNull(targetProject, "TABLE " + tableName + " not found.");
        assertEquals(columns.length, targetProject.getColumnList().size(), "Number of columns differs.");
        List<String> columnList = Arrays.asList(columns);
        for (ColumnName cn : targetProject.getColumnList()) {
            assertTrue(columnList.contains(cn.getQualifiedName()), "COLUMN " + cn + " not found");
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
        // Build Logical WORKFLOW

            // Create initial steps (Projects)
        List<LogicalStep> initialSteps = new LinkedList<>();
        Operations operation = Operations.PROJECT;
        TableName tableName = new TableName("demo", "myTable");
        ClusterName clusterName = new ClusterName("clusterTest");
        Project project = new Project(operation, tableName, clusterName);

            // Next step (Select)
        operation = Operations.SELECT_OPERATOR;
        Map<ColumnName, String> columnMap = new LinkedHashMap<>();
        columnMap.put(new ColumnName(tableName, "id"), "id");
        columnMap.put(new ColumnName(tableName, "user"), "user");
        Map<String, ColumnType> typeMap = new LinkedHashMap<>();
        typeMap.put("id", ColumnType.INT);
        typeMap.put("user", ColumnType.VARCHAR);
        Select select = new Select(operation, columnMap, typeMap);

            // Next step (Filter)
        project.setNextStep(select);
        operation = Operations.FILTER_PK_EQ;
        Selector selector = new ColumnSelector(new ColumnName(tableName, "id"));
        Operator operator = Operator.EQ;
        Selector rightTerm = new IntegerSelector(25);
        Relation relation = new Relation(selector, operator, rightTerm);
        Filter filter = new Filter(operation, relation);
        project.setNextStep(filter);
        initialSteps.add(project);

            // Add initial steps
        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        // Fill in data for METADATAMANAGER
            // Create & add DATASTORE
            final String DATASTORE_NAME = "dataStoreTest";
            DataStoreName dataStoreName = new DataStoreName(DATASTORE_NAME);
            final String version = "0.1.0";
            insertDataStore(DATASTORE_NAME, "production");


            // Create & add CONNECTOR
        ConnectorName connectorName = new ConnectorName("ConnectorTest");
        Set<DataStoreName> dataStoreRefs = Collections.singleton(dataStoreName);
        /*RequiredPropertiesType requiredPropertiesForConnector = new RequiredPropertiesType();
        OptionalPropertiesType optionalProperties = new OptionalPropertiesType();
        SupportedOperationsType supportedOperations = new SupportedOperationsType();
        Set<Operations> operations = new HashSet<>();
        operations.add(Operations.PROJECT);
        operations.add(Operations.SELECT_OPERATOR);
        operations.add(Operations.FILTER_PK_EQ);
        supportedOperations.setOperation(operations);
        ConnectorMetadata connectorMetadata = new ConnectorMetadata(connectorName, version, dataStoreRefs, requiredPropertiesForConnector,
                optionalProperties, supportedOperations);
        connectorMetadata.setActorRef(null);
        MetadataManager.MANAGER.createConnector(connectorMetadata);*/

            // Create & add CLUSTER
        Map<Selector, Selector> options = new HashMap<>();
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();
        Map<Selector, Selector> properties = new HashMap<>();
        ConnectorAttachedMetadata connectorAttachedMetadata = new ConnectorAttachedMetadata(connectorName, clusterName,
                properties);
        connectorAttachedRefs.put(connectorName, connectorAttachedMetadata);
        ClusterMetadata clusterMetadata = new ClusterMetadata(clusterName, dataStoreName, options, connectorAttachedRefs);
        MetadataManager.MANAGER.createCluster(clusterMetadata);

            // Create & add CATALOG
        CatalogName catalogName = new CatalogName("demo");
        Map<TableName, TableMetadata> catalogTables = new HashMap<>();
        CatalogMetadata catalogMetadata = new CatalogMetadata(catalogName, options, catalogTables);
        MetadataManager.MANAGER.createCatalog(catalogMetadata);

            // Create & add TABLE
        Map<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        ColumnName columnName = new ColumnName(tableName, "id");
        Integer[] parameters = {25};
        ColumnType columnType = ColumnType.INT;
        ColumnMetadata columnMetadata = new ColumnMetadata(columnName, parameters, columnType);
        columns.put(columnName, columnMetadata);

        columnName = new ColumnName(tableName, "user");
        String[] params = {"stratio"};
        columnType = ColumnType.VARCHAR;
        columnMetadata = new ColumnMetadata(columnName, params, columnType);
        columns.put(columnName, columnMetadata);

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        List<ColumnName> partitionKey = new ArrayList<>();
        partitionKey.add(new ColumnName(tableName, "id"));
        List<ColumnName> clusterKey = new ArrayList<>();
        TableMetadata tableMetadata = new TableMetadata(tableName, options, columns, indexes, clusterName,
                partitionKey, clusterKey);
        MetadataManager.MANAGER.createTable(tableMetadata);

        // Get initial steps
        List<TableName> tables = planner.getInitialSteps(workflow.getInitialSteps());

        // Get connectors meeting the required capabilities
        Map<TableName, List<ConnectorMetadata>> candidatesConnectors = planner.findCapableConnectors(tables,
                workflow.getInitialSteps());

        assertEquals(candidatesConnectors.values().iterator().next().iterator().next().getName(), connectorName,
                "Candidate Connectors wrong");

        // Get more suitable connector
        try {
            ConnectorMetadata chosenConnector = planner.findMoreSuitableConnector(candidatesConnectors);
            assertEquals(chosenConnector.getName(), connectorName, "Chosen connector wrong");
        } catch (PlanningException e) {
            fail(e.getMessage());
        }

        try {
            planner.buildExecutionWorkflow(workflow);
        } catch (PlanningException e) {
            LOG.error("connectorChoice test failed", e);
        }
    }

}
