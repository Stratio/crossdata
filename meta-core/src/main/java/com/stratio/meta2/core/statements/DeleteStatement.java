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

package com.stratio.meta2.core.statements;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.statements.structures.relationships.RelationCompare;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.statements.structures.terms.Term;

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
  private TableName tableName = null;

  /**
   * The list of {@link com.stratio.meta.common.statements.structures.relationships.Relation} found in the WHERE clause.
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
    public void setTableName(TableName tableName) {
      this.tableName = tableName;
  }

  /**
   * Add a new {@link com.stratio.meta.common.statements.structures.relationships.Relation} found in a WHERE clause.
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
      sb.append("(").append(StringUtils.stringList(targetColumns, ", ")).append(") ");
    }
    sb.append("FROM ");
    if (catalogInc) {
      sb.append(catalog).append(".");
    }
    sb.append(tableName);
    if (!whereClauses.isEmpty()) {
      sb.append(" WHERE ");
      sb.append(StringUtils.stringList(whereClauses, " AND "));
    }
    return sb.toString();
  }

  /** {@inheritDoc} */
  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {

    Result result = validateCatalogAndTable(metadata, sessionCatalog);
    String effectiveCatalog = getEffectiveCatalog();

    TableMetadata tableMetadata = null;
    if (!result.hasError()) {
      tableMetadata = metadata.getTableMetadata(effectiveCatalog, tableName);
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
      //TODO Uncomment
      //relation.updateTermClass(tableMetadata);
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
    String column = rc.getIdentifiers().get(0).toString();
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
                + tableMetadata.getColumn(rc.getIdentifiers().get(0).toString()).getType().asJavaClass()
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
   * Validate that a valid catalog is present, and that the table does not exits unless
   * {@code ifNotExists} has been specified.
   * 
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides the
   *        required information.
   * @param targetCatalog The target catalog where the query will be executed.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateCatalogAndTable(MetadataManager metadata, String targetCatalog) {
    Result result = QueryResult.createSuccessQueryResult();
    // Get the effective catalog based on the user specification during the create
    // sentence, or taking the catalog in use in the user session.
    String effectiveCatalog = getEffectiveCatalog();

    // Check that the catalog and table exists.
    if (effectiveCatalog == null || effectiveCatalog.length() == 0) {
      result =
          Result.createValidationErrorResult("Target catalog missing or no catalog has been selected.");
    } else {
      CatalogMetadata ksMetadata = metadata.getCatalogMetadata(effectiveCatalog);
      if (ksMetadata == null) {
        result =
            Result.createValidationErrorResult("Catalog " + effectiveCatalog + " does not exist.");
      } else {
        TableMetadata tableMetadata = metadata.getTableMetadata(effectiveCatalog, tableName);
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

}
