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

import com.stratio.meta.common.statements.structures.ColumnName;
import com.stratio.meta.common.statements.structures.TableName;
import com.stratio.meta.common.statements.structures.assignations.Assignation;

import java.util.List;

public interface IStatement {
  /**
   * Get the name of the catalogs involved the statement to be executed.
   * @return A list of catalog names.
   */
  public List<String> getCatalogs();

  /**
   * Get the name of the tables involved in the statement to be executed. The names may be
   * fully qualified or not.
   * @return A list of table names.
   */
  public List<TableName> getTables();

  /**
   * Get the name of the columns involved in the statement to be executed. The names may contain
   * the table name.
   * @return A list of column names.
   */
  public List<ColumnName> getColumns();

  /**
   * Get the list of Assignations involved in the statement to be executed. An assignation may
   * represent inserting a value in a column, comparing a column with a value, etc.
   * @return A list of Assignation.
   */
  public List<Assignation> getAssignations();

  /**
   * Check whether the IF EXISTS clause has been used.
   * @return True if used.
   */
  public boolean getIfExists();

  /**
   * Check whether the IT NOT EXISTS has been used.
   * @return True if used.
   */
  public boolean getIfNotExists();
}
