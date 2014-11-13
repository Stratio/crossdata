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

package com.stratio.crossdata.core.utils;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.core.metadata.MetadataManager;

/**
 * Core Utils Class.
 */
public final class CoreUtils {

    private CoreUtils() {
    }

    /**
     * Constructor.
     * @return
     */
    public static CoreUtils create(){
        return new CoreUtils();
    }

    /**
     * Convert Selector to Object.
     * @param selector The Selector
     * @param columnName The ColumnName
     * @return
     * @throws PlanningException
     */
    public static Object convertSelectorToObject(Selector selector, ColumnName columnName) throws PlanningException {
        Object result = null;
        ColumnMetadata columnMetadata = MetadataManager.MANAGER.getColumn(columnName);
        switch(columnMetadata.getColumnType()){
        case BIGINT:
            result = convertSelectorToLong(selector);
            break;
        case BOOLEAN:
            result = convertSelectorToBoolean(selector);
            break;
        case DOUBLE:
            result = convertSelectorToDouble(selector);
            break;
        case FLOAT:
            result = convertSelectorToFloat(selector);
            break;
        case INT:
            result = convertSelectorToInteger(selector);
            break;
        case VARCHAR:
        case TEXT:
            result = convertSelectorToString(selector);
            break;
        case LIST:
        case MAP:
        case NATIVE:
        case SET:
            throw new PlanningException("ColumnType: " + columnMetadata.getColumnType() + " not supported yet");
        }
        return result;
    }

    private static String convertSelectorToString(Selector selector) throws PlanningException {
        String result;
        try {
            StringSelector stringSelector = (StringSelector) selector;
            result = stringSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to String", cce);
        }
        return result;
    }

    private static Boolean convertSelectorToBoolean(Selector selector) throws PlanningException {
        boolean result;
        try {
            BooleanSelector booleanSelector = (BooleanSelector) selector;
            result = booleanSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to boolean", cce);
        }
        return result;
    }

    private static Float convertSelectorToFloat(Selector selector) throws PlanningException {
        float result;
        try {
            FloatingPointSelector floatingPointSelector = (FloatingPointSelector) selector;
            result = (float) floatingPointSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to float", cce);
        }
        return result;
    }

    private static Double convertSelectorToDouble(Selector selector) throws PlanningException {
        double result;
        try {
            FloatingPointSelector floatingPointSelector = (FloatingPointSelector) selector;
            result = floatingPointSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to double", cce);
        }
        return result;
    }

    private static Integer convertSelectorToInteger(Selector selector) throws PlanningException {
        int result;
        try {
            IntegerSelector integerSelector = (IntegerSelector) selector;
            result = (int) integerSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to int", cce);
        }
        return result;
    }

    private static Long convertSelectorToLong(Selector selector) throws PlanningException {
        long result;
        try {
            IntegerSelector integerSelector = (IntegerSelector) selector;
            result = integerSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to long", cce);
        }
        return result;
    }
}
