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

package com.stratio.connector.inmemory.datastore.functions;

import java.util.Map;

import com.stratio.connector.inmemory.datastore.datatypes.SimpleValue;
import com.stratio.connector.inmemory.datastore.selector.InMemoryColumnSelector;
import com.stratio.connector.inmemory.datastore.selector.InMemoryFunctionSelector;
import com.stratio.connector.inmemory.datastore.selector.InMemoryLiteralSelector;
import com.stratio.connector.inmemory.datastore.selector.InMemorySelector;

/**
 * Concatenation function.
 */
public class ConcatFunction extends AbstractInMemoryFunction{

    @Override public String apply(Map<String, Integer> columnIndex, SimpleValue[] row) throws Exception {
        StringBuilder sb = new StringBuilder();
        for(InMemorySelector selector : arguments){
            if(InMemoryFunctionSelector.class.isInstance(selector)){
                AbstractInMemoryFunction f = InMemoryFunctionSelector.class.cast(selector).getFunction();
                sb.append(f.apply(columnIndex, row).toString());
            }else if(InMemoryColumnSelector.class.isInstance(selector)){
                sb.append(row[columnIndex.get(selector.getName())].getValue());
            }else if(InMemoryLiteralSelector.class.isInstance(selector)){
                sb.append(selector.getName());
            }
        }
        return sb.toString();
    }

}
