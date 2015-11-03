package com.stratio.crossdata.testsAT.specs.utils;

public class JDBCDriverUtil {
    
    private static JDBCDriverUtil instance = new JDBCDriverUtil();
    private final JDBCDriver cUtils = new JDBCDriver();

    private JDBCDriverUtil() {
    }

    public static JDBCDriverUtil getInstance() {
        return instance;
    }

    public JDBCDriver getDriver() {
        return cUtils;
    }
}
