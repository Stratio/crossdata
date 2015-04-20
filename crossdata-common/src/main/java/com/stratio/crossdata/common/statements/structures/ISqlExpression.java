package com.stratio.crossdata.common.statements.structures;

public interface ISqlExpression {

    public String toSQLString(boolean withAlias);
}
