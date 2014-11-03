/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.core.statements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.IndexType;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;

/**
 * Class that models a {@code CREATE INDEX} statement of the CROSSDATA language. This class recognizes
 * the following syntax:
 * <p/>
 * CREATE {@link IndexType} INDEX (IF NOT EXISTS)? {@literal <index_name>} <br>
 * ON {@literal <tableName>} ( {@literal <identifier> , ..., <identifier>}) <br>
 * ( USING {@literal <index_class>} )? ( WITH OPTIONS ( key_1=value_1 AND ... AND key_n=value_n) )?;
 */
public class CreateIndexStatement extends IndexStatement {

    /**
     * The {@link IndexType} to be created.
     */
    private IndexType type = null;

    /**
     * The {@link com.stratio.crossdata.common.data.IndexName} to be created.
     */
    private IndexName name = null;

    /**
     * Whether the index should be created only if not exists.
     */
    private boolean createIfNotExists = false;

    /**
     * The name of the target table.
     */
    private TableName tableName = null;

    /**
     * The list of columns covered by the index. Only one column is allowed for {@code DEFAULT}
     * indexes.
     */
    private List<ColumnName> targetColumns = null;

    /**
     * The name of the class that implements the secondary index.
     */
    private String usingClass = null;

    /**
     * The map of options passed to the index during its creation.
     */
    private Map<Selector, Selector> options = null;

    /**
     * Class constructor.
     */
    public CreateIndexStatement() {
        this.command = false;
        targetColumns = new ArrayList<>();
        options = new LinkedHashMap<>();
    }

    /**
     * Set the type of index.
     *
     * @param type The type from {@link IndexType}.
     */
    public void setIndexType(String type) {
        this.type = IndexType.valueOf(type.toUpperCase());
    }

    /**
     * Set that the index should be created if not exists.
     */
    public void setCreateIfNotExists() {
        createIfNotExists = true;
    }


    /**
     * Set the name of the index from a column name as both have the same attributes.
     *
     * @param columnName The column name.
     */
    public void setName(ColumnName columnName) {
        tableName=columnName.getTableName();
        if (tableName!=null) {
            catalog = columnName.getTableName().getCatalogName();
        }
        this.name = new IndexName(columnName.getTableName(), columnName.getName());
    }

    /**
     * Get the table name.
     *
     * @return The TableName.
     */
    public TableName getTableName() {
        return tableName;
    }

    /**
     * Set the name of the target table.
     *
     * @param tableName The name.
     */
    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    /**
     * Get the index type name.
     *
     * @return The IndexType.
     */
    public IndexType getType() {
        return type;
    }

    /**
     * Set the type of index.
     *
     * @param type A {@link IndexType}.
     */
    public void setType(IndexType type) {
        this.type = type;
    }

    /**
     * Get the targeted columns in a List<ColumnName>.
     *
     * @return List<ColumnName> with the columns targeted by the index.
     */
    public List<ColumnName> getTargetColumns() {
        return targetColumns;
    }

    /**
     * Add a column to the list of indexed columns.
     *
     * @param column The name of the column.
     */
    public void addColumn(ColumnName column) {
        tableName=column.getTableName();
        catalog=column.getTableName().getCatalogName();
        targetColumns.add(column);
    }

    /**
     * Set a USING class that implements the custom index.
     *
     * @param using The qualified name of the class.
     */
    public void setUsingClass(String using) {
        usingClass = using;
    }

    public void setOptionsJson(String optionsJson) {
        options = StringUtils.convertJsonToOptions(optionsJson);
    }

    /**
     * Get the map of options.
     *
     * @return The map of options.
     */
    public Map<Selector, Selector> getOptions() {
        return options;
    }


    public boolean isCreateIfNotExists() {
        return createIfNotExists;
    }

    /**
     * Get the name of the index. If a LUCENE index is to be created, the name of the index is
     * prepended with {@code stratio_lucene_}. If a name for the index is not specified, the index
     * will be named using the concatenation of the target column names.
     *
     * @return The name of the index.
     */
    protected String getIndexName() {
        String result;
        if (name == null) {
            StringBuilder sb = new StringBuilder();
            if (IndexType.FULL_TEXT.equals(type)) {
                sb.append("stratio_lucene_");
                sb.append(tableName);
            } else {
                sb.append(tableName);
                for (ColumnName c : targetColumns) {
                    sb.append("_");
                    sb.append(c.getQualifiedName());
                }
                sb.append("_idx");
            }
            result = sb.toString();
        } else {
            result = name.getName();
            if (IndexType.FULL_TEXT.equals(type)) {
                result = name.toString().replaceAll("\\[(\\w*)\\]", "[stratio_lucene_$1]");
            }
        }
        return result;
    }

    public void normalizeIndexName(){
        this.name = new IndexName(tableName, this.name.getName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CREATE ");
        sb.append(type);
        sb.append(" INDEX ");
        if (createIfNotExists) {
            sb.append("IF NOT EXISTS ");
        }

        if (name != null) {
            sb.append(getIndexName()).append(" ");
        }
        sb.append("ON ");
        if (catalogInc) {
            sb.append(catalog).append(".");
        }
        sb.append(tableName);
        sb.append("(").append(StringUtils.stringList(targetColumns, ", ")).append(")");
        if (usingClass != null) {
            sb.append(" USING ");
            sb.append("'").append(usingClass).append("'");
        }
        if (!options.isEmpty()) {
            sb.append(" WITH ");
            sb.append(options);
        }

        return sb.toString();
    }

    public IndexName getName() {
        return new IndexName(catalog.getName(),tableName.getName(),getIndexName());
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(ValidationTypes.MUST_NOT_EXIST_INDEX).add(ValidationTypes.MUST_EXIST_TABLE)
                .add(ValidationTypes.MUST_EXIST_COLUMN);
    }
}
