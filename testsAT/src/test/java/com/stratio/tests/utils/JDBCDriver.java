package com.stratio.tests.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.tests.utils.MetadataHelper;

public class JDBCDriver {
    private static final String DEFAULT_DRIVER_CLASS = "com.stratio.jdbc.core.jdbc4.StratioDriver";
    private static final String DEFAULT_URL = "jdbc:simba://PWD=pwd;UID=uid;";
    private static final String DRIVER_CLASS_ARG_KEY = "DriverClass";
    private static final String URL_ARG_KEY = "URL";

    private static final Logger LOG = Logger.getLogger(JDBCDriver.class);
    static Connection connectionJDBC;
    static com.stratio.crossdata.common.data.ResultSet resultSet;

    public Connection getConnectionJDBC() {
        return connectionJDBC;
    }

    public com.stratio.crossdata.common.data.ResultSet getResult() {
        return resultSet;
    }

    public void connect() throws ClassNotFoundException, SQLException {

        TreeMap<String, String> argMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        argMap.put(DRIVER_CLASS_ARG_KEY, DEFAULT_DRIVER_CLASS);
        argMap.put(URL_ARG_KEY, DEFAULT_URL);
        Class.forName(argMap.get(DRIVER_CLASS_ARG_KEY));
        Properties p = new Properties();
        for (Map.Entry<String, String> e : argMap.entrySet()) {
            p.setProperty(e.getKey(), e.getValue());

        }
        connectionJDBC = DriverManager.getConnection(argMap.get(URL_ARG_KEY), p);
    }

    public void dissconect(Connection connection) throws SQLException {
        connection.close();
    }

    public void executeQueryJDBC(Connection conn, String sql) throws SQLException {
        System.out.println("Executing SQL query: " + sql);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            boolean isResultSet = stmt.execute(sql);
            boolean hasMoreResult = true;
            while (hasMoreResult) {
                if (isResultSet) {
                    ResultSet rs = null;
                    try {
                        rs = stmt.getResultSet();
                        resultSet = transformToMetaResultSet(rs);
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                    }
                } else {
                    int rowCount = stmt.getUpdateCount();
                    if (rowCount == -1) {
                        hasMoreResult = false;
                    } else {
                        System.out.println(String.format("Query OK, %d row affected.", rowCount));
                    }
                }
                isResultSet = stmt.getMoreResults();
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private com.stratio.crossdata.common.data.ResultSet transformToMetaResultSet(java.sql.ResultSet resultSet)
            throws SQLException {
        com.stratio.crossdata.common.data.ResultSet crossdataResult = new com.stratio.crossdata.common.data.ResultSet();
        MetadataHelper helper = new MetadataHelper();

        // Get the columns in order
        ResultSetMetaData resultSetMetaData = null;
        int numColumns = 0;
        try {
            resultSetMetaData = resultSet.getMetaData();
            numColumns = resultSetMetaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<ColumnMetadata> columnList = new ArrayList<>();
        ColumnMetadata columnMetadata = null;
        com.stratio.crossdata.common.data.Row crossdataRow = null;
        for (int i = 0; i < numColumns; i++) {
            try {
                ColumnName columnName = new ColumnName(resultSetMetaData.getCatalogName(i),
                        resultSetMetaData.getTableName(i), resultSetMetaData.getColumnName(i));
                ColumnType type = helper.toColumnType(resultSetMetaData.getColumnTypeName(i));
                resultSetMetaData.getColumnType(i);
                columnMetadata = new ColumnMetadata(columnName, null, type);
                columnList.add(columnMetadata);

                crossdataRow = new com.stratio.crossdata.common.data.Row();
                Cell crossdataCell = getCell(resultSetMetaData.getColumnTypeName(i), resultSet,
                        resultSetMetaData.getColumnName(i));
                crossdataRow.addCell(resultSetMetaData.getColumnName(i), crossdataCell);
                crossdataResult.add(crossdataRow);
            } catch (SQLException e) {
                LOG.error("Cannot transform result set", e);
                crossdataResult = new com.stratio.crossdata.common.data.ResultSet();
            }
        }

        crossdataResult.setColumnMetadata(columnList);

        return crossdataResult;
    }

    /**
     * Get a {@link com.stratio.crossdata.common.data.Cell} with the column contents of a Row.
     * 
     * @param type
     *            The type of the column.
     * @param resultSet
     *            The row that contains the column.
     * @param columnName
     *            The column name.
     * @return A {@link com.stratio.crossdata.common.data.Cell} with the contents.
     * @throws java.lang.reflect.InvocationTargetException
     *             If the required method cannot be invoked.
     * @throws IllegalAccessException
     *             If the method cannot be accessed.
     */
    protected Cell getCell(String type, java.sql.ResultSet resultSet, String columnName) throws SQLException {
        Object value;
        switch (type) {
        case "CHAR":
        case "VARCHAR":
        case "LONGVARCHAR":
            value = resultSet.getString(columnName);
            break;
        case "NUMERIC":
        case "DECIMAL":
            value = resultSet.getDouble(columnName);
            break;
        case "BIT":
        case "BOOLEAN":
            value = resultSet.getBoolean(columnName);
            break;
        case "TINYINT":
            value = resultSet.getByte(columnName);
            break;
        case "SMALLINT":
            value = resultSet.getShort(columnName);
            break;
        case "INTEGER":
            value = resultSet.getInt(columnName);
            break;
        case "NUMBER":
            value = resultSet.getLong(columnName);
            break;
        case "BIGINT":
            value = resultSet.getLong(columnName);
            break;
        case "REAL":
            value = resultSet.getFloat(columnName);
            break;
        case "FLOAT":
        case "DOUBLE":
            value = resultSet.getDouble(columnName);
            break;
        case "BINARY":
        case "VARBINARY":
        case "LONGVARBINARY":
            value = resultSet.getBytes(columnName);
            break;
        case "DATE":
            value = resultSet.getDate(columnName);
            break;
        case "TIME":
            value = resultSet.getTime(columnName);
            break;
        case "TIMESTAMP":
            value = resultSet.getTimestamp(columnName);
            break;
        case "CLOB":
            value = resultSet.getClob(columnName);
            break;
        case "BLOB":
            value = resultSet.getBlob(columnName);
            break;
        case "ARRAY":
            value = resultSet.getArray(columnName);
            break;
        case "DISTINCT":
        case "STRUCT":
        case "REF":
        case "DATALINK":
        case "JAVA_OBJECT":
        default:
            value = null;
        }
        return new Cell(value);
    }

    public List<String> getTables(String catalogName) {
        List<String> tableNames = new ArrayList<String>();
        try {
            DatabaseMetaData meta = connectionJDBC.getMetaData();
            java.sql.ResultSet res = meta.getTables(catalogName, null, null, null);
            for (int i = 0; res.next(); i++) {
                tableNames.add(res.getString(i));
            }
            return tableNames;
        } catch (SQLException e) {
            LOG.error(e.toString());
        }
        return null;
    }

    public boolean existsTable(String catalogName, String TableName) {
        if (getTables(catalogName).contains(TableName)) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> getColumns(String catalogName, String tableName) {
        List<String> colNames = new ArrayList<String>();
        try {
            DatabaseMetaData meta = connectionJDBC.getMetaData();
            java.sql.ResultSet res = meta.getColumns(catalogName, null, tableName, null);
            for (int i = 0; res.next(); i++) {
                colNames.add(res.getString(i));
            }
            return colNames;
        } catch (SQLException e) {
            LOG.error(e.toString());
        }
        return null;
    }

    public boolean existsColumn(String catalogName, String TableName, String ColumnName) {
        if (getColumns(catalogName, TableName).contains(ColumnName)) {
            return true;
        } else {
            return false;
        }
    }

    public int getRows(String catalogName, String tableName) {
        try {
           
            Statement stmt = connectionJDBC.createStatement();
            ResultSet res = stmt.executeQuery("SELECT COUNT(*) FROM "+tableName);
            return res.getInt(1);
        } catch (SQLException e) {
            LOG.error(e.toString());
        }
        return -1;
    }
}
