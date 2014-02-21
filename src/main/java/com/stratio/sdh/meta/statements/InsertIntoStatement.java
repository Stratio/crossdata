package com.stratio.sdh.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.sdh.meta.structures.Option;
import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.ValueCell;
import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.List;

public class InsertIntoStatement extends MetaStatement {

    public static final int TYPE_SELECT_CLAUSE = 1;
    public static final int TYPE_VALUES_CLAUSE = 2;
    
    private String tablename;    
    private List<String> ids;
    private SelectStatement selectStatement;
    private List<ValueCell> cellValues;
    private boolean ifNotExists;
    private boolean optsInc;
    private List<Option> options;
    private int typeValues;

    public InsertIntoStatement(String tablename, List<String> ids, 
                               SelectStatement selectStatement, 
                               List<ValueCell> cellValues, 
                               boolean ifNotExists,
                               boolean optsInc,
                               List<Option> options, 
                               int typeValues) {
        this.tablename = tablename;
        this.ids = ids;
        this.selectStatement = selectStatement;
        this.cellValues = cellValues;
        this.ifNotExists = ifNotExists;
        this.optsInc = optsInc;
        this.options = options;
        this.typeValues = typeValues;
    }   

    public InsertIntoStatement(String tablename, 
                               List<String> ids, 
                               SelectStatement selectStatement, 
                               boolean ifNotExists, 
                               List<Option> options) {
        this(tablename, ids, selectStatement, null, ifNotExists, true, options, 1);
    }        

    public InsertIntoStatement(String tablename, 
                               List<String> ids, 
                               List<ValueCell> cellValues, 
                               boolean ifNotExists, 
                               List<Option> options) {
        this(tablename, ids, null, cellValues, ifNotExists, true, options, 2);
    }        
    
    public InsertIntoStatement(String tablename, 
                               List<String> ids, 
                               SelectStatement selectStatement, 
                               boolean ifNotExists) {
        this(tablename, ids, selectStatement, null, ifNotExists, false, null, 1);
    }        

    public InsertIntoStatement(String tablename, 
                               List<String> ids, 
                               List<ValueCell> cellValues, 
                               boolean ifNotExists) {
        this(tablename, ids, null, cellValues, ifNotExists, false, null, 2);
    }
    
    public List<String> getIds() {
        return ids;
    }

    public String getId(int index) {
        return ids.get(index);
    }
    
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
    
    public void addId(String id) {
        ids.add(id);
    }
    
    public void removeId(String id){
        ids.remove(id);
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public SelectStatement getSelectStatement() {
        return selectStatement;
    }

    public void setSelectStatement(SelectStatement selectStatement) {
        this.selectStatement = selectStatement;
    }

    public List<ValueCell> getCellValues() {
        return cellValues;
    }

    public ValueCell getCellValue(int index) {
        return cellValues.get(index);
    }
    
    public void setCellValues(List<ValueCell> cellValues) {
        this.cellValues = cellValues;
    }

    public void addCellValue(ValueCell valueCell) {
        cellValues.add(valueCell);
    }
    
    public void removeCellValue(ValueCell valueCell) {
        cellValues.remove(valueCell);
    }
    
    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public List<Option> getOptions() {
        return options;
    }

    public Option getOption(int n){
        return options.get(n);
    }
    
    public void setOptions(List<Option> options) {
        this.options = options;
    }
    
    public void addOption(Option option) {
        options.add(option);
    }
    
    public void remove(Option option) {
        options.remove(option);
    }

    public int getTypeValues() {
        return typeValues;
    }

    public void setTypeValues(int typeValues) {
        this.typeValues = typeValues;
    }        
    
    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tablename).append(" (");
        sb.append(MetaUtils.StringList(ids, ", ")).append(") ");
        if(typeValues == TYPE_SELECT_CLAUSE){
           sb.append(selectStatement.toString());
        } else {
           sb.append("VALUES(");
           sb.append(MetaUtils.StringList(cellValues, ", "));
           sb.append(")");
        }        
        if(ifNotExists){
            sb.append(" IF NOT EXISTS");            
        }
        if(optsInc){
            sb.append(" USING ");
            sb.append(MetaUtils.StringList(options, " AND "));
        }
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
    
}
