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

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.executionplan.ExecutionWorkflow;
import com.stratio.crossdata.common.executionplan.ResultType;
import com.stratio.crossdata.common.logicalplan.Filter;
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
import com.stratio.crossdata.core.grammar.ParsingTest;
import com.stratio.crossdata.core.metadata.MetadataManagerTestHelper;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.query.SelectPlannedQuery;
import com.stratio.crossdata.core.statements.SelectStatement;

/**
 * Base class for planner tests.
 */
public class PlannerBaseTest extends MetadataManagerTestHelper {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(PlannerBaseTest.class);

    ParsingTest helperPT = new ParsingTest();

    Planner planner = new Planner();

    /**
     * Get the execution workflow from a statement.
     * @param statement A valid statement.
     * @param methodName The test name.
     * @param shouldFail Whether the planning should succeed.
     * @param tableMetadataList The list of table metadata.
     * @return An {@link com.stratio.crossdata.common.executionplan.ExecutionWorkflow}.
     */
    public ExecutionWorkflow getPlannedQuery(String statement, String methodName,
            boolean shouldFail, TableMetadata... tableMetadataList){

        IParsedQuery stmt = helperPT.testRegularStatement(statement, methodName);
        SelectParsedQuery spq = SelectParsedQuery.class.cast(stmt);
        SelectStatement ss = spq.getStatement();

        SelectValidatedQueryWrapper svqw = new SelectValidatedQueryWrapper(ss, spq);
        for (TableMetadata tm : tableMetadataList) {
            svqw.addTableMetadata(tm);
        }

        SelectPlannedQuery plannedQuery = null;
        try {
            plannedQuery = planner.planQuery(svqw);
            if(shouldFail){
                fail("Expecting planning to fail");
            }
        } catch (PlanningException e) {
            if(!shouldFail){
                fail("Expecting planning to succeed");
            }else{
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
                LOG.info("-> " + ((Filter) step).getOperation());
                found = operation.equals(((Filter) step).getOperation());
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
            int numTimeUnits, TimeUnit timeUnit){

        Iterator<LogicalStep> it = workflow.getInitialSteps().iterator();
        LogicalStep step = null;
        boolean found = false;
        //For each initial logical step try to find the join.
        while (it.hasNext() && !found) {
            step = it.next();
            while(step != null && !found) {
                LOG.info("step: " + step + " isInstance: " + com.stratio.crossdata.common.logicalplan.Window.class.isInstance(step));
                if (com.stratio.crossdata.common.logicalplan.Window.class.isInstance(step)) {
                    found = true;
                }else {
                    step = step.getNextStep();
                }
            }
        }
        if(found) {
            com.stratio.crossdata.common.logicalplan.Window window = com.stratio.crossdata.common.logicalplan.Window.class
                    .cast(step);

            assertEquals(window.getType(), type, "Invalid window type");
            if (WindowType.TEMPORAL.equals(type)) {
                assertEquals(window.getNumTimeUnits(), numTimeUnits, "Invalid number of time units");
                assertEquals(window.getTimeUnit(), timeUnit, "Invalid time unit");
            } else {
                assertEquals(window.getNumRows(), numRows, "Invalid number of rows");
            }
        }else{
            fail("Window operator not found");
        }

    }

    public void assertExecutionWorkflow(ExecutionWorkflow executionWorkflow, int numberSteps, String [] targetActors){
        assertNotNull(executionWorkflow, "Null execution workflow received");
        LOG.info(executionWorkflow);
        int steps = 1;
        ExecutionWorkflow current = executionWorkflow;
        assertNotNull(current.getActorRef(), "Null target actor");
        assertEquals(targetActors[0], current.getActorRef(), "Invalid target actor " + current.getActorRef());
        while(ResultType.TRIGGER_EXECUTION.equals(current.getResultType())){
            assertNotNull(current.getActorRef(), "Null target actor");
            assertEquals(targetActors[steps-1], current.getActorRef(), "Invalid target actor");
            current = current.getNextExecutionWorkflow();
            steps++;
        }
        assertEquals(steps, numberSteps, "Invalid number of linked execution workflows.");
        assertEquals(current.getResultType(), ResultType.RESULTS, "Invalid result type for last execution workflow");
    }

}
