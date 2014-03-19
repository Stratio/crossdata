package com.stratio.meta.common.statements;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.core.structures.IndexType;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.common.utils.DeepResult;
import com.stratio.meta.common.utils.MetaStep;
import com.stratio.meta.common.utils.MetaUtils;
import com.stratio.meta.common.utils.ValidationException;
import com.stratio.meta.server.metadata.MetadataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Create index statement of the META language. This class recognizes the following syntax:
 * <p>
 * CREATE {@link IndexType} INDEX (IF NOT EXISTS)? {@literal <index_name>}
 * ON {@literal <tablename>} ( {@literal <identifier> , ..., <identifier>})
 * ( USING {@literal <index_class>} )? ( WITH OPTIONS ( key_1=value_1 AND ... AND key_n=value_n) )?;
 */
public class CreateIndexStatement extends MetaStatement {	
	
    private boolean _keyspaceInc = false;
    private String _keyspace = null;
    private IndexType _type = null;
    private boolean _createIfNotExists = false;
    private String _name = null;
    private String _tablename = null;
    private ArrayList<String> _targetColumn = null;
    private String _usingClass = null;
    private HashMap<String, ValueProperty> _options = null;

    public CreateIndexStatement(){
        _targetColumn = new ArrayList<>();
        _options = new HashMap<>();
    }

    public void setIndexType(String type){
        _type = IndexType.valueOf(type);
    }

    public void setCreateIfNotExists(){
        _createIfNotExists = true;
    }

    public boolean isKeyspaceInc() {
        return _keyspaceInc;
    }

    public void setKeyspaceInc(boolean _keyspaceInc) {
        this._keyspaceInc = _keyspaceInc;
    }

    public String getKeyspace() {
        return _keyspace;
    }

    public void setKeyspace(String _keyspace) {
        this._keyspace = _keyspace;
    }

    public IndexType getType() {
        return _type;
    }

    public void setType(IndexType _type) {
        this._type = _type;
    }

    public boolean isCreateIfNotExists() {
        return _createIfNotExists;
    }

    public void setCreateIfNotExists(boolean _createIfNotExists) {
        this._createIfNotExists = _createIfNotExists;
    }

    public ArrayList<String> getTargetColumn() {
        return _targetColumn;
    }

    public void setTargetColumn(ArrayList<String> _targetColumn) {
        this._targetColumn = _targetColumn;
    }	        
        
    public void setName(String name){
        if(name.contains(".")){
            String[] ksAndTablename = name.split("\\.");
            _keyspace = ksAndTablename[0];
            name = ksAndTablename[1];
            _keyspaceInc = true;
        }
        _name = name;
    }

    public String getName(){
            return _name;
    }

    public void setTablename(String tablename){
        _tablename = tablename;
    }

    public void addColumn(String column){
        _targetColumn.add(column);
    }

    public void setUsingClass(String using){
        _usingClass = using;
    }

    public void addOption(String key, ValueProperty value){
        _options.put(key, value);
    }

    public HashMap<String, ValueProperty> getOptions(){
        return _options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CREATE ");
        //if(_type == IndexType.HASH){
        //    sb.append("CUSTOM");
        //} else {
            sb.append(_type);
        //}
        sb.append(" INDEX ");
        if(_createIfNotExists){
                sb.append("IF NOT EXISTS ");
        }
        if(_keyspaceInc){
            sb.append(_keyspace).append(".");
        }
        if(_name != null){
            sb.append(_name).append(" ");
        }
        sb.append("ON ");
        sb.append(_tablename);
        sb.append(" (").append(MetaUtils.StringList(_targetColumn, ", ")).append(")");
        if(_usingClass != null){
                sb.append(" USING ");
                sb.append(_usingClass);
        }
        if(_options.size() > 0){
            sb.append(" WITH OPTIONS ");
            //sb.append(" WITH OPTIONS = {");
            Iterator<Entry<String, ValueProperty>> entryIt = _options.entrySet().iterator();
            Entry<String, ValueProperty> e;
            while(entryIt.hasNext()){
                    e = entryIt.next();
                    //sb.append(e.getKey()).append(" : ").append("'").append(e.getValue()).append("'");
                    sb.append(e.getKey()).append(" = ").append(e.getValue());
                    if(entryIt.hasNext()){
                            sb.append(" AND ");
                    }
            }
            //sb.append("}");
        }
        
        return sb.toString();
    }

    @Override
    public void validate() {
    }

    public void validate(MetadataManager mm, String keyspace) {

        //Get the target table metadata
        KeyspaceMetadata km = mm.getKeyspaceMetadata(keyspace);
        //Check that target columns appear on the table

        if(IndexType.LUCENE.equals(_type)){
            //Parse index options.
            //Check that the index mapping types are compatible with the specified C* types.

        }else if(IndexType.DEFAULT.equals(_type)){
            //Check that only one column is specified

        }
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        String cqlString = this.toString().replace(" DEFAULT ", " ");
        if(cqlString.contains(" LUCENE ")){
            throw new ValidationException("Lucene indexes are not supported yet");
        }        
        if(cqlString.contains("USING")){
            cqlString = cqlString.replace("USING ", "USING '");
            if(cqlString.contains("WITH ")){
                cqlString = cqlString.replace(" WITH ", "' WITH ");
            } else {
                cqlString = cqlString.replace(";", "';");
            }
        }
        if(cqlString.contains("OPTIONS")){
            cqlString = cqlString.replace("OPTIONS", "OPTIONS = {");
            cqlString = cqlString.replace(";", "};");
            String cqlOptions = cqlString.substring(cqlString.indexOf("{")+1, cqlString.lastIndexOf("}")+1);
            //System.out.println("cqlOptions: "+cqlOptions);
            cqlString = cqlString.substring(0, cqlString.indexOf("{")+1).concat(cqlString.substring(cqlString.lastIndexOf("}")));
            //System.out.println("cqlString: "+cqlString);            
            /*
            String[] opts = cqlOptions.split("=");
            cqlOptions = new String();
            for(int i=0; i<opts.length; i++){
                cqlOptions = cqlOptions.concat("\'").concat(opts[i]).concat("\'");
                if(i % 2 == 0){
                    cqlOptions = cqlOptions.concat(": ");
                } else {
                    if(i<(opts.length-1)){
                        cqlOptions = cqlOptions.concat(" AND ");
                    }
                }
            }
            cqlString = cqlString.replace("OPTIONS = {", "OPTIONS = {"+cqlOptions);
            */            
            cqlString = cqlString.replace("OPTIONS = {", "OPTIONS = {"+MetaUtils.addSingleQuotesToStringList(cqlOptions));
        }
        return cqlString;
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
