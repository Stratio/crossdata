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

package com.stratio.crossdata.core.planner.utils;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.GlobalIndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.*;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.query.SelectValidatedQuery;
import com.stratio.crossdata.core.statements.SelectStatement;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalIndexQueryBuilder {

    private static final Logger LOG = Logger.getLogger(GlobalIndexQueryBuilder.class);
    private GlobalIndexMetadata globalIndex;

    public GlobalIndexQueryBuilder(GlobalIndexMetadata globalIndex) {
        this.globalIndex = globalIndex;
    }

    public SelectValidatedQuery buildGlobalIndexQuery(SelectValidatedQuery originalQuery){
        LOG.info("Proces GlobalIndex query: " + originalQuery.toString());

        BaseQuery baseQuery = new BaseQuery(originalQuery.getQueryId(), originalQuery.getQuery(), globalIndex.getTableName().getCatalogName(), originalQuery.getSessionId());

        //Build select statement for globalIndex
        SelectStatement statement = buildSelectStatement();
        SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery,statement);
        statement.setWhere(originalQuery.getRelations());
        SelectValidatedQuery result = new SelectValidatedQuery(selectParsedQuery);
        result.setRelations(new ArrayList<AbstractRelation>());

        TableName tableName =globalIndex.getTableName();
        for (AbstractRelation rel: originalQuery.getRelations()){

            if(rel instanceof  FunctionRelation){
                FunctionRelation functionRelation = (FunctionRelation) rel;
                List<Selector> parameters = new ArrayList<>();
                for(Selector sel : functionRelation.getFunctionSelectors()){
                    if (sel instanceof ColumnSelector){
                        parameters.add(new ColumnSelector(new ColumnName(tableName, ((ColumnSelector) sel).getName().getName())));
                    }else if (sel instanceof StringSelector){
                        parameters.add(new StringSelector(tableName, ((StringSelector) sel).getValue()));
                    }else {
                        parameters.add(sel);
                    }
                }
                result.getRelations().add(new FunctionRelation(functionRelation.getFunctionName(), parameters ,tableName));
            }else if(rel instanceof  Relation){
                result.getRelations().add(rel);
            }
        }

        result.setTableMetadata(Arrays.asList(globalIndex.getTableMetadata()));

        result.optimizeQuery();
        return result;
    }

    private SelectStatement buildSelectStatement() {
        List<ColumnName> columns =  globalIndex.getTableMetadata().getPrimaryKey();
        List<Selector> selectors = new ArrayList();
        for (ColumnName column: columns){
            selectors.add(new ColumnSelector(column));
        }
        return new SelectStatement(new SelectExpression(selectors));
    }

    public SelectValidatedQuery buildMainlIndexQuery(SelectValidatedQuery originalQuery) {
         TableMetadata tableMetadata = originalQuery.getTableMetadata().get(0);

        ListSelector listSelector = new ListSelector(new ArrayList<Selector>());

        AbstractRelation relation = new Relation(new ColumnSelector(tableMetadata.getPrimaryKey().get(0)), Operator.IN, listSelector);

        originalQuery.setRelations(Arrays.asList(relation));
        originalQuery.getStatement().setWhere(Arrays.asList(relation));
        return originalQuery;
    }
}
