package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Assignment;
import com.stratio.sdh.meta.structures.MetaRelation;
import com.stratio.sdh.meta.structures.Option;
import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.Term;
import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.List;
import java.util.Map;

public class UpdateTableStatement extends Statement {
    
    private String tablename;
    private boolean optsInc;
    private List<Option> options;
    private List<Assignment> assignments;
    private List<MetaRelation> whereclauses;
    private boolean condsInc;
    private Map<String, Term> conditions;

    public UpdateTableStatement(String tablename, 
                                boolean optsInc, 
                                List<Option> options, 
                                List<Assignment> assignments, 
                                List<MetaRelation> whereclauses, 
                                boolean condsInc, 
                                Map<String, Term> conditions) {
        this.tablename = tablename;
        this.optsInc = optsInc;
        this.options = options;
        this.assignments = assignments;
        this.whereclauses = whereclauses;
        this.condsInc = condsInc;
        this.conditions = conditions;
    }        
    
    public UpdateTableStatement(String tablename, 
                                List<Option> options, 
                                List<Assignment> assignments, 
                                List<MetaRelation> whereclauses, 
                                Map<String, Term> conditions) {
        this(tablename, true, options, assignments, whereclauses, true, conditions);
    }
    
    public UpdateTableStatement(String tablename, 
                                List<Assignment> assignments, 
                                List<MetaRelation> whereclauses, 
                                Map<String, Term> conditions) {
        this(tablename, false, null, assignments, whereclauses, true, conditions);
    }
    
    public UpdateTableStatement(String tablename, 
                                List<Option> options, 
                                List<Assignment> assignments, 
                                List<MetaRelation> whereclauses) {
        this(tablename, true, options, assignments, whereclauses, false, null);
    }
    
    public UpdateTableStatement(String tablename,                                 
                                List<Assignment> assignments, 
                                List<MetaRelation> whereclauses) {
        this(tablename, false, null, assignments, whereclauses, false, null);
    }
    
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public boolean isOptsInc() {
        return optsInc;
    }

    public void setOptsInc(boolean optsInc) {
        this.optsInc = optsInc;
    }   
    
    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }        

    public List<MetaRelation> getWhereclauses() {
        return whereclauses;
    }

    public void setWhereclauses(List<MetaRelation> whereclauses) {
        this.whereclauses = whereclauses;
    }

    public boolean isIncConds() {
        return condsInc;
    }

    public void setIncConds(boolean condsInc) {
        this.condsInc = condsInc;
    }

    public Map<String, Term> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Term> conditions) {
        this.conditions = conditions;
    }

    @Override
    public Path estimatePath() {        
        return Path.CASSANDRA;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tablename).append(" ");
        if(optsInc){
            sb.append("USING ");
            sb.append(MetaUtils.StringList(options, " AND "));
        }
        sb.append(" ").append("SET ");
        sb.append(MetaUtils.StringList(assignments, ", "));
        /*for(Assignment assign: assignments){
            sb.append(assign.toString());
        }*/        
        sb.append(" ").append("WHERE ");
        sb.append(MetaUtils.StringList(whereclauses, " AND "));
        /*for(MetaRelation relation: whereclauses){
            sb.append(relation.toString());
        }*/
        if(condsInc){
            sb.append(" ").append("IF ");
            sb.append(MetaUtils.StringMap(conditions, ": ", " AND "));
            /*for(String key: conditions.keySet()){
                sb.append(sb);
            }*/
        }
        return sb.toString();
    }    
    
}
