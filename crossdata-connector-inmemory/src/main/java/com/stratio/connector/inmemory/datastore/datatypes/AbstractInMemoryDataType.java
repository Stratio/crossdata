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

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.exceptions.ExecutionException;

public abstract class AbstractInMemoryDataType {

    /**
     * Class logger.
     */
    protected static final Logger LOG = Logger.getLogger(AbstractInMemoryDataType.class);

    private static final Set<String> SUPPORTED_TYPES = new HashSet<>();
    static{
        SUPPORTED_TYPES.add(Boolean.class.getSimpleName());
        SUPPORTED_TYPES.add(String.class.getSimpleName());
        SUPPORTED_TYPES.add(Float.class.getSimpleName());
        SUPPORTED_TYPES.add(Integer.class.getSimpleName());
        SUPPORTED_TYPES.add(Double.class.getSimpleName());
        SUPPORTED_TYPES.add(Long.class.getSimpleName());
    }
    public abstract Class<?> getClazz();

    public abstract void setClazz(Class<?> clazz);

    public abstract void setParameters(Object... parameters);

    public abstract Object convertStringToInMemoryDataType(String input) throws ExecutionException;

    public abstract String convertInMemoryDataTypeToString(Object input);

    public static AbstractInMemoryDataType castToNativeDataType(String dataType)  throws ExecutionException{
        AbstractInMemoryDataType nativeDataType = null;
        if(dataType != null){
            if(dataType.equals("Date")){
                nativeDataType = new Date();
            }else if (!SUPPORTED_TYPES.contains(dataType)) {
                throw new ExecutionException("You have an error in your SQL syntax, the type '" + dataType + "' is not supported");
            }
        }
        return nativeDataType;
    }

}
