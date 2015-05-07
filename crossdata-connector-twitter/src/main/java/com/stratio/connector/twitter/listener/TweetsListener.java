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

package com.stratio.connector.twitter.listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import com.stratio.crossdata.common.connector.IResultHandler;
import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.statements.structures.Selector;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TweetsListener implements StatusListener {

    private final String queryId;
    private final IResultHandler resultHandler;
    private final TableMetadata tableMetadata;
    private final List<Selector> outputSelectorOrder;
    private final long durationInSeconds;
    private final List<Status> tweets = new ArrayList<>();
    private DateTime lastDelivery = new DateTime();
    private int pageCount = 0;

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(TweetsListener.class);

    public TweetsListener(
            String queryId,
            IResultHandler resultHandler,
            TableMetadata tableMetadata,
            List<Selector> outputSelectorOrder,
            long durationInMilliseconds) {
        this.queryId = queryId;
        this.resultHandler = resultHandler;
        this.tableMetadata = tableMetadata;
        this.outputSelectorOrder = outputSelectorOrder;
        this.durationInSeconds = (durationInMilliseconds / 1000);
        lastDelivery = new DateTime();
    }

    @Override
    public void onStatus(Status status) {
        tweets.add(status);
        DateTime now = new DateTime();
        if(Seconds.secondsBetween(lastDelivery, now).getSeconds() > durationInSeconds){
            createResult();
            lastDelivery = now;
            pageCount++;
            tweets.clear();
        }
    }

    private void createResult() {
        ResultSet resultSet = new ResultSet();
        List<ColumnMetadata> cm = new ArrayList<>();
        for(Status tweet: tweets){
            Row row = new Row();
            for(Selector selector: outputSelectorOrder){
                String alias = selector.getAlias();
                if((alias == null) || (alias.isEmpty())){
                    alias = selector.getColumnName().getAlias();
                }
                if((alias == null) || (alias.isEmpty())){
                    alias = selector.getColumnName().getName();
                }
                Cell cell = new Cell(null);
                try {
                    Method method;
                    ColumnMetadata column = tableMetadata.getColumns().get(selector.getColumnName());
                    if(column.getColumnType().getDataType() == DataType.BOOLEAN){
                        method = tweet.getClass().getDeclaredMethod("is" + WordUtils.capitalize(alias));
                    } else {
                        method = tweet.getClass().getDeclaredMethod("get" + WordUtils.capitalize(alias));
                    }
                    method.setAccessible(true);
                    Object result = method.invoke(tweet);
                    cell = new Cell(result);
                    if((resultSet.getColumnMetadata() == null) || resultSet.getColumnMetadata().isEmpty()){
                        ColumnName columnName = new ColumnName(selector.getTableName(), alias);
                        Object[] parameters = new Object[]{};

                        ColumnType ct = column.getColumnType();
                        ColumnMetadata columnMetadata = new ColumnMetadata(
                                columnName,
                                parameters,
                                ct);
                        cm.add(columnMetadata);
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    LOG.error(e);
                    resultHandler.processException(queryId, new ExecutionException(e.getMessage()));
                }
                row.addCell(alias, cell);
            }
            resultSet.add(row);
            if((resultSet.getColumnMetadata() == null) || resultSet.getColumnMetadata().isEmpty()){
                resultSet.setColumnMetadata(cm);
            }
        }
        QueryResult queryResult = QueryResult.createQueryResult(queryId, resultSet, pageCount, false);
        LOG.info("Sending result with " + resultSet.size() + " tweets");
        resultHandler.processResult(queryResult);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        //LOG.debug("DeletionNotice = " + statusDeletionNotice);
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        LOG.info("TrackLimitationNotice = " + i);
    }

    @Override
    public void onScrubGeo(long l, long l2) {
        LOG.info("ScrubGeo = " + l + ", " + l2);
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
        LOG.warn("StallWarning = " + stallWarning);
    }

    @Override
    public void onException(Exception e) {
        LOG.error(e);
        resultHandler.processException(queryId, new ExecutionException(e.getMessage()));
    }
}
