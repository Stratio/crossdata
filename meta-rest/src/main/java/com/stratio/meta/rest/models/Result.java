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

package com.stratio.meta.rest.models;

public class Result {

    private String currentCatalog;

    private boolean lastResultSet;

    private boolean catalogChanged;

    private ResultSet resultSet;

    private int resultPage;

    private String queryId;

    public Result(String currentCatalog, boolean lastResultSet, boolean catalogChanged,
            ResultSet resultSet, int resultPage, String queryId) {
        super();
        this.currentCatalog = currentCatalog;
        this.lastResultSet = lastResultSet;
        this.catalogChanged = catalogChanged;
        this.resultSet = resultSet;
        this.resultPage = resultPage;
        this.queryId = queryId;
    }

    public Result() {
    }

    public String getCurrentCatalog() {
        return currentCatalog;
    }

    public void setCurrentCatalog(String currentCatalog) {
        this.currentCatalog = currentCatalog;
    }

    public boolean isLastResultSet() {
        return lastResultSet;
    }

    public void setLastResultSet(boolean lastResultSet) {
        this.lastResultSet = lastResultSet;
    }

    public boolean isCatalogChanged() {
        return catalogChanged;
    }

    public void setCatalogChanged(boolean catalogChanged) {
        this.catalogChanged = catalogChanged;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public int getResultPage() {
        return resultPage;
    }

    public void setResultPage(int resultPage) {
        this.resultPage = resultPage;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

}
