package com.stratio.meta.core.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;

import java.util.ArrayList;
import java.util.List;

import com.stratio.meta.core.structures.MetaRelation;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.sh.utils.ShUtils;
import java.util.Arrays;

/**
 * Delete a set of rows. This class recognizes the following syntax:
 * <p>
 * DELETE ( {@literal <column>}, ( ',' {@literal <column>} )*)? FROM {@literal <tablename>}
 * WHERE {@literal <where_clause>};
 */
public class DeleteStatement extends MetaStatement {
	
    private ArrayList<String> _targetColumn = null;
    private boolean keyspaceInc = false;
    private String keyspace;
    private String _tablename = null;
    private List<MetaRelation> _whereClauses;

    public DeleteStatement(){
            _targetColumn = new ArrayList<>();
            _whereClauses = new ArrayList<>();
    }

    public ArrayList<String> getTargetColumn() {
        return _targetColumn;
    }

    public void setTargetColumn(ArrayList<String> _targetColumn) {
        this._targetColumn = _targetColumn;
    }

    public boolean isKeyspaceInc() {
        return keyspaceInc;
    }

    public void setKeyspaceInc(boolean keyspaceInc) {
        this.keyspaceInc = keyspaceInc;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public List<MetaRelation> getWhereClauses() {
        return _whereClauses;
    }

    public void setWhereClauses(List<MetaRelation> _whereClauses) {
        this._whereClauses = _whereClauses;
    }        

    public void addColumn(String column){
            _targetColumn.add(column);
    }

    public void setTablename(String tablename){
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
            keyspaceInc = true;
        }
        _tablename = tablename;
    }

    public void addRelation(MetaRelation relation){
            _whereClauses.add(relation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DELETE ");
        if(_targetColumn.size() > 0){
        sb.append(ShUtils.StringList(_targetColumn, ", "));
        }
        sb.append(" FROM ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        } 
        sb.append(_tablename);
        if(_whereClauses.size() > 0){
        	sb.append(" WHERE ");
        	sb.append(ShUtils.StringList(_whereClauses, " AND "));
        }
        return sb.toString();
    }

    @Override
    public void validate() {
        
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
        ArrayList<MetaStep> steps = new ArrayList<>();
        return steps;
    }
    
}
