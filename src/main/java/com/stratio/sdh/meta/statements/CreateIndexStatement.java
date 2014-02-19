package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.IndexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.ValueProperty;
import com.stratio.sdh.meta.utils.MetaUtils;

/**
 * Create index statement of the META language. This class recognizes the following syntax:
 * <p>
 * CREATE {@link IndexType} INDEX (IF NOT EXISTS)? {@literal <index_name>}
 * ON {@literal <tablename>} ( {@literal <identifier> , ..., <identifier>})
 * ( USING {@literal <index_class>} )? ( WITH OPTIONS ( key_1=value_1 AND ... AND key_n=value_n) )?;
 */
public class CreateIndexStatement extends Statement {	
	
	private IndexType _type = null;
	private boolean _createIfNotExists = false;
	private String _name = null;
	private String _tablename = null;
	private ArrayList<String> _targetColumn = null;
	private String _usingClass = null;
	private HashMap<String, ValueProperty> _options = null;
	
	public CreateIndexStatement(){
		_targetColumn = new ArrayList<String>();
		_options = new HashMap<String, ValueProperty>();
	}
	
	public void setIndexType(String type){
		_type = IndexType.valueOf(type);
	}
	
	public void setCreateIfNotExists(){
		_createIfNotExists = true;
	}
	
	public void setName(String name){
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
	public Path estimatePath() {
		return Path.CASSANDRA;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("CREATE ");
		sb.append(_type);
		sb.append(" INDEX ");
		if(_createIfNotExists){
			sb.append("IF NOT EXISTS ");
		}
		sb.append(_name);
		sb.append(" ON ");
		sb.append(_tablename);
		sb.append(" (").append(MetaUtils.StringList(_targetColumn, ", ")).append(")");
		if(_usingClass != null){
			sb.append(" USING ");
			sb.append(_usingClass);
		}
		if(_options.size() > 0){
			sb.append(" WITH OPTIONS ");
			Iterator<Entry<String, ValueProperty>> entryIt = _options.entrySet().iterator();
			Entry<String, ValueProperty> e = null;
			while(entryIt.hasNext()){
				e = entryIt.next();
				sb.append(e.getKey() + " = " + e.getValue());
				if(entryIt.hasNext()){
					sb.append(" AND ");
				}
			}
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

}
