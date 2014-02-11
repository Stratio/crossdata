package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Option;
import com.stratio.sdh.meta.structures.Path;
import java.util.List;

public class SelectStatement extends Statement {

    private SelectionClause selectionClause;    
    private String tablename;
    private WindowSelect window;
    private InnerJoin join;
    private boolean whereInc;
    private WhereClause where;
    private boolean orderInc;
    private OrderBy order;
    private GroupBy group;
    private boolean limitInc;
    private int limit;
    private boolean disableAnalytics;
    private boolean optsInc;
    private List<Option> options;
    

    public SelectStatement() {
    } 
    
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }          
    
    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }

    @Override
    public String toString() {
        return "SELECT ";
    }
    
}
