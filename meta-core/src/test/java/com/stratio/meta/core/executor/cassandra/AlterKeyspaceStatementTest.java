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

package com.stratio.meta.core.executor.cassandra;

import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.core.executor.BasicExecutorTest;
import com.stratio.meta.core.statements.AlterKeyspaceStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.IdentifierProperty;
import com.stratio.meta.core.structures.Relation;
import com.stratio.meta.core.structures.RelationCompare;
import com.stratio.meta.core.structures.SelectionClause;
import com.stratio.meta.core.structures.SelectionList;
import com.stratio.meta.core.structures.SelectionSelector;
import com.stratio.meta.core.structures.SelectionSelectors;
import com.stratio.meta.core.structures.SelectorIdentifier;
import com.stratio.meta.core.structures.StringTerm;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class AlterKeyspaceStatementTest extends BasicExecutorTest {

  @Test
  public void executionForAlterKeyspaceReplicationFactor2() {
    // EXECUTION
    MetaQuery metaQuery =
        new MetaQuery(
            "ALTER KEYSPACE demo WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 2};");

    Map<String, ValueProperty> properties = new HashMap<>();
    properties.put("REPLICATION", new IdentifierProperty(
        "{'class': 'SimpleStrategy', 'replication_factor': 2}"));

    AlterKeyspaceStatement stmt = new AlterKeyspaceStatement("demo", properties);

    stmt.validate(metadataManager, null);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, stmt));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    validateOk(metaQuery, "executionForAlterKeyspaceReplicationFactor2");

    // CHECKING RESULT
    MetaQuery metaQuerySelect =
        new MetaQuery(
            "SELECT strategy_options FROM system.schema_keyspaces WHERE keyspace_name = 'demo';");

    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "strategy_options")));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    SelectStatement selectStmt = new SelectStatement(selectionClause, "system.schema_keyspaces");

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare("keyspace_name", "=", new StringTerm("demo", true));
    clause.add(relation);
    selectStmt.setWhere(clause);

    selectStmt.validate(metadataManager, null);

    Tree treeSelect = new Tree();
    treeSelect.setNode(new MetaStep(MetaPath.CASSANDRA, selectStmt));
    metaQuerySelect.setPlan(treeSelect);
    metaQuerySelect.setStatus(QueryStatus.PLANNED);

    MetaQuery resultSelect = executor.executeQuery(metaQuerySelect, null);

    QueryResult queryResult = (QueryResult) resultSelect.getResult();

    Row row = queryResult.getResultSet().iterator().next();

    String value =
        (String) row.getCells().get(row.getCells().keySet().iterator().next()).getValue();

    int currentReplicationFactor =
        Integer.parseInt(value.replace("{\"replication_factor\":\"", "").replace("\"}", "").trim());

    assertEquals(currentReplicationFactor, 2, "executionForAlterKeyspaceReplicationFactor2");
  }

  @Test
  public void executionForAlterReplicationFactor1() {
    // EXECUTION
    MetaQuery metaQuery =
        new MetaQuery(
            "ALTER KEYSPACE demo WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1};");

    Map<String, ValueProperty> properties = new HashMap<>();
    properties.put("REPLICATION", new IdentifierProperty(
        "{'class': 'SimpleStrategy', 'replication_factor': 1}"));

    AlterKeyspaceStatement stmt = new AlterKeyspaceStatement("demo", properties);

    stmt.validate(metadataManager, null);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, stmt));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    validateOk(metaQuery, "executionForAlterKeyspaceReplicationFactor1");

    // CHECKING RESULT
    MetaQuery metaQuerySelect =
        new MetaQuery(
            "SELECT strategy_options FROM system.schema_keyspaces WHERE keyspace_name = 'demo';");

    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "strategy_options")));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    SelectStatement selectStmt = new SelectStatement(selectionClause, "system.schema_keyspaces");

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare("keyspace_name", "=", new StringTerm("demo", true));
    clause.add(relation);
    selectStmt.setWhere(clause);

    selectStmt.validate(metadataManager, null);

    Tree treeSelect = new Tree();
    treeSelect.setNode(new MetaStep(MetaPath.CASSANDRA, selectStmt));
    metaQuerySelect.setPlan(treeSelect);
    metaQuerySelect.setStatus(QueryStatus.PLANNED);

    MetaQuery resultSelect = executor.executeQuery(metaQuerySelect, null);

    QueryResult queryResult = (QueryResult) resultSelect.getResult();

    Row row = queryResult.getResultSet().iterator().next();

    String value =
        (String) row.getCells().get(row.getCells().keySet().iterator().next()).getValue();

    int currentReplicationFactor =
        Integer.parseInt(value.replace("{\"replication_factor\":\"", "").replace("\"}", "").trim());

    assertEquals(currentReplicationFactor, 1, "executionForAlterKeyspaceReplicationFactor1");
  }

}
