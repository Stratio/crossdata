package com.stratio.tests.utils;


import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MetadataHelper {
    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(MetadataHelper.class.getName());
    /**
     * Mapping of native datatypes to SQL types.
     */
    private static Map<String, String> nativeODBCType = new HashMap<>();
    private static Map<ColumnType, String> dbType = new HashMap<>();
    private static Map<ColumnType, Class<?>> dbClass = new HashMap<>();

    static {

        dbClass.put(new ColumnType(DataType.BIGINT), Long.class);
        dbClass.put(new ColumnType(DataType.BOOLEAN), Boolean.class);
        dbClass.put(new ColumnType(DataType.DOUBLE), Double.class);
        dbClass.put(new ColumnType(DataType.FLOAT), Float.class);
        dbClass.put(new ColumnType(DataType.INT), Integer.class);
        dbClass.put(new ColumnType(DataType.TEXT), String.class);
        dbClass.put(new ColumnType(DataType.VARCHAR), String.class);

        dbType.put(new ColumnType(DataType.BIGINT), "BIGINT");
        dbType.put(new ColumnType(DataType.BOOLEAN), "BOOLEAN");
        dbType.put(new ColumnType(DataType.DOUBLE), "DOUBLE");
        dbType.put(new ColumnType(DataType.FLOAT), "FLOAT");
        dbType.put(new ColumnType(DataType.INT), "INT");
        dbType.put(new ColumnType(DataType.TEXT), "TEXT");
        dbType.put(new ColumnType(DataType.VARCHAR), "VARCHAR");

    }

    /**
     * Mapping between Cassandra datatypes and META datatypes.
     */
    private static Map<String, ColumnType> typeMapping = new HashMap<>();

    /**
     * Class constructor.
     */
    public MetadataHelper() {
        for (Map.Entry<ColumnType, String> entry : dbType.entrySet()) {
            typeMapping.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * Transform a Cassandra type to Crossdata type.
     *
     * @param dbTypeName The Cassandra column type.
     * @return The Crossdata ColumnType.
     */
    public ColumnType toColumnType(String dbTypeName) {
        ColumnType result = typeMapping.get(dbTypeName.toUpperCase());
        if (result == null) {
            try {
                String jdbcType = (dbTypeName.toUpperCase());
                result = new ColumnType(DataType.NATIVE);
                result.setDBMapping(jdbcType, getJavaClass(jdbcType));
                result.setODBCType(nativeODBCType.get(jdbcType));
            } catch (IllegalArgumentException iae) {
                LOG.error("Invalid database type: " + dbTypeName, iae);
                result = null;
            }
        } else {
            result.setDBMapping(dbType.get(result), dbClass.get(result));
        }

        return result;
    }

    private Class<?> getJavaClass(String type) {
        Object value;
        switch (type) {
            case "CHAR":
            case "VARCHAR":
                return String.class;
            case "NUMERIC":
            case "DECIMAL":
                return BigDecimal.class;
            case "BIT":
            case "BOOLEAN":
                return Boolean.class;
            case "TINYINT":
                return Byte.class;
            case "SMALLINT":
                return Short.class;
            case "INTEGER":
                return Integer.class;
            case "BIGINT":
                return Long.class;
            case "REAL":
                return Float.class;
            case "FLOAT":
            case "DOUBLE":
                return Double.class;
            case "BINARY":
            case "VARBINARY":
            case "LONGVARBINARY":
                return Byte[].class;
            case "DATE":
                return Date.class;
            case "TIME":
                return Time.class;
            case "TIMESTAMP":
                return Timestamp.class;
            case "CLOB":
                return Clob.class;
            case "BLOB":
                return Blob.class;
            case "ARRAY":
                return Array.class;
            case "DISTINCT":
            case "STRUCT":
            case "REF":
            case "DATALINK":
            case "JAVA_OBJECT":
            default:
                return Object.class;
        }
    }
}