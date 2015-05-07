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

package com.stratio.crossdata.core.planner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.executionplan.ExecutionWorkflow;
import com.stratio.crossdata.common.executionplan.ResultType;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.logicalplan.GroupBy;
import com.stratio.crossdata.common.logicalplan.Join;
import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.window.TimeUnit;
import com.stratio.crossdata.common.statements.structures.window.WindowType;
import com.stratio.crossdata.core.MetadataManagerTestHelper;
import com.stratio.crossdata.core.grammar.ParsingTest;
import com.stratio.crossdata.core.parser.Parser;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.query.SelectPlannedQuery;
import com.stratio.crossdata.core.query.SelectValidatedQuery;
import com.stratio.crossdata.core.query.StorageParsedQuery;
import com.stratio.crossdata.core.query.StoragePlannedQuery;
import com.stratio.crossdata.core.query.StorageValidatedQuery;
import com.stratio.crossdata.core.statements.SelectStatement;
import com.stratio.crossdata.core.validator.Validator;

/**
 * Base class for planner tests.
 */
public class PlannerBaseTest {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(PlannerBaseTest.class);

    ParsingTest helperPT = new ParsingTest();

    Planner planner = new Planner("127.0.0.1");


    @BeforeClass
    public void setUp() throws ManifestException {
        MetadataManagerTestHelper.HELPER.initHelper();
        MetadataManagerTestHelper.HELPER.createTestEnvironment();
    }

    @AfterClass
    public void tearDown() throws Exception {
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    public ExecutionWorkflow getPlannedQuery(String statement, String methodName,
            boolean shouldFail, TableMetadata... tableMetadataList) {
        return getPlannedQuery(statement, methodName, true, shouldFail, tableMetadataList);
    }

    /**
     * Get the execution workflow from a statement.
     *
     * @param statement         A valid statement.
     * @param methodName        The test name.
     * @param checkParser       Whether the planning should check parser result.
     * @param shouldFail        Whether the planning should succeed.
     * @param tableMetadataList The list of table metadata.
     * @return An {@link com.stratio.crossdata.common.executionplan.ExecutionWorkflow}.
     */
    public ExecutionWorkflow getPlannedQuery(String statement, String methodName, boolean checkParser,
            boolean shouldFail, TableMetadata... tableMetadataList) {

        IParsedQuery stmt = helperPT.testRegularStatement(statement, methodName, checkParser);
        SelectParsedQuery spq = SelectParsedQuery.class.cast(stmt);

        Validator validator = new Validator();
        SelectValidatedQuery svq = null;
        try {
            svq = (SelectValidatedQuery) validator.validate(spq);
        } catch (ValidationException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        SelectPlannedQuery plannedQuery = null;
        try {
            plannedQuery = planner.planQuery(svq);
            if (shouldFail) {
                fail("Expecting planning to fail");
            }
        } catch (PlanningException e) {
            if (!shouldFail) {
                e.printStackTrace();
                fail("Expecting planning to succeed: " + e.getMessage()
                        + System.lineSeparator());
            } else {
                assertNotNull(e, "Exception should not be null");
                assertEquals(e.getClass(), PlanningException.class, "Exception class does not match.");
            }
        }
        if(plannedQuery != null){
            LOG.info(plannedQuery.getExecutionWorkflow());
            return plannedQuery.getExecutionWorkflow();
        }
        return null;
    }

    public ExecutionWorkflow getPlannedStorageQuery(
            String statement,
            String methodName,
            boolean shouldFail) throws ValidationException, IgnoreQueryException {

        IParsedQuery stmt = helperPT.testRegularStatement(statement, methodName);
        StorageParsedQuery spq = StorageParsedQuery.class.cast(stmt);

        Validator validator = new Validator();
        StorageValidatedQuery storageValidatedQuery = (StorageValidatedQuery) validator.validate(spq);

        StoragePlannedQuery plannedQuery = null;
        try {
            plannedQuery = planner.planQuery(storageValidatedQuery);
            if (shouldFail) {
                fail("Expecting planning to fail");
            }
        } catch (PlanningException e) {
            if (!shouldFail) {
                fail("Expecting planning to succeed: " + System.lineSeparator() + e.getMessage());
            } else {
                assertNotNull(e, "Exception should not be null");
                assertEquals(e.getClass(), PlanningException.class, "Exception class does not match.");
            }
        }
        LOG.info(plannedQuery.getExecutionWorkflow());
        return plannedQuery.getExecutionWorkflow();
    }

    public LogicalWorkflow getWorkflow(String statement, String methodName,
            TableMetadata... tableMetadataList) throws PlanningException {
        IParsedQuery stmt = helperPT.testRegularStatement(statement, methodName);
        SelectParsedQuery spq = SelectParsedQuery.class.cast(stmt);
        SelectStatement ss = spq.getStatement();

        SelectValidatedQueryWrapper svqw = new SelectValidatedQueryWrapper(ss, spq);
        for (TableMetadata tm : tableMetadataList) {
            svqw.addTableMetadata(tm);
        }

        LogicalWorkflow workflow = planner.buildWorkflow(svqw);
        LOG.info(workflow.toString());
        return workflow;
    }

    public LogicalWorkflow getRealWorkflow(String statement, String methodName,
            TableMetadata... tableMetadataList) throws PlanningException {
        IParsedQuery stmt = helperPT.testRegularStatement(statement, methodName);
        SelectParsedQuery spq = SelectParsedQuery.class.cast(stmt);

        Validator validator = new Validator();

        SelectValidatedQuery svq = null;
        try {
            svq = (SelectValidatedQuery) validator.validate(spq);
        } catch (ValidationException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        SelectPlannedQuery plannedQuery = null;
        try {
            plannedQuery = planner.planQuery(svq);

        } catch (PlanningException e) {
                assertNotNull(e, "Exception should not be null");
                assertEquals(e.getClass(), PlanningException.class, "Exception class does not match.");
        }

        if(plannedQuery != null){
            LogicalWorkflow workflow = planner.buildWorkflow(svq);
            LOG.info(workflow.toString());
            return workflow;
        }
       return null;

    }

    public LogicalWorkflow getWorkflowNonParsed(String statement, String methodName,
            TableMetadata... tableMetadataList) throws PlanningException {
        Parser parser = new Parser();
        IParsedQuery stmt;
        BaseQuery baseQuery = new BaseQuery(
                UUID.randomUUID().toString(),
                statement,
                new CatalogName(""),
                "sessionTest");
        stmt = parser.parse(baseQuery);

        SelectParsedQuery spq = SelectParsedQuery.class.cast(stmt);
        SelectStatement ss = spq.getStatement();

        SelectValidatedQueryWrapper svqw = new SelectValidatedQueryWrapper(ss, spq);
        for (TableMetadata tm : tableMetadataList) {
            svqw.addTableMetadata(tm);
        }
        LogicalWorkflow workflow = planner.buildWorkflow(svqw);
        LOG.info(workflow.toString());
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
                LOG.info("-> " + step.getOperations());
                found = step.getOperations().contains(operation);
                //found = operation.equals(step.getOperation());
            }
            step = step.getNextStep();
        }
        assertTrue(found, "Filter " + operation + " not found.");
    }

    public void assertGroupByInPath(Project initialStep, Operations operation) {
        LogicalStep step = initialStep;
        boolean found = false;
        while (step != null && !found) {
            if (GroupBy.class.isInstance(step)) {
                LOG.info("-> " + step.getOperations());
                found = step.getOperations().contains(operation);
                //found = operation.equals(step.getOperation());
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
        LogicalStep step;
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

    public void assertSelect(LogicalWorkflow workflow,
            Map<ColumnName, String> columnMap,
            Map<String, ColumnType> typeMap) {
        assertTrue(Select.class.isInstance(workflow.getLastStep()), "Select step not found.");
        Select s = Select.class.cast(workflow.getLastStep());
        assertEquals(columnMap, s.getColumnMap(), "Invalid column map");
        assertEquals(typeMap, s.getTypeMap(), "Invalid type map");
    }

    public void assertWindow(LogicalWorkflow workflow, WindowType type, int numRows,
            int numTimeUnits, TimeUnit timeUnit) {

        Iterator<LogicalStep> it = workflow.getInitialSteps().iterator();
        LogicalStep step = null;
        boolean found = false;
        //For each initial logical step try to find the join.
        while (it.hasNext() && !found) {
            step = it.next();
            while (step != null && !found) {
                LOG.info("step: " + step + " isInstance: " + com.stratio.crossdata.common.logicalplan.Window.class
                        .isInstance(step));
                if (com.stratio.crossdata.common.logicalplan.Window.class.isInstance(step)) {
                    found = true;
                } else {
                    step = step.getNextStep();
                }
            }
        }
        if (found) {
            com.stratio.crossdata.common.logicalplan.Window window = com.stratio.crossdata.common.logicalplan.Window.class
                    .cast(step);

            assertEquals(window.getType(), type, "Invalid window type");
            if (WindowType.TEMPORAL.equals(type)) {
                assertEquals(window.getNumTimeUnits(), numTimeUnits, "Invalid number of time units");
                assertEquals(window.getTimeUnit(), timeUnit, "Invalid time unit");
            } else {
                assertEquals(window.getNumRows(), numRows, "Invalid number of rows");
            }
        } else {
            fail("Window operator not found");
        }

    }

    public void assertExecutionWorkflow(ExecutionWorkflow executionWorkflow, int numberSteps, String[] targetActors) {
        assertNotNull(executionWorkflow, "Null execution workflow received");
        LOG.info(executionWorkflow);
        int steps = 1;
        ExecutionWorkflow current = executionWorkflow;
        assertNotNull(current.getActorRef(), "Null target actor");
        assertEquals(targetActors[0], current.getActorRef(), "Invalid target actor " + current.getActorRef());
        while (ResultType.TRIGGER_EXECUTION.equals(current.getResultType())) {
            assertNotNull(current.getActorRef(), "Null target actor");
            assertEquals(targetActors[steps - 1], current.getActorRef(), "Invalid target actor");
            current = current.getNextExecutionWorkflow();
            steps++;
        }
        assertEquals(steps, numberSteps, "Invalid number of linked execution workflows.");
        assertEquals(current.getResultType(), ResultType.RESULTS, "Invalid result type for last execution workflow");
    }

}
