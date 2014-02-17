package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public class RemoveUDFStatement extends Statement {

    private String _jarName = null;

    public RemoveUDFStatement(String jarName){
            _jarName = jarName;
    }

    @Override
    public Path estimatePath() {
            return Path.CASSANDRA;
    }

    @Override
    public String toString() {
            return "REMOVE UDF \"" + _jarName + "\"";
    }

    @Override
    public boolean validate() {
        return true;
    }

}
