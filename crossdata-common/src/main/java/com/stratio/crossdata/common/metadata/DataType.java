package com.stratio.crossdata.common.metadata;

import java.io.Serializable;

public enum DataType implements Serializable {
    BIGINT,
    BOOLEAN,
    DOUBLE,
    FLOAT,
    INT,
    TEXT,
    VARCHAR,
    NATIVE,
    SET,
    LIST,
    MAP;
}