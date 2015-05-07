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

package com.stratio.connector.inmemory.datastore.structures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory function selector.
 */
public class InMemoryFunctionSelector extends InMemorySelector {

    /**
     * List of arguments.
     */
    private final List<InMemorySelector> arguments;

    /**
     * Map of functions associating function name and implementing class.
     */
    private static final Map<String, Class> functions = new HashMap<>();

    static {
        //Aggregations
        functions.put("count", CountFunction.class);
        //Simple functions
        functions.put("now", NowFunction.class);
        functions.put("toUpper", ToUpperFunction.class);
        functions.put("concat", ConcatFunction.class);
    }

    /**
     * Class constructor.
     *
     * @param name The selector name.
     * @param arguments The function arguments.
     */
    public InMemoryFunctionSelector(String name, List<InMemorySelector> arguments) {
        super(name);
        this.arguments = arguments;
    }

    /**
     * Get the implementing function.
     * @return A {@link AbstractInMemoryFunction}.
     * @throws Exception If the function cannot be defined.
     */
    public AbstractInMemoryFunction getFunction() throws Exception{
        AbstractInMemoryFunction f = ((AbstractInMemoryFunction) functions.get(this.getName()).newInstance());
        f.setArguments(this.arguments);
        return f;
    }
}
