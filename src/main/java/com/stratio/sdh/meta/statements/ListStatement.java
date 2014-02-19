package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.ListType;
import com.stratio.sdh.meta.structures.Path;

public class ListStatement extends Statement {

    private ListType _type = null;

    public ListStatement(String type){
            _type = ListType.valueOf(type);
    }

    @Override
    public Path estimatePath() {
            return Path.CASSANDRA;
    }

    @Override
    public String toString() {
            return "LIST " + _type;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

}
