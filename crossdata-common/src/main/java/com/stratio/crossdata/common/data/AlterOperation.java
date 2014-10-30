package com.stratio.crossdata.common.data;


public enum AlterOperation {
    /**
     * Alter a new column data type using {@code ALTER}.
     */
    ALTER_COLUMN,

    /**
     * Add a new column using {@code ADD}.
     */
    ADD_COLUMN,

    /**
     * Drop a column using {@code DROP}.
     */
    DROP_COLUMN,

    /**
     * Establish a set of options using {@code WITH}.
     */
    ALTER_OPTIONS

}
