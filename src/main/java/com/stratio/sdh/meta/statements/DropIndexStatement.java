package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public class DropIndexStatement extends Statement {

    private boolean _dropIfExists = false;
    private String _name = null;

    public void setDropIfExists(){
            _dropIfExists = true;
    }

    public void setName(String name){
            _name = name;
    }

    @Override
    public Path estimatePath() {
            return Path.CASSANDRA;
    }

    @Override
    public String toString() {
            StringBuilder sb = new StringBuilder("DROP INDEX ");
            if(_dropIfExists){
                    sb.append("IF EXISTS ");
            }
            sb.append(_name);
            return sb.toString();
    }

    @Override
    public boolean validate() {
        return true;
    }

}
