package com.stratio.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.utils.DeepResult;
import com.stratio.meta.utils.MetaStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropIndexStatement extends MetaStatement {

    private boolean _dropIfExists = false;
    private String _name = null;

    public void setDropIfExists(){
            _dropIfExists = true;
    }

    public void setName(String name){
            _name = name;
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

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }

    @Override
    public String parseResult(ResultSet resultSet) {
        return "\t"+resultSet.toString();
    }
    
    @Override
    public Statement getDriverStatement() {
        Statement statement = null;
        return statement;
    }
    
    @Override
    public DeepResult executeDeep() {
        return new DeepResult("", new ArrayList<>(Arrays.asList("Not supported yet")));
    }
    
    @Override
    public List<MetaStep> getPlan() {
        return null;
    }
    
}
