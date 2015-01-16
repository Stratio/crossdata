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

import java.util.Map;

import com.stratio.crossdata.common.data.AlterOperation;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

/**
 * Class that models an {@code ALTER TABLE} statement from the CROSSDATA language.
 */
public class AlterTableStatement extends MetadataStatement implements ITableStatement {

    private class InnerAlterStatement extends AbstractTableStatement{   }
    InnerAlterStatement tableStatement=new InnerAlterStatement();
    
    /**
     * Type of alter.
     */
    private AlterOperation option;

    /**
     * Target column name.
     */
    private ColumnName column;

    /**
     * Target column datatype used with {@code ALTER} or {@code ADD}.
     */
    private ColumnType type;

    /**
     * The list of {@link com.stratio.crossdata.core.structures.Property} of the table.
     */
    private Map<Selector, Selector> properties = null;

    /**
     * The alter columnMetadata.
     */
    private ColumnMetadata columnMetadata = null;

    /**
     * Class constructor.
     *
     * @param tableName  The name of the table.
     * @param column     The name of the column.
     * @param type       The data type of the column.
     * @param properties The type of modification.
     * @param option     The map of options.
     */
    public AlterTableStatement(TableName tableName, ColumnName column, ColumnType type,
            String properties, AlterOperation option) {
        this.command = false;
        this.tableStatement.setTableName(tableName);
        this.column = column;
        this.type = type;
        this.properties = StringUtils.convertJsonToOptions(tableName, properties);
        this.option = option;
        Object[] parameters = { };
        this.columnMetadata = new ColumnMetadata(column, parameters, type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ALTER TABLE ");
        sb.append(tableStatement.getTableName().getQualifiedName());
        switch (option) {
        case ALTER_COLUMN:
            sb.append(" ALTER ").append(column.getQualifiedName());
            sb.append(" TYPE ").append(type);
            break;
        case ADD_COLUMN:
            sb.append(" ADD ");
            sb.append(column.getQualifiedName()).append(" ");
            sb.append(type);
            break;
        case DROP_COLUMN:
            sb.append(" DROP ");
            sb.append(column.getQualifiedName());
            break;
        case ALTER_OPTIONS:
            sb.append(" WITH ").append(properties);
            break;
        default:
            sb.append("BAD OPTION");
            break;
        }
        if((option != AlterOperation.ALTER_OPTIONS) && (properties != null) && (!properties.isEmpty())){
            sb.append(" WITH ").append(properties);
        }
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        ValidationRequirements validationRequirements;
        switch (option) {
        case ALTER_COLUMN:
            validationRequirements = new ValidationRequirements().add(ValidationTypes.MUST_EXIST_TABLE)
                    .add(ValidationTypes.MUST_EXIST_COLUMN);
            break;
        case ADD_COLUMN:
            validationRequirements = new ValidationRequirements().add(ValidationTypes.MUST_EXIST_TABLE)
                    .add(ValidationTypes.MUST_NOT_EXIST_COLUMN);
            break;
        case DROP_COLUMN:
            validationRequirements = new ValidationRequirements().add(ValidationTypes.MUST_EXIST_TABLE)
                    .add(ValidationTypes.MUST_EXIST_COLUMN);
            break;
        case ALTER_OPTIONS:
            validationRequirements = new ValidationRequirements().add(ValidationTypes.MUST_EXIST_TABLE)
                    .add(ValidationTypes.MUST_EXIST_PROPERTIES);
            break;
        default:
            validationRequirements = new ValidationRequirements().add(ValidationTypes.MUST_EXIST_TABLE);
        }
        return validationRequirements;
    }

    public TableName getTableName() { return tableStatement.getTableName(); }

    public void setTableName(TableName tableName) { this.tableStatement.setTableName(tableName); }

    @Override
    public CatalogName getEffectiveCatalog() { return tableStatement.getEffectiveCatalog(); }

    public ColumnName getColumn() {
        return column;
    }

    public ColumnType getType() {
        return type;
    }

    public AlterOperation getOption() {
        return option;
    }

    public Map<Selector, Selector> getProperties() {
        return properties;
    }

    @Override
    public ColumnMetadata getColumnMetadata() {
        return columnMetadata;
    }
}
