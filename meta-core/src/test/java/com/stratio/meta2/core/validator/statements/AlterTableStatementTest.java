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

package com.stratio.meta2.core.validator.statements;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetadataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.AlterTableStatement;
import com.stratio.meta2.core.validator.BasicValidatorTest;
import com.stratio.meta2.core.validator.Validator;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AlterTableStatementTest extends BasicValidatorTest {


  @Test
  public void alterTableAlterColumns() {
      String query = "ALTER TABLE demo.users ALTER demo.users.age TYPE BIGINT;";

      AlterTableStatement alterTableStatement=new AlterTableStatement(new TableName("demo","users"),new ColumnName("demo","users","age"),
          ColumnType.BIGINT,null,1);
      Validator validator=new Validator();

      BaseQuery baseQuery=new BaseQuery("alterTableId",query, new CatalogName("demo"));

      ParsedQuery parsedQuery=new MetadataParsedQuery(baseQuery,alterTableStatement);
      try {
          validator.validate(parsedQuery);
          Assert.assertFalse(false);
      } catch (ValidationException e) {
          Assert.assertTrue(true);
      } catch (IgnoreQueryException e) {
          Assert.assertTrue(true);
      }
  }

    @Test
    public void alterTableAddColumns() {
        String query = "ALTER TABLE demo.users ADD demo.users.new TYPE TEXT ;";

        AlterTableStatement alterTableStatement=new AlterTableStatement(new TableName("demo","users"),new ColumnName("demo","users","new"),
            ColumnType.VARCHAR,null,2);
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("alterTableId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetadataParsedQuery(baseQuery,alterTableStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertFalse(false);
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void alterTableWithOptions() {
        String query = "ALTER TABLE demo.users WITH comment='Users table to maintain users';";
        AlterTableStatement alterTableStatement=new AlterTableStatement(new TableName("demo","users"), null , null, "{'comment': 'Users table to maintain users'}", 4);

        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("alterTableId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetadataParsedQuery(baseQuery,alterTableStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertFalse(false);
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void alterTableDropColumns() {
        String query = "ALTER TABLE demo.users DROP demo.users.age;";

        AlterTableStatement alterTableStatement=new AlterTableStatement(new TableName("demo","users"),new ColumnName("demo","users","age"),
            ColumnType.BIGINT,null,3);
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("alterTableId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetadataParsedQuery(baseQuery,alterTableStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertFalse(false);
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }








}
