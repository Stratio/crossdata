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

package com.stratio.crossdata.common.logicalplan;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Created by lcisneros on 5/05/15.
 */
public class LogicalWorkflowTest {


    @Test
    public void testToString(){

        List<LogicalStep> logicalSteps = new ArrayList<>();

        TableName tableName = new TableName("catalogName", "Employees");
        ClusterName clusterName = new ClusterName("ClusterName");

        Project project = new Project(Collections.singleton(Operations.PROJECT), tableName, clusterName);
        project.addColumn(new ColumnName(tableName, "id"));
        project.addColumn(new ColumnName(tableName, "name"));

        Select select = createSelect(project);

        project.setNextStep(select);

        logicalSteps.add(project);
        LogicalWorkflow logicalWorkflow = new LogicalWorkflow(logicalSteps);

        //Experimentation
        String result = logicalWorkflow.toString();

        //Expectations

        String expected = "LogicalWorkflow\n"
        +"PROJECT catalogName.Employees ON cluster.ClusterName (catalogName.Employees.id, catalogName.Employees.name)\n"
        + "\tSELECT (catalogName.Employees.id AS id, catalogName.Employees.name AS name)\n";

        Assert.assertEquals(result, expected);
    }

    private Select createSelect(Project project) {
        Map<Selector, String> columnMap = new LinkedHashMap<>();
        Map<String, ColumnType> typeMap = new LinkedHashMap<>();
        Map<Selector, ColumnType> typeMapFromColumnName = new LinkedHashMap<>();

        for(ColumnName column : project.getColumnList()){
            ColumnSelector cs = new ColumnSelector(column);
            columnMap.put(cs, column.getName());
            typeMap.put(column.getName(), new ColumnType(DataType.TEXT) );
            typeMapFromColumnName.put(cs, new ColumnType(DataType.TEXT));
        }

        return new Select(
                Collections.singleton(Operations.SELECT_OPERATOR),
                columnMap,
                typeMap,
                typeMapFromColumnName);
    }


    @Test
    private void testGetLastStep(){
        List<LogicalStep> logicalSteps = new ArrayList<>();

        TableName tableName = new TableName("catalogName", "Employees");
        ClusterName clusterName = new ClusterName("ClusterName");

        Project project = new Project(Collections.singleton(Operations.PROJECT), tableName, clusterName);
        project.addColumn(new ColumnName(tableName, "id"));
        project.addColumn(new ColumnName(tableName, "name"));

        Select select = createSelect(project);

        project.setNextStep(select);

        logicalSteps.add(project);
        LogicalWorkflow logicalWorkflow = new LogicalWorkflow(logicalSteps);

        //experimentation
        LogicalStep lastStep = logicalWorkflow.getLastStep();

        //expectations
        Assert.assertEquals(lastStep, select);
    }

}
