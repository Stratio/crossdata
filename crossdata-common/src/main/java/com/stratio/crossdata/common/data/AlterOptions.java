package com.stratio.crossdata.common.data;

import java.io.Serializable;
import java.util.Map;

import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.statements.structures.Selector;

public class AlterOptions implements Serializable {

    // Serial UID

    /**
     * Type of alter.
     */
    private AlterOperation option;

    ColumnMetadata columnMetadata;

    private Map<Selector, Selector> properties = null;

    public AlterOptions(AlterOperation option,
            Map<Selector, Selector> properties, ColumnMetadata columnMetadata) {
        this.option = option;
        this.properties = properties;
        this.columnMetadata = columnMetadata;
    }

    public AlterOperation getOption() {
        return option;
    }

    public void setOption(AlterOperation option) {
        this.option = option;
    }

    public ColumnMetadata getColumnMetadata() {
        return columnMetadata;
    }

    public void setColumnMetadata(ColumnMetadata columnMetadata) {
        this.columnMetadata = columnMetadata;
    }

    public Map<Selector, Selector> getProperties() {
        return properties;
    }

    public void setProperties(Map<Selector, Selector> properties) {
        this.properties = properties;
    }
}
