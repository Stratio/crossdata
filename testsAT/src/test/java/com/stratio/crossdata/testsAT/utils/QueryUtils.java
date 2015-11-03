package com.stratio.crossdata.testsAT.specs.utils;

import java.util.HashMap;

public class QueryUtils {

    /*
     * CLUSTER QUERIES
     */

    public String addDastatore(String path) {
        return "ADD DATASTORE \"" + path + "\";";
    }

    public String dropDatastore(String datastore_name) {
        return "DROP DATASTORE " + datastore_name + ";";
    }

    public String attachClusterQuery(String exists, String cluster_name, String datastore_name, String JSON) {
        String result = "ATTACH CLUSTER ";
        if (exists.equals("true")) {
            result += "IF NOT EXISTS ";
        }
        result += cluster_name + " ON DATASTORE " + datastore_name + " WITH OPTIONS ";
        result += JSON + ";";
        return result;
    }

    public String detachClusterQuery(String exists, String cluster_name) {
        String result = "DETACH CLUSTER ";
        if (exists.equals("true")) {
            result += "IF EXISTS ";
        }
        result += cluster_name;
        return result + ";";
    }

    public String alterClusterQuery(String exists, String cluster_name, String datastore, String JSON) {
        String result = "ALTER CLUSTER ";
        if (exists == "true") {
            result += "IF EXISTS ";
        }
        result += cluster_name + " WITH OPTIONS " + JSON;
        return result + ";";
    }

    public String describeClusterQuery(String cluster_name) {
        String result = "DESCRIBE ";
        if (cluster_name.equals("")) {
            result += "CLUSTERS ";
        } else {
            result += "CLUSTER " + cluster_name;
        }
        return result + ";";
    }

    public String attachConnector(String connector_name, String cluster_name, String JSON) {
        String result = "ATTACH CONNECTOR ";
        if (JSON.equals("NULL")) {
            result += connector_name + " TO " + cluster_name + ";";
        } else {
            result += connector_name + " TO " + cluster_name + " WITH OPTIONS " + JSON + ";";
        }

        return result;
    }

    public String detachConnector(String connector_name, String cluster_name) {
        return "DETACH CONNECTOR " + connector_name + " FROM " + cluster_name + ";";
    }

    /*
     * CATALOG QUERIES
     */

    public String createCatalogQuery(String ifNotExists, String catalogName, String JSON) {
        String result = "CREATE CATALOG ";
        if (ifNotExists.equals("true")) {
            result += "IF NOT EXISTS ";
        }
        if(JSON.equals("NULL")){
            return result + catalogName + ";";
        }
        result += catalogName + " WITH " + JSON;
        return result + ";";
    }

    public String dropCatalogQuery(String ifExists, String catalogName) {
        String result = "DROP CATALOG ";
        if (ifExists.equals("true")) {
            result += "IF EXISTS ";
        }
        result += catalogName;
        return result + ";";
    }

    public String alterCatalogQuery(String ifExists, String catalogName, String JSON) {
        String result = "ALTER CATALOG ";
        if (ifExists.equals("true")) {
            result += "IF EXISTS ";
        }
        result += catalogName + " WITH " + JSON;
        return result + ";";
    }

    public String describeCatalogQuery(String catalog_name) {
        String result = "DESCRIBE ";
        if (catalog_name.equals("")) {
            result += "CATALOGS";
        } else {
            result += "CATALOG " + catalog_name;
        }
        return result + ";";
    }

    public String useCatalogQuery(String catalog_name) {
        return "USE " + catalog_name + ";";
    }

    /*
     * TABLE QUERIES
     */

    public String createTableQuery(String ifNotExists, String tablename, String cluster, String[] columns,
            String[] options) {
        String result = "CREATE TABLE ";
        if (ifNotExists.equals("true")) {
            result += "IF NOT EXISTS ";
        }
        result += tablename + " ON CLUSTER " + cluster + " (";
        for (int i = 0; i < columns.length - 1; i++) {
            result += columns[i] + " , ";
        }
        result += columns[columns.length - 1] + ")";
        if (options.length > 0) {
            result += " WITH ";
            for (int i = 0; i < options.length - 1; i++) {
                result += options[i] + " AND ";
            }
            result += options[options.length - 1];
        }
        return result + ";";
    }

    public String alterTableQuery(String ifNotExists, String tablename, String instruction) {
        String result = "ALTER TABLE ";
        if (ifNotExists.equals("true")) {
            result += "IF EXISTS ";
        }
        result += tablename + " " + instruction;
        return result + ";";
    }

    public String dropTableQuery(String ifNotExists, String tablename) {
        String result = "DROP TABLE ";
        if (ifNotExists.equals("true")) {
            result += "IF EXISTS ";
        }
        result += tablename;
        return result + ";";
    }

    public String describeTableQuery(String tablename) {
        String result = "DESCRIBE ";
        if (tablename.equals("")) {
            result += "TABLES";
        } else {
            result += "TABLE " + tablename;
        }
        return result + ";";
    }

    /*
     * DATA OPERATIONS OVER TABLES
     */

    public String insertInto(String tableName, String[] columns, String[] values, String IfNotExists, String[] options) {
        String result = "INSERT INTO " + tableName + " ( ";
        for (int i = 0; i < columns.length - 1; i++) {
            result += columns[i] + " , ";
        }
        result += columns[columns.length - 1] + ") VALUES ( ";
        for (int i = 0; i < values.length - 1; i++) {
            result += values[i] + " , ";
        }
        result += values[columns.length - 1] + " ) ";
        if (IfNotExists.equals("true")) {
            result += "IF NOT EXISTS ";
        }
        if (!options[0].equals("")) {
            result += "USING ";
            for (int i = 0; i < options.length - 1; i++) {
                result += options[i] + " AND ";
            }
            result += options[options.length - 1];
        }
        return result + ";";
    }

    public String insertIntoSelectClause(String tableName, String[] columns, String select_clause, String IfNotExists,
            String[] options) {
        String result = "INSERT INTO " + tableName + " ( ";
        for (int i = 0; i < columns.length - 1; i++) {
            result += columns[i] + " , ";
        }
        result += columns[columns.length - 1] + ") " + select_clause + " ";
        if (IfNotExists.equals("true")) {
            result += "IF NOT EXISTS ";
        }
        if (!options[0].equals("")) {
            result += "USING ";
            for (int i = 0; i < options.length - 1; i++) {
                result += options[i] + " AND ";
            }
            result += options[options.length - 1];
        }
        return result + ";";
    }

    public String truncateTableClause(String ifExists, String tableName) {
        String result = "TRUNCATE ";
        if (ifExists.equals("true")) {
            result += "IF EXISTS ";
        }
        result += tableName;
        return result + ";";
    }

    /*
     * AUXILIARY FUNCTIONS
     */

    private HashMap<String, String> connectorsToHashMap(String connectors) {
        HashMap<String, String> result = new HashMap<String, String>();
        String[] connectorArray = connectors.split(",");
        if (connectorArray.length > 1) {
            for (int i = 0; i < connectorArray.length; i++) {
                result.put(connectorArray[i], connectorArray[i + 1]);
                i++;
            }
        }
        return result;
    }

}
