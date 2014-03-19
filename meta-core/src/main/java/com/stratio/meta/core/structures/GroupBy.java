package com.stratio.meta.core.structures;

import com.stratio.meta.sh.utils.ShUtils;

import java.util.List;

public class GroupBy {

    private List<String> colNames; 

    public GroupBy(List<String> colNames) {
        this.colNames = colNames;
    }    
    
    public List<String> getColNames() {
        return colNames;
    }

    public void setColNames(List<String> colNames) {
        this.colNames = colNames;
    }        
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(" GROUP BY ");  
        sb.append(ShUtils.StringList(colNames, ", "));
        return sb.toString();
    }
    
}
