package com.stratio.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.structures.Consistency;
import com.stratio.meta.structures.Path;

import java.util.ArrayList;
import java.util.List;

public class SetOptionsStatement extends MetaStatement {

    private Consistency consistency;
    private boolean analytics;
    private List<Boolean> optionsCheck;

    public SetOptionsStatement(boolean analytics, Consistency consistency, List<Boolean> optionsCheck) {
        this.consistency = consistency;
        this.analytics = analytics;
        this.optionsCheck = new ArrayList<>();
        this.optionsCheck.addAll(optionsCheck);
    }

    public Consistency getConsistency() {
        return consistency;
    }

    public void setConsistency(Consistency consistency) {
        this.consistency = consistency;
    }

    public boolean isAnalytics() {
        return analytics;
    }

    public void setAnalytics(boolean analytics) {
        this.analytics = analytics;
    }        

    public List<Boolean> getOptionsCheck() {
        return optionsCheck;
    }

    public void setOptionsCheck(List<Boolean> optionsCheck) {
        this.optionsCheck = optionsCheck;
    }        
    
    @Override
    public String toString() {
        //System.out.println("optionsCheck="+optionsCheck.toString());
        StringBuilder sb = new StringBuilder("Set options ");
        if(optionsCheck.get(0)){
            sb.append("analytics=").append(analytics);            
            if(optionsCheck.get(1)){
                sb.append(" AND consistency=").append(consistency);
            }
        } else {
            if(optionsCheck.get(1)){
                sb.append("consistency=").append(consistency);
            }
        }        
        return sb.toString();
    }

    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
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
    
}
