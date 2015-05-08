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

package com.stratio.connector.inmemory.datastore.datatypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.stratio.crossdata.common.exceptions.ExecutionException;

public class Date extends AbstractInMemoryDataType {

    private Class<?> clazz = Date.class;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setParameters(Object... parameters){
        formatter = new SimpleDateFormat(String.class.cast(parameters[0]));
    }

    public Object convertStringToInMemoryDataType(String input) throws ExecutionException {
        java.util.Date output;
        try {
            output = formatter.parse(input);
        } catch (ParseException e) {
            throw new ExecutionException(e.getMessage());
        }
        return output;
    }

    public String convertInMemoryDataTypeToString(Object input){
        java.util.Date date = java.util.Date.class.cast(input);
        return formatter.format(date);
    }

}
