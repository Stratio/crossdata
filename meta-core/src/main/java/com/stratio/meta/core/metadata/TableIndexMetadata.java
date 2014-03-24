package com.stratio.meta.core.metadata;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.core.structures.IndexType;
import org.apache.log4j.Logger;

import java.util.*;

public class TableIndexMetadata {

    /**
     * Target table.
     */
    private TableMetadata _tableMetadata = null;

    /**
     * Class logger.
     */
     private static final Logger _logger = Logger.getLogger(TableIndexMetadata.class.getName());

    /**
     * Default constructor that defines the index metadata of
     * {@code tableMetadata}.
     * @param tableMetadata The {@link com.datastax.driver.core.TableMetadata}
     *                      associated with the target table.
     */
    public TableIndexMetadata(TableMetadata tableMetadata){
        _tableMetadata = tableMetadata;
    }

    /**
     * Return the list of indexes available for each column in the
     * {@link #_tableMetadata} associated with the class.
     * @return The list of available indexes.
     */
    public Map<String, List<CustomIndexMetadata>> getColumnIndexes(){
        Map<String, List<CustomIndexMetadata>> result = new HashMap<>();
        for(ColumnMetadata column : _tableMetadata.getColumns()){
            if(column.getIndex() != null){
                if(_logger.isTraceEnabled()){
                	_logger.trace("Index found in column " + column.getName());
                }

                List<CustomIndexMetadata> indexes = result.get(column.getName());
                if(indexes == null){
                    indexes = new ArrayList<>();
                    result.put(column.getName(), indexes);
                }

                CustomIndexMetadata toAdd = null;
                if(!column.getIndex().isCustomIndex()){
                    indexes.add(new CustomIndexMetadata(column, IndexType.DEFAULT));
                }else if (column.getIndex().isCustomIndex()
                        && column.getIndex().getIndexClassName().compareTo("org.apache.cassandra.db.index.stratio.RowIndex") == 0){
                    result.putAll(processCustomIndex(column));
                }else{
                    _logger.error("Index " + column.getIndex().getName()
                            + " on " + column.getName()
                            + " with class " + column.getIndex().getIndexClassName() + " not supported.");
                }
            }
        }

        if(_logger.isDebugEnabled()){
        	_logger.debug("Table " + _tableMetadata.getName() + " has " + result.size() + " indexed columns.");
        }
        return result;
    }

    /**
     * Process the options of a custom index extracting all the columns mapped by the index.
     * @param column The target column.
     * @return The map of columns mapped by the index.
     */
    protected Map<String, List<CustomIndexMetadata>> processCustomIndex(ColumnMetadata column) {
        Map<String, List<CustomIndexMetadata>> result = new HashMap<String, List<CustomIndexMetadata>>();
        //TODO Check Miguel Angel's code to reuse the Cassandra object to send the query.

        return result;
    }


}
