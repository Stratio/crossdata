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

package com.stratio.crossdata.common.statements.structures;

import java.util.Arrays;
import java.util.LinkedList;

import com.stratio.crossdata.common.data.FunctionName;
import com.stratio.crossdata.common.metadata.ColumnType;

public class Tuple {
    final private FunctionName name;
    private LinkedList<ColumnType> input;
    private LinkedList<ColumnType> output;

    public Tuple(FunctionName name) {
        this.name = name;
    }

    public Tuple(FunctionName name, LinkedList<ColumnType> input, LinkedList<ColumnType> output) {
        this.name = name;
        this.input = input;
        this.output = output;
    }

    public void setInput(ColumnType... input) {
        this.input = new LinkedList<>(Arrays.asList(input));
    }

    public void setOutput(ColumnType... output) {
        this.output = new LinkedList<>(Arrays.asList(output));
    }

    public FunctionName getName() {
        return name;
    }

    public LinkedList<ColumnType> getInput() {
        return input;
    }

    public LinkedList<ColumnType> getOutput() {
        return output;
    }
}
