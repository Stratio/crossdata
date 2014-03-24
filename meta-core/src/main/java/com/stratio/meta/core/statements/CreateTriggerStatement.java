package com.stratio.meta.core.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateTriggerStatement extends MetaStatement {
    
    private String trigger_name;

    public CreateTriggerStatement(String trigger_name, String table_name, String class_name) {
        this.trigger_name = trigger_name;
        this.table_name = table_name;
        this.class_name = class_name;
    }

    public String getTrigger_name() {
        return trigger_name;
    }

    public void setTrigger_name(String trigger_name) {
        this.trigger_name = trigger_name;
    }

    private String table_name;

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    private String class_name;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Create trigger ");
        sb.append(trigger_name);
        sb.append(" on ");
        sb.append(table_name);
        sb.append(" using ");
        sb.append(class_name);
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public MetaResult validate(MetadataManager metadata, String targetKeyspace) {
        return null;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }
    
//    @Override
//    public String parseResult(ResultSet resultSet) {
//        return "\t"+resultSet.toString();
//    }
    
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
        ArrayList<MetaStep> steps = new ArrayList<>();
        return steps;
    }
    
}
