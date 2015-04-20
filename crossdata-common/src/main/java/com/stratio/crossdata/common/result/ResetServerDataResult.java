/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lcisneros on 17/04/15.
 */
public class ResetServerDataResult extends Result {

    private CommandResult result;

    private List<Object> queries = new ArrayList<>();

    public ResetServerDataResult(CommandResult result) {
        this.result = result;
    }

    public CommandResult getResult() {
        return result;
    }

    public List<Object> getQueries() {
        return queries;
    }

    @Override
    public String toString() {
        return "ResetServerDataResult{" +
                "result=" + result +
                ", queries=" + queries +
                '}';
    }
}
