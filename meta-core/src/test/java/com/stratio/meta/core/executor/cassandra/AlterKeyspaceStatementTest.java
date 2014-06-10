/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.executor.cassandra;

import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.core.executor.BasicExecutorTest;
import com.stratio.meta.core.statements.AlterKeyspaceStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.*;
import com.stratio.meta.core.utils.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertEquals;

public class AlterKeyspaceStatementTest extends BasicExecutorTest {

    @Test
    public void executionForAlterKeyspaceReplicationFactor2(){
        // EXECUTION
        MetaQuery metaQuery = new MetaQuery("ALTER KEYSPACE demo WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 2};");

        Map<String, ValueProperty> properties = new HashMap();
        properties.put("REPLICATION", new IdentifierProperty("{'class': 'SimpleStrategy', 'replication_factor': 2}"));

        AlterKeyspaceStatement stmt = new AlterKeyspaceStatement("demo", properties);

        stmt.validate(metadataManager);

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, stmt));
        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);

        validateOk(metaQuery, "executionForAlterKeyspaceReplicationFactor2");

        // CHECKING RESULT
        MetaQuery metaQuerySelect = new MetaQuery("SELECT strategy_options FROM system.schema_keyspaces WHERE keyspace_name = 'demo';");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("strategy_options")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        SelectStatement selectStmt = new SelectStatement(selectionClause, "system.schema_keyspaces");

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("keyspace_name", "=", new StringTerm("demo", true));
        clause.add(relation);
        selectStmt.setWhere(clause);

        selectStmt.validate(metadataManager);

        Tree treeSelect = new Tree();
        treeSelect.setNode(new MetaStep(MetaPath.CASSANDRA, selectStmt));
        metaQuerySelect.setPlan(treeSelect);
        metaQuerySelect.setStatus(QueryStatus.PLANNED);

        MetaQuery resultSelect = executor.executeQuery(metaQuerySelect);

        QueryResult queryResult = (QueryResult) resultSelect.getResult();

        Row row = queryResult.getResultSet().iterator().next();

        String value = (String) row.getCells().get(row.getCells().keySet().iterator().next()).getValue();

        int currentReplicationFactor = Integer.parseInt(value.replace("{\"replication_factor\":\"", "").replace("\"}", "").trim());

        assertEquals(currentReplicationFactor, 2, "executionForAlterKeyspaceReplicationFactor2");
    }

    @Test
    public void executionForAlterReplicationFactor1(){
        // EXECUTION
        MetaQuery metaQuery = new MetaQuery("ALTER KEYSPACE demo WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1};");

        Map<String, ValueProperty> properties = new HashMap();
        properties.put("REPLICATION", new IdentifierProperty("{'class': 'SimpleStrategy', 'replication_factor': 1}"));

        AlterKeyspaceStatement stmt = new AlterKeyspaceStatement("demo", properties);

        stmt.validate(metadataManager);

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, stmt));
        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);

        validateOk(metaQuery, "executionForAlterKeyspaceReplicationFactor1");

        // CHECKING RESULT
        MetaQuery metaQuerySelect = new MetaQuery("SELECT strategy_options FROM system.schema_keyspaces WHERE keyspace_name = 'demo';");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("strategy_options")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        SelectStatement selectStmt = new SelectStatement(selectionClause, "system.schema_keyspaces");

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("keyspace_name", "=", new StringTerm("demo", true));
        clause.add(relation);
        selectStmt.setWhere(clause);

        selectStmt.validate(metadataManager);

        Tree treeSelect = new Tree();
        treeSelect.setNode(new MetaStep(MetaPath.CASSANDRA, selectStmt));
        metaQuerySelect.setPlan(treeSelect);
        metaQuerySelect.setStatus(QueryStatus.PLANNED);

        MetaQuery resultSelect = executor.executeQuery(metaQuerySelect);

        QueryResult queryResult = (QueryResult) resultSelect.getResult();

        Row row = queryResult.getResultSet().iterator().next();

        String value = (String) row.getCells().get(row.getCells().keySet().iterator().next()).getValue();

        int currentReplicationFactor = Integer.parseInt(value.replace("{\"replication_factor\":\"", "").replace("\"}", "").trim());

        assertEquals(currentReplicationFactor, 1, "executionForAlterKeyspaceReplicationFactor1");
    }

}
