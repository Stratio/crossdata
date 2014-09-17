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
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetaDataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.DropCatalogStatement;
import com.stratio.meta2.core.statements.DropTableStatement;
import com.stratio.meta2.core.validator.BasicValidatorTest;
import com.stratio.meta2.core.validator.Validator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DropTableStatementTest extends BasicValidatorTest {


    @Test
    public void validateOk(){
        String query = "DROP TABLE demo.users;";
        DropTableStatement dropTableStatement=new DropTableStatement(new TableName("demo","users"),true);
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("dropTableid",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetaDataParsedQuery(baseQuery,dropTableStatement);
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
    public void validateIfNotExists(){
        String query = "DROP TABLE IF EXISTS unknown;";
        DropTableStatement dropTableStatement=new DropTableStatement(new TableName("demo","users"),true);
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("dropTableid",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetaDataParsedQuery(baseQuery,dropTableStatement);
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
