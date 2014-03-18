package com.stratio.meta.metadata;

import com.datastax.driver.core.ColumnMetadata;

public class CustomIndexMetadata {

    /**
     * Type of index associated with the column.
     */
    private final IndexType _type;

    /**
     * Index options.
     */
    private String _options = null;

    /**
     * Class constructor for default Cassandra indexes.
     * @param columnMetadata The column metadata associated with the index.
     * @param type Type of index.
     */
    public CustomIndexMetadata(ColumnMetadata columnMetadata, IndexType type){
        _type = type;
    }

    /**
     * Get the type of the index associated with the column.
     * @return The type of index.
     */
    public IndexType getIndexType(){
        IndexType result = null;
        return result;
    }

    /**
     * Set the custom index options.
     * @param options The options.
     */
    public void setIndexOptions(String options){
        _options = options;
    }

    /**
     * Get the options associated with the index.
     * @return The options or null if not set.
     */
    public String getIndexOptions(){
        return _options;
    }
}
