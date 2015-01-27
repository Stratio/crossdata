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

import java.util.HashMap;
import java.util.Map;

/**
 * Selector Helper class that implements a converter of selector map to String map.
 */
public final class SelectorHelper {

    /**
     * Constructor class.
     */
    private SelectorHelper(){

    }

    /**
     * Convert a map of selector to a map of strings.
     * @param selectorsMap The map of selectors.
     * @return A map of strings.
     */
    public static Map<String, String> convertSelectorMapToStringMap(Map<Selector, Selector> selectorsMap) {
        Map<String, String> stringsMap = new HashMap<>();
        for(Map.Entry<Selector,Selector> entry: selectorsMap.entrySet()){
            stringsMap.put(entry.getKey().getStringValue(), entry.getValue().getStringValue());
        }
        return stringsMap;
    }

}
