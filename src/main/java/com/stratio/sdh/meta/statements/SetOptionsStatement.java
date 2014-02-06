package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Consistency;
import com.stratio.sdh.meta.structures.Path;
import java.util.ArrayList;
import java.util.List;

public class SetOptionsStatement extends Statement {

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
        StringBuilder sb = new StringBuilder("Setting options: ");
        if(optionsCheck.get(0)){
            sb.append("analytics=").append(analytics);            
            if(optionsCheck.get(1)){
                sb.append(", consistency=").append(consistency);
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
    
}
