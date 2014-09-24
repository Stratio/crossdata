/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.core.statements;

import com.stratio.meta.common.result.Result;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.Option;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Class that models an {@code INSERT INTO} statement from the META language.
 */
public class InsertIntoStatement extends StorageStatement {

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
  private TableName tableName;

  /**
   * The list of columns to be assigned.
   */
  private List<ColumnName> ids;

  /**
   * A {@link com.stratio.meta2.core.statements.SelectStatement} to retrieve data if the insert type
   * is matches {@code TYPE_SELECT_CLAUSE}.
   */
  private SelectStatement selectStatement;

  /**
   * A list of {@link com.stratio.meta2.common.statements.structures.selectors.Selector} with the
   * literal values to be assigned if the insert type matches {@code TYPE_VALUES_CLAUSE}.
   */
  private List<Selector> cellValues;

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
   * @param selectStatement a {@link com.stratio.meta2.core.statements.InsertIntoStatement}
   * @param cellValues List of
   *        {@link com.stratio.meta2.common.statements.structures.selectors.Selector} to insert.
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   * @param optsInc Boolean that indicates if there is options in the query.
   * @param options Query options.
   * @param typeValues Integer that indicates if values come from insert or select.
   */
  public InsertIntoStatement(TableName tableName, List<ColumnName> ids,
                             SelectStatement selectStatement, List<Selector> cellValues, boolean ifNotExists,
                             boolean optsInc, List<Option> options, int typeValues) {
    this.command = false;
    this.tableName = tableName;
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
   * @param selectStatement a {@link com.stratio.meta2.core.statements.InsertIntoStatement}
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   * @param options Query options.
   */
  public InsertIntoStatement(TableName tableName, List<ColumnName> ids,
                             SelectStatement selectStatement, boolean ifNotExists, List<Option> options) {
    this(tableName, ids, selectStatement, null, ifNotExists, true, options, 1);
  }

  /**
   * InsertIntoStatement constructor comes from INSERT INTO .. VALUES .. with options.
   *
   * @param tableName Tablename target.
   * @param ids List of name of fields in the table.
   * @param cellValues List of
   *        {@link com.stratio.meta2.common.statements.structures.selectors.Selector} to insert.
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   * @param options Query options.
   */
  public InsertIntoStatement(TableName tableName, List<ColumnName> ids, List<Selector> cellValues,
                             boolean ifNotExists, List<Option> options) {
    this(tableName, ids, null, cellValues, ifNotExists, true, options, 2);
  }

  /**
   * InsertIntoStatement constructor comes from INSERT INTO .. SELECT .. without options.
   *
   * @param tableName Tablename target.
   * @param ids List of name of fields in the table.
   * @param selectStatement a {@link com.stratio.meta2.core.statements.InsertIntoStatement}
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   */
  public InsertIntoStatement(TableName tableName, List<ColumnName> ids,
                             SelectStatement selectStatement, boolean ifNotExists) {
    this(tableName, ids, selectStatement, null, ifNotExists, false, null, 1);
  }

  /**
   * InsertIntoStatement constructor comes from INSERT INTO .. VALUES .. without options.
   *
   * @param tableName Tablename target.
   * @param ids List of name of fields in the table.
   * @param cellValues List of
   *        {@link com.stratio.meta2.common.statements.structures.selectors.Selector} to insert.
   * @param ifNotExists Boolean that indicates if IF NOT EXISTS clause is included in the query.
   */
  public InsertIntoStatement(TableName tableName, List<ColumnName> ids, List<Selector> cellValues,
                             boolean ifNotExists) {
    this(tableName, ids, null, cellValues, ifNotExists, false, null, 2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("INSERT INTO ");
    if (catalogInc) {
      sb.append(catalog).append(".");
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
        validateCatalogAndTable(metadata, sessionCatalog, catalogInc, catalog, tableName);
    if (!result.hasError()) {
      CatalogName effectiveCatalog = getEffectiveCatalog();

      /*TableMetadata tableMetadata = metadata.getTableMetadata(effectiveCatalog, tableName);

      if (typeValues == TYPE_SELECT_CLAUSE) {
        result = Result.createValidationErrorResult("INSERT INTO with subqueries not supported.");
      } else {
        result = validateColumns(tableMetadata);
      }*/
    }
    return result;
  }

  public void updateTermClass(TableMetadata tableMetadata) {
    throw new UnsupportedOperationException();
    /*for (int i = 0; i < ids.size(); i++) {
      Class<? extends Comparable<?>> dataType =
          (Class<? extends Comparable<?>>) tableMetadata.getColumns().get(ids.get(i)).getColumnType()
              .asJavaClass();
      if (cellValues.get(i) instanceof Term) {
        Term<?> term = (Term<?>) cellValues.get(i);
        if (dataType == Integer.class && term.getTermClass() == Long.class) {
          cellValues.set(i, new IntegerTerm((Term<Long>) term));
        } else if (dataType == Float.class && term.getTermClass() == Double.class) {
          cellValues.set(i, new FloatTerm((Term<Double>) term));
        }
      }
    }*/
  }

  /**
   * Check that the specified columns exist on the target table and that the semantics of the
   * assigned values match.
   *
   * @param tableMetadata Table metadata associated with the target table.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateColumns(TableMetadata tableMetadata) {
    throw new UnsupportedOperationException();
    /*Result result = QueryResult.createSuccessQueryResult();

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
          cm = tableMetadata.getColumns().get(ids.get(index));
          if (cm != null) {
            Term<?> t = Term.class.cast(cellValues.get(index));
            if (!cm.getColumnType().asJavaClass().equals(t.getTermClass())) {
              result =
                  Result.createValidationErrorResult("Column " + ids.get(index) + " of type "
                      + cm.getColumnType().asJavaClass() + " does not accept " + t.getTermClass()
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
    return result;*/
  }

  /**
   * Iterate over InsertIntoStatement#cellValues and add
   * values to Insert object to be translated in CQL.
   *
   * @param insertStmt
   */
  /*private void iterateValuesAndInsertThem(Insert insertStmt) {
    Iterator<Selector> it = this.cellValues.iterator();
    for (ColumnName id: this.ids) {
      Selector genericTerm = it.next();
      if (genericTerm.toString().matches("[0123456789.]+")) {
        insertStmt = insertStmt.value(id.getName(), Integer.parseInt(genericTerm.toString()));
      } else if (genericTerm.toString().contains("-")) {
        insertStmt = insertStmt.value(id.getName(), UUID.fromString(genericTerm.toString()));
      } else if ("true".equalsIgnoreCase(genericTerm.toString())
                 || "false".equalsIgnoreCase(genericTerm.toString())) {
        insertStmt = insertStmt.value(id.getName(), Boolean.valueOf(genericTerm.toString()));
      } else {
        insertStmt = insertStmt.value(id.getName(), genericTerm.toString());
      }
    }
  }*/

  /**
   * Check the options for InsertIntoStatement.
   *
   * //@param insertStmt a Insert where insert the
   *        options.
   * //@return a Options
   */
  /*private Insert.Options checkOptions(Insert insertStmt) {
    Insert.Options optionsStmt = null;

    if (this.optsInc) {
      for (Option option : this.options) {
        if (option.getFixedOption() == Option.OPTION_PROPERTY) {
          if ("ttl".equalsIgnoreCase(((StringSelector) option.getNameProperty()).getValue())) {
            optionsStmt =
                insertStmt.using(QueryBuilder.ttl(Integer.parseInt(option.getProperties()
                                                                       .toString())));
          } else if ("timestamp".equalsIgnoreCase(((StringSelector) option.getNameProperty()).getValue())) {
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
  }*/

  @Override
  public ValidationRequirements getValidationRequirements() {
    return new ValidationRequirements().add(Validation.MUST_EXIST_CATALOG).add(Validation.MUST_EXIST_TABLE);
  }
}
