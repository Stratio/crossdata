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
import com.stratio.meta.common.statements.structures.assignations.Assignation;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.statements.structures.selectors.SelectorIdentifier;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.Option;
import com.stratio.meta.core.utils.CoreUtils;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.statements.structures.terms.FloatTerm;
import com.stratio.meta2.common.statements.structures.terms.GenericTerm;
import com.stratio.meta2.common.statements.structures.terms.IntegerTerm;
import com.stratio.meta2.common.statements.structures.terms.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that models an {@code UPDATE} statement from the META language.
 */
public class UpdateTableStatement extends MetaStatement {

  /**
   * The name of the table.
   */
  private String tableName;

  /**
   * Whether options are included.
   */
  private boolean optsInc;

  /**
   * The list of options.
   */
  private List<Option> options;

  /**
   * The list of assignations.
   */
  private List<Assignation> assignations;

  /**
   * The list of relations.
   */
  private List<Relation> whereClauses;

  /**
   * Whether conditions are included.
   */
  private boolean condsInc;

  /**
   * Map of conditions.
   */
  private Map<String, Term<?>> conditions;

  /**
   * Class constructor.
   * 
   * @param tableName The name of the table.
   * @param optsInc Whether options are included.
   * @param options The list of options.
   * @param assignations The list of assignations.
   * @param whereClauses The list of relations.
   * @param condsInc Whether conditions are included.
   * @param conditions The map of conditions.
   */
  public UpdateTableStatement(String tableName, boolean optsInc, List<Option> options,
      List<Assignation> assignations, List<Relation> whereClauses, boolean condsInc,
      Map<String, Term<?>> conditions) {
    this.command = false;
    if (tableName.contains(".")) {
      String[] ksAndTableName = tableName.split("\\.");
      catalog = ksAndTableName[0];
      this.tableName = ksAndTableName[1];
      catalogInc = true;
    } else {
      this.tableName = tableName;
    }

    this.optsInc = optsInc;

    // this.options = options;
    if (optsInc) {
      this.options = new ArrayList<>();
      for (Option opt : options) {
        opt.setNameProperty(opt.getNameProperty().toLowerCase());
        this.options.add(opt);
      }
    }

    this.assignations = assignations;
    this.whereClauses = whereClauses;
    this.condsInc = condsInc;
    this.conditions = conditions;
  }

  /**
   * Class constructor.
   * 
   * @param tableName The name of the table.
   * @param options The list of options.
   * @param assignations The list of assignations.
   * @param whereClauses The list of relations.
   * @param conditions The map of conditions.
   */
  public UpdateTableStatement(String tableName, List<Option> options, List<Assignation> assignations,
      List<Relation> whereClauses, Map<String, Term<?>> conditions) {
    this(tableName, true, options, assignations, whereClauses, true, conditions);
  }

  /**
   * Class constructor.
   * 
   * @param tableName The name of the table.
   * @param assignations The list of assignations.
   * @param whereClauses The list of relations.
   * @param conditions The map of conditions.
   */
  public UpdateTableStatement(String tableName, List<Assignation> assignations,
      List<Relation> whereClauses, Map<String, Term<?>> conditions) {
    this(tableName, false, null, assignations, whereClauses, true, conditions);
  }

  /**
   * Class constructor.
   * 
   * @param tableName The name of the table.
   * @param options The list of options.
   * @param assignations The list of assignations.
   * @param whereClauses The list of relations.
   */
  public UpdateTableStatement(String tableName, List<Option> options, List<Assignation> assignations,
      List<Relation> whereClauses) {
    this(tableName, true, options, assignations, whereClauses, false, null);
  }

  /**
   * Class constructor.
   * 
   * @param tableName The name of the table.
   * @param assignations The list of assignations.
   * @param whereClauses The list of relations.
   */
  public UpdateTableStatement(String tableName, List<Assignation> assignations,
      List<Relation> whereClauses) {
    this(tableName, false, null, assignations, whereClauses, false, null);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("UPDATE ");
    if (catalogInc) {
      sb.append(catalog).append(".");
    }
    sb.append(tableName);
    if (optsInc) {
      sb.append(" ").append("USING ");
      sb.append(StringUtils.stringList(options, " AND "));
    }
    sb.append(" ").append("SET ");
    sb.append(StringUtils.stringList(assignations, ", "));
    sb.append(" ").append("WHERE ");
    sb.append(StringUtils.stringList(whereClauses, " AND "));
    if (condsInc) {
      sb.append(" ").append("IF ");
      sb.append(ParserUtils.stringMap(conditions, " = ", " AND "));
    }
    return sb.toString();
  }

  @Override
  public String translateToCQL() {
    return this.toString();
  }

  /**
   * Validate the semantics of the current statement. This method checks the existing metadata to
   * determine that all referenced entities exists in the {@code targetKeyspace} and the types are
   * compatible with the assignations or comparisons.
   * 
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides the
   *        required information.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result =
        validateKeyspaceAndTable(metadata, sessionCatalog, catalogInc, catalog, tableName);
    if (!result.hasError()) {
      TableMetadata tableMetadata = metadata.getTableMetadata(getEffectiveCatalog(), tableName);

      if (optsInc) {
        result = validateOptions();
      }

      if (!result.hasError()) {
        result = validateAssignations(tableMetadata);
      }

      if (!result.hasError()) {
        /*
        result = validateWhereClauses(tableMetadata);
        */
      }

      if ((!result.hasError()) && condsInc) {
        result = validateConds(tableMetadata);
      }
    }
    return result;
  }

  private Result validateConds(TableMetadata tableMetadata) {
    updateTermClassesInConditions(tableMetadata);
    Result result = QueryResult.createSuccessQueryResult();
    for (String key : conditions.keySet()) {
      ColumnMetadata cm = tableMetadata.getColumn(key);
      if (cm != null) {
        if (!(cm.getType().asJavaClass() == conditions.get(key).getTermClass())) {
          result =
              Result.createValidationErrorResult("Column " + key + " should be type "
                  + cm.getType().asJavaClass().getSimpleName());
        }
      } else {
        result =
            Result.createValidationErrorResult("Column " + key + " was not found in table "
                + tableName);
      }
    }
    return result;
  }

  private void updateTermClassesInConditions(TableMetadata tableMetadata) {
    for (String ident : conditions.keySet()) {
      ColumnMetadata cm = tableMetadata.getColumn(ident);
      Term<?> term = conditions.get(ident);
      if ((cm != null) && (term != null)) {
        if (term instanceof Term) {
          if (CoreUtils.castForLongType(cm, term)) {
            conditions.put(ident, new IntegerTerm((Term<Long>) term));
          } else if (CoreUtils.castForDoubleType(cm, term)) {
            conditions.put(ident, new FloatTerm((Term<Double>) term));
          }
        }
      }
    }
  }

  private Result validateOptions() {
    Result result = QueryResult.createSuccessQueryResult();
    for (Option opt : options) {
      if (!("ttl".equalsIgnoreCase(opt.getNameProperty()) || "timestamp".equalsIgnoreCase(opt
          .getNameProperty()))) {
        result =
            Result.createValidationErrorResult("TIMESTAMP and TTL are the only accepted options.");
      }
    }
    for (Option opt: options) {
      if (opt.getProperties().getType() != GenericTerm.SIMPLE_TERM) {
        result = Result.createValidationErrorResult("TIMESTAMP and TTL must have a constant value.");
      } else {
        Term simpleTerm = (Term) opt.getProperties();
        if(!simpleTerm.isConstant()){
          result = Result.createValidationErrorResult("TIMESTAMP and TTL must have a constant value.");
        }
      }
    }
    return result;
  }

  /**
   * Check that the specified columns exist on the target table
   * 
   * @param tableMetadata Table metadata associated with the target table.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  private Result validateAssignations(TableMetadata tableMetadata) {
    updateTermClassesInAssignations(tableMetadata);
    Result result = QueryResult.createSuccessQueryResult();
    for (int index = 0; index < assignations.size(); index++) {
      Assignation assignment = assignations.get(index);

      ColumnName targetCol = assignment.getTargetColumn();

      // Check if identifier exists
      String colId = targetCol.getName();
      ColumnMetadata cm = tableMetadata.getColumn(colId);
      if (cm == null) {
        result =
            Result.createValidationErrorResult("Column " + colId
                + " not found in " + tableMetadata.getName() + ".");
        break;
      }

      // Check if column data type of the identifier is one of the supported types
      Class<?> idClazz = cm.getType().asJavaClass();
      if (!result.hasError()) {
        if (!CoreUtils.supportedTypes.contains(idClazz.getSimpleName().toLowerCase())) {
          result =
              Result.createValidationErrorResult("Column " + colId
                  + " is of type " + cm.getType().asJavaClass().getSimpleName()
                  + ", which is not supported yet.");
        }
      }

      // Check if identifier is simple, otherwise it refers to a collection, which are not supported
      // yet
      if (!result.hasError()) {
        if (cm.getType().isCollection()) {
          result = Result.createValidationErrorResult("Collections are not supported yet.");
        }
      }

      if (!result.hasError()) {
        GenericTerm valueAssign = assignment.getValue();
        if (valueAssign.getType() == GenericTerm.SIMPLE_TERM) {
          Term<?> valueTerm = (Term<?>) valueAssign;
          // Check data type between column of the identifier and term type of the statement
          Class<?> valueClazz = valueTerm.getTermClass();
          String valueClass = valueClazz.getSimpleName();
          if (!idClazz.getSimpleName().equalsIgnoreCase(valueClass)) {
            result =
                Result.createValidationErrorResult(cm.getName() + " and " + valueTerm.getTermValue()
                    + " are not compatible type.");
          }
        } else {
          result = Result.createValidationErrorResult("Collections are not supported yet.");
        }
      }
    }
    return result;
  }

  private void updateTermClassesInAssignations(TableMetadata tableMetadata) {
    for (Assignation assignment: assignations) {
      String ident = assignment.getTargetColumn().getName();
      ColumnMetadata cm = tableMetadata.getColumn(ident);
      Term<?> valueTerm = (Term<?>) assignment.getValue();
      if ((cm != null) && (valueTerm != null)) {
        if (valueTerm instanceof Term) {
          if (CoreUtils.castForLongType(cm, valueTerm)) {
            assignment.setValue(new IntegerTerm((Term<Long>) valueTerm));
          } else if (CoreUtils.castForDoubleType(cm, valueTerm)) {
            assignment.setValue(new FloatTerm((Term<Double>) valueTerm));
          }
        }
      }
    }
  }

  /*
  private Result validateWhereClauses(TableMetadata tableMetadata) {
    Result result = QueryResult.createSuccessQueryResult();
    for (Relation rel : whereClauses) {
      Term<?> term = rel.getTerms().get(0);
      Class<?> valueClazz = term.getTermClass();
      for (SelectorIdentifier id : rel.getIdentifiers()) {
        boolean foundAndSameType = false;
        for (ColumnMetadata cm : tableMetadata.getColumns()) {
          if (cm.getName().equalsIgnoreCase(id.toString())
              && (cm.getType().asJavaClass() == valueClazz)) {
            foundAndSameType = true;
            break;
          }
        }
        if (!foundAndSameType) {
          result =
              Result.createValidationErrorResult("Column " + id + " not found in "
                  + tableMetadata.getName());
          break;
        }
      }
      if (result.hasError()) {
        break;
      }
    }
    return result;
  }
  */

}
