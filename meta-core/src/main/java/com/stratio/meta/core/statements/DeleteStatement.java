/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.statements;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.Relation;
import com.stratio.meta.core.structures.RelationCompare;
import com.stratio.meta.core.structures.Term;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that models a {@code SELECT} statement from the META language. This class recognizes the
 * following syntax:
 * <p>
 * DELETE ( {@literal <column>}, ( ',' {@literal <column>} )*)? FROM {@literal <tablename>} WHERE
 * {@literal <where_clause>};
 */
public class DeleteStatement extends MetaStatement {

  /**
   * The list of columns to be removed.
   */
  private List<String> targetColumns = null;

  /**
   * The name of the targe table.
   */
  private String tableName = null;

  /**
   * The list of {@link com.stratio.meta.core.structures.Relation} found in the WHERE clause.
   */
  private List<Relation> whereClauses;

  /**
   * Class constructor.
   */
  public DeleteStatement() {
    this.command = false;
    targetColumns = new ArrayList<>();
    whereClauses = new ArrayList<>();
  }

  /**
   * Add a new column to be deleted.
   * 
   * @param column The column name.
   */
  public void addColumn(String column) {
    targetColumns.add(column);
  }

  /**
   * Set the name of the table.
   * 
   * @param tableName The name of the table.
   */
  public void setTableName(String tableName) {
    if (tableName.contains(".")) {
      String[] ksAndTableName = tableName.split("\\.");
      keyspace = ksAndTableName[0];
      this.tableName = ksAndTableName[1];
      keyspaceInc = true;
    } else {
      this.tableName = tableName;
    }
  }

  /**
   * Add a new {@link com.stratio.meta.core.structures.Relation} found in a WHERE clause.
   * 
   * @param relation The relation.
   */
  public void addRelation(Relation relation) {
    whereClauses.add(relation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("DELETE ");
    if (!targetColumns.isEmpty()) {
      sb.append("(").append(ParserUtils.stringList(targetColumns, ", ")).append(") ");
    }
    sb.append("FROM ");
    if (keyspaceInc) {
      sb.append(keyspace).append(".");
    }
    sb.append(tableName);
    if (!whereClauses.isEmpty()) {
      sb.append(" WHERE ");
      sb.append(ParserUtils.stringList(whereClauses, " AND "));
    }
    return sb.toString();
  }

  /** {@inheritDoc} */
  @Override
  public Result validate(MetadataManager metadata) {
    Result result = validateKeyspaceAndTable(metadata, sessionKeyspace);
    String effectiveKeyspace = getEffectiveKeyspace();
    if (keyspaceInc) {
      effectiveKeyspace = keyspace;
    }
    TableMetadata tableMetadata = null;
    if (!result.hasError()) {
      tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tableName);
      result = validateSelectionColumns(tableMetadata);
    }
    if (!result.hasError()) {
      result = validateWhereClause(tableMetadata);
    }

    return result;

  }

  /**
   * Validate that the columns specified in the select are valid by checking that the selection
   * columns exists in the table.
   * 
   * @param tableMetadata The associated {@link com.datastax.driver.core.TableMetadata}.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateWhereClause(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    Iterator<Relation> relations = whereClauses.iterator();
    while (!result.hasError() && relations.hasNext()) {
      Relation relation = relations.next();
      relation.updateTermClass(tableMetadata);
      if (Relation.TYPE_COMPARE == relation.getType()) {
        result = validateCompareRelation(relation, tableMetadata);
      } else if (Relation.TYPE_IN == relation.getType()) {
        // TODO: Check IN relation
        result = Result.createValidationErrorResult("IN clause not supported.");
      } else if (Relation.TYPE_TOKEN == relation.getType()) {
        // TODO: Check IN relation
        result = Result.createValidationErrorResult("TOKEN function not supported.");
      } else if (Relation.TYPE_BETWEEN == relation.getType()) {
        // TODO: Check IN relation
        result = Result.createValidationErrorResult("BETWEEN clause not supported.");
      }

    }
    return result;
  }

  private Result validateCompareRelation(Relation relation, TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    // Check comparison, =, >, <, etc.
    RelationCompare rc = RelationCompare.class.cast(relation);
    String column = rc.getIdentifiers().get(0);
    if (tableMetadata.getColumn(column) == null) {
      result =
          Result.createValidationErrorResult("Column " + column + " does not exist in table "
              + tableMetadata.getName());
    }

    ColumnMetadata cm = tableMetadata.getColumn(column);
    if (cm != null) {
      // relation.updateTermClass(tableMetadata);
      Term t = Term.class.cast(rc.getTerms().get(0));
      if (!tableMetadata.getColumn(column).getType().asJavaClass().equals(t.getTermClass())) {
        result =
            Result.createValidationErrorResult("Column " + column + " of type "
                + tableMetadata.getColumn(rc.getIdentifiers().get(0)).getType().asJavaClass()
                + " does not accept " + t.getTermClass() + " values (" + t.toString() + ")");
      }

      if (Boolean.class.equals(tableMetadata.getColumn(column).getType().asJavaClass())) {
        boolean supported = true;
        switch (rc.getOperator()) {
          case ">":
            supported = false;
            break;
          case "<":
            supported = false;
            break;
          case ">=":
            supported = false;
            break;
          case "<=":
            supported = false;
            break;
          default:
            break;
        }
        if (!supported) {
          result =
              Result.createValidationErrorResult("Operand " + rc.getOperator() + " not supported"
                  + " for column " + column + ".");
        }
      }
    } else {
      result =
          Result.createValidationErrorResult("Column " + column + " not found in " + tableName
              + " table.");
    }
    return result;
  }

  /**
   * Validate that the columns specified in the select are valid by checking that the selection
   * columns exists in the table.
   * 
   * @param tableMetadata The associated {@link com.datastax.driver.core.TableMetadata}.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateSelectionColumns(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();

    for (String c : targetColumns) {
      if (c.toLowerCase().startsWith("stratio")) {
        result =
            Result.createValidationErrorResult("Internal column " + c
                + " cannot be part of the WHERE " + "clause.");
      } else if (tableMetadata.getColumn(c) == null) {
        result =
            Result.createValidationErrorResult("Column " + c + " does not exists in table "
                + tableMetadata.getName());
      }
    }

    return result;
  }

  /**
   * Validate that a valid keyspace is present, and that the table does not exits unless
   * {@code ifNotExists} has been specified.
   * 
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides the
   *        required information.
   * @param targetKeyspace The target keyspace where the query will be executed.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateKeyspaceAndTable(MetadataManager metadata, String targetKeyspace) {
    Result result = QueryResult.createSuccessQueryResult();
    // Get the effective keyspace based on the user specification during the create
    // sentence, or taking the keyspace in use in the user session.
    String effectiveKeyspace = getEffectiveKeyspace();

    // Check that the keyspace and table exists.
    if (effectiveKeyspace == null || effectiveKeyspace.length() == 0) {
      result =
          Result.createValidationErrorResult("Target keyspace missing or no keyspace has been selected.");
    } else {
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
      if (ksMetadata == null) {
        result =
            Result.createValidationErrorResult("Keyspace " + effectiveKeyspace + " does not exist.");
      } else {
        TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tableName);
        if (tableMetadata == null) {
          result = Result.createValidationErrorResult("Table " + tableName + " does not exist.");
        }
      }

    }
    return result;
  }


  @Override
  public String translateToCQL() {
    return this.toString();
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
