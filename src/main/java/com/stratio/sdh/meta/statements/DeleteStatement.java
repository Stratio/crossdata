package com.stratio.sdh.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import java.util.ArrayList;
import java.util.List;

import com.stratio.sdh.meta.structures.MetaRelation;
import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.utils.MetaUtils;

/**
 * Delete a set of rows. This class recognizes the following syntax:
 * <p>
 * DELETE ( {@literal <column>}, ( ',' {@literal <column>} )*)? FROM {@literal <tablename>}
 * WHERE {@literal <where_clause>};
 */
public class DeleteStatement extends MetaStatement {
	
    private ArrayList<String> _targetColumn = null;
    private String _tablename = null;
    private List<MetaRelation> _whereClauses;

    public DeleteStatement(){
            _targetColumn = new ArrayList<>();
            _whereClauses = new ArrayList<>();
    }

    public void addColumn(String column){
            _targetColumn.add(column);
    }

    public void setTablename(String tablename){
            _tablename = tablename;
    }

    public void addRelation(MetaRelation relation){
            _whereClauses.add(relation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DELETE");
        if(_targetColumn.size() > 0){
        sb.append(" (").append(MetaUtils.StringList(_targetColumn, ", ")).append(")");
        }
		sb.append(" FROM ");
        sb.append(_tablename);
        if(_whereClauses.size() > 0){
        	sb.append(" WHERE ");
        	sb.append(MetaUtils.StringList(_whereClauses, " AND "));
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
