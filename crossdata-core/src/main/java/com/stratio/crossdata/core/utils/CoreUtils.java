package com.stratio.crossdata.core.utils;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.statements.structures.selectors.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.selectors.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.selectors.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.selectors.Selector;
import com.stratio.crossdata.common.statements.structures.selectors.StringSelector;
import com.stratio.crossdata.core.metadata.MetadataManager;

public final class CoreUtils {

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

    private static boolean convertSelectorToBoolean(Selector selector) throws PlanningException {
        boolean result;
        try {
            BooleanSelector booleanSelector = (BooleanSelector) selector;
            result = booleanSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to boolean", cce);
        }
        return result;
    }

    private static float convertSelectorToFloat(Selector selector) throws PlanningException {
        float result;
        try {
            FloatingPointSelector floatingPointSelector = (FloatingPointSelector) selector;
            result = (float) floatingPointSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to float", cce);
        }
        return result;
    }

    private static double convertSelectorToDouble(Selector selector) throws PlanningException {
        double result;
        try {
            FloatingPointSelector floatingPointSelector = (FloatingPointSelector) selector;
            result = floatingPointSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to double", cce);
        }
        return result;
    }

    private static int convertSelectorToInteger(Selector selector) throws PlanningException {
        int result;
        try {
            IntegerSelector integerSelector = (IntegerSelector) selector;
            result = (int) integerSelector.getValue();
        } catch (ClassCastException cce) {
            throw new PlanningException(selector + " cannot be converted to int", cce);
        }
        return result;
    }

    private static long convertSelectorToLong(Selector selector) throws PlanningException {
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
