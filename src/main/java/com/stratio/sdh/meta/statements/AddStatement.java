package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public class AddStatement extends Statement {

    private String _path = null;

    public AddStatement(String path){
            _path = path;
    }

    @Override
    public Path estimatePath() {
            return Path.CASSANDRA;
    }

    @Override
    public String toString() {
            return "ADD \"" + _path + "\"";
    }

    @Override
    public boolean validate() {
        return true;
    }

}
