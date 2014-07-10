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

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.common.statements.structures.terms.FloatTerm;
import com.stratio.meta.common.statements.structures.terms.IntegerTerm;
import com.stratio.meta.core.structures.Option;
import com.stratio.meta.common.statements.structures.terms.Term;
import com.stratio.meta.common.statements.structures.terms.ValueCell;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models an {@code INSERT INTO} statement from the META language.
 */
public class InsertIntoStatement extends MetaStatement {

  /**
   * Constant to define an {@code INSERT INTO} that takes the input values from a {@code SELECT}
   * subquery.
   */
  public static final int TYPE_SELECT_CLAUSE = 1;

  /**
   * Constant to define an {@code INSERT INTO} that takes literal values as input.
   */
  public static final int TYPE_VALUES_CLAUSE = 2;

  /**
   * The name of the target table.
   */
  private String tableName;

  /**
   * The list of columns to be assigned.
   */
  private List<String> ids;

  /**
   * A {@link com.stratio.meta.core.statements.SelectStatement} to retrieve data if the insert type
   * is matches {@code TYPE_SELECT_CLAUSE}.
   */
  private SelectStatement selectStatement;

  /**
   * A list of {@link com.stratio.meta.common.statements.structures.terms.ValueCell} with the literal values to be
   * assigned if the insert type matches {@code TYPE_VALUES_CLAUSE}.
   */
  private List<ValueCell<?>> cellValues;

  /**
   * Indicates if exists "IF NOT EXISTS" clause.
   */
  private boolean ifNotExists;

  /**
   * Indicates if there is options in the statement..
   */
  private boolean optsInc;

  /**
   * List of options included in the statement.
   */
  private List<Option> options;

  /**
   * Type of Insert statement. {@link InsertIntoStatement#TYPE_SELECT_CLAUSE} or
   * {@link InsertIntoStatement#TYPE_VALUES_CLAUSE}.
   */
  private int typeValues;

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(SelectStatement.class);

  /**
   * InsertIntoStatement general constructor.
   * 
   * @param tableName Tablename target.
   * @param ids List of name of fields in the table.
   * @param selectStatement a {@link com.stratio.meta.core.statements.InsertIntoStatement}
   * @param cellValues List of {@link com.stratio.meta.common.statements.structures.terms.ValueCell} to insert.
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   * @param optsInc Boolean that indicates if there is options in the query.
   * @param options Query options.
   * @param typeValues Integer that indicates if values come from insert or select.
   */
  public InsertIntoStatement(String tableName, List<String> ids, SelectStatement selectStatement,
      List<ValueCell<?>> cellValues, boolean ifNotExists, boolean optsInc, List<Option> options,
      int typeValues) {
    this.command = false;
    this.tableName = tableName;
    if (tableName.contains(".")) {
      String[] ksAndTableName = tableName.split("\\.");
      keyspace = ksAndTableName[0];
      this.tableName = ksAndTableName[1];
      keyspaceInc = true;
    }
    this.ids = ids;
    this.selectStatement = selectStatement;
    this.cellValues = cellValues;
    this.ifNotExists = ifNotExists;
    this.optsInc = optsInc;
    this.options = options;
    this.typeValues = typeValues;
  }

  /**
   * InsertIntoStatement constructor comes from INSERT INTO .. SELECT .. with options.
   * 
   * @param tableName Tablename target.
   * @param ids List of name of fields in the table.
   * @param selectStatement a {@link com.stratio.meta.core.statements.InsertIntoStatement}
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   * @param options Query options.
   */
  public InsertIntoStatement(String tableName, List<String> ids, SelectStatement selectStatement,
      boolean ifNotExists, List<Option> options) {
    this(tableName, ids, selectStatement, null, ifNotExists, true, options, 1);
  }

  /**
   * InsertIntoStatement constructor comes from INSERT INTO .. VALUES .. with options.
   * 
   * @param tableName Tablename target.
   * @param ids List of name of fields in the table.
   * @param cellValues List of {@link com.stratio.meta.common.statements.structures.terms.ValueCell} to insert.
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   * @param options Query options.
   */
  public InsertIntoStatement(String tableName, List<String> ids, List<ValueCell<?>> cellValues,
      boolean ifNotExists, List<Option> options) {
    this(tableName, ids, null, cellValues, ifNotExists, true, options, 2);
  }

  /**
   * InsertIntoStatement constructor comes from INSERT INTO .. SELECT .. without options.
   * 
   * @param tableName Tablename target.
   * @param ids List of name of fields in the table.
   * @param selectStatement a {@link com.stratio.meta.core.statements.InsertIntoStatement}
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   */
  public InsertIntoStatement(String tableName, List<String> ids, SelectStatement selectStatement,
      boolean ifNotExists) {
    this(tableName, ids, selectStatement, null, ifNotExists, false, null, 1);
  }

  /**
   * InsertIntoStatement constructor comes from INSERT INTO .. VALUES .. without options.
   * 
   * @param tableName Tablename target.
   * @param ids List of name of fields in the table.
   * @param cellValues List of {@link com.stratio.meta.common.statements.structures.terms.ValueCell} to insert.
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   */
  public InsertIntoStatement(String tableName, List<String> ids, List<ValueCell<?>> cellValues,
      boolean ifNotExists) {
    this(tableName, ids, null, cellValues, ifNotExists, false, null, 2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("INSERT INTO ");
    if (keyspaceInc) {
      sb.append(keyspace).append(".");
    }
    sb.append(tableName).append(" (");
    sb.append(StringUtils.stringList(ids, ", ")).append(") ");
    if (typeValues == TYPE_SELECT_CLAUSE) {
      sb.append(selectStatement.toString());
    } else {
      sb.append("VALUES (");
      sb.append(StringUtils.stringList(cellValues, ", "));
      sb.append(")");
    }
    if (ifNotExists) {
      sb.append(" IF NOT EXISTS");
    }
    if (optsInc) {
      sb.append(" USING ");
      sb.append(StringUtils.stringList(options, " AND "));
    }
    return sb.toString();
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result =
        validateKeyspaceAndTable(metadata, sessionKeyspace, keyspaceInc, keyspace, tableName);
    if (!result.hasError()) {
      String effectiveKeyspace = getEffectiveKeyspace();

      TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tableName);

      if (typeValues == TYPE_SELECT_CLAUSE) {
        result = Result.createValidationErrorResult("INSERT INTO with subqueries not supported.");
      } else {
        result = validateColumns(tableMetadata);
      }
    }
    return result;
  }

  public void updateTermClass(TableMetadata tableMetadata) {
    for (int i = 0; i < ids.size(); i++) {
      Class<? extends Comparable<?>> dataType =
          (Class<? extends Comparable<?>>) tableMetadata.getColumn(ids.get(i)).getType()
              .asJavaClass();
      if (cellValues.get(i) instanceof Term) {
        Term<?> term = (Term<?>) cellValues.get(i);
        if (dataType == Integer.class && term.getTermClass() == Long.class) {
          cellValues.set(i, new IntegerTerm((Term<Long>) term));
        } else if (dataType == Float.class && term.getTermClass() == Double.class) {
          cellValues.set(i, new FloatTerm((Term<Double>) term));
        }
      }
    }
  }

  /**
   * Check that the specified columns exist on the target table and that the semantics of the
   * assigned values match.
   * 
   * @param tableMetadata Table metadata associated with the target table.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateColumns(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();

    // Validate target column names
    for (String c : ids) {
      if (c.toLowerCase().startsWith("stratio")) {
        result =
            Result.createValidationErrorResult("Cannot insert data into column " + c
                + " reserved for internal use.");
      }
    }
    if (!result.hasError()) {
      ColumnMetadata cm = null;
      if (cellValues.size() == ids.size()) {
        updateTermClass(tableMetadata);
        for (int index = 0; index < cellValues.size(); index++) {
          cm = tableMetadata.getColumn(ids.get(index));
          if (cm != null) {
            Term<?> t = Term.class.cast(cellValues.get(index));
            if (!cm.getType().asJavaClass().equals(t.getTermClass())) {
              result =
                  Result.createValidationErrorResult("Column " + ids.get(index) + " of type "
                      + cm.getType().asJavaClass() + " does not accept " + t.getTermClass()
                      + " values (" + cellValues.get(index) + ")");
            }
          } else {
            result =
                Result.createValidationErrorResult("Column " + ids.get(index) + " not found in "
                    + tableMetadata.getName());
          }
        }
      } else {
        result = Result.createValidationErrorResult("Number of columns and values does not match.");
      }
    }
    return result;
  }

  @Override
  public String translateToCQL() {
    StringBuilder sb = new StringBuilder("INSERT INTO ");
    if (keyspaceInc) {
      sb.append(keyspace).append(".");
    }
    sb.append(tableName).append(" (");
    sb.append(StringUtils.stringList(ids, ", "));
    sb.append(") ");
    if (typeValues == TYPE_SELECT_CLAUSE) {
      sb.append(selectStatement.toString());
    }
    if (typeValues == TYPE_VALUES_CLAUSE) {
      sb.append("VALUES (");
      sb.append(ParserUtils.addSingleQuotesToString(StringUtils.stringList(cellValues, ", "), ","));
      sb.append(")");
    }
    if (ifNotExists) {
      sb.append(" IF NOT EXISTS");
    }
    if (optsInc) {
      sb.append(" USING ");
      sb.append(StringUtils.stringList(options, " AND "));
    }
    return sb.append(";").toString();
  }

  @Override
  public Statement getDriverStatement() {
    if (this.typeValues == TYPE_SELECT_CLAUSE) {
      return null;
    }

    Insert insertStmt =
        this.keyspaceInc ? QueryBuilder.insertInto(this.keyspace, this.tableName) : QueryBuilder
            .insertInto(this.tableName);

    try {
      iterateValuesAndInsertThem(insertStmt);
    } catch (Exception ex) {
      return null;
    }

    if (this.ifNotExists) {
      insertStmt = insertStmt.ifNotExists();
    }

    Insert.Options optionsStmt = checkOptions(insertStmt);

    return optionsStmt == null ? insertStmt : optionsStmt;
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

  /**
   * Iterate over {@link com.stratio.meta.core.statements.InsertIntoStatement#cellValues} and add
   * values to {@link com.datastax.driver.core.querybuilder.Insert} object to be translated in CQL.
   * 
   * @param insertStmt
   */
  private void iterateValuesAndInsertThem(Insert insertStmt) {
    Iterator<ValueCell<?>> it = this.cellValues.iterator();
    for (String id : this.ids) {
      ValueCell<?> valueCell = it.next();
      if (valueCell.toString().matches("[0123456789.]+")) {
        insertStmt = insertStmt.value(id, Integer.parseInt(valueCell.getStringValue()));
      } else if (valueCell.toString().contains("-")) {
        insertStmt = insertStmt.value(id, UUID.fromString(valueCell.getStringValue()));
      } else if ("true".equalsIgnoreCase(valueCell.toString())
          || "false".equalsIgnoreCase(valueCell.toString())) {
        insertStmt = insertStmt.value(id, Boolean.valueOf(valueCell.toString()));
      } else {
        insertStmt = insertStmt.value(id, valueCell.getStringValue());
      }
    }
  }

  /**
   * Check the options for InsertIntoStatement.
   * 
   * @param insertStmt a {@link com.datastax.driver.core.querybuilder.Insert} where insert the
   *        options.
   * @return a {@link com.datastax.driver.core.querybuilder.Insert.Options}
   */
  private Insert.Options checkOptions(Insert insertStmt) {
    Insert.Options optionsStmt = null;

    if (this.optsInc) {
      for (Option option : this.options) {
        if (option.getFixedOption() == Option.OPTION_PROPERTY) {
          if ("ttl".equalsIgnoreCase(option.getNameProperty())) {
            optionsStmt =
                insertStmt.using(QueryBuilder.ttl(Integer.parseInt(option.getProperties()
                    .toString())));
          } else if ("timestamp".equalsIgnoreCase(option.getNameProperty())) {
            optionsStmt =
                insertStmt.using(QueryBuilder.timestamp(Integer.parseInt(option.getProperties()
                    .toString())));
          } else {
            LOG.warn("Unsupported option: " + option.getNameProperty());
          }
        }
      }
    }

    return optionsStmt;
  }
}
