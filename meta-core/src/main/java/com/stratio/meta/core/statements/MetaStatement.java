/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.statements;

import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import java.util.List;

public abstract class MetaStatement {
    
    protected String query;
    protected MetaPath path;

    public MetaStatement() {
    }

    public MetaStatement(String query, MetaPath path) {
        this.query = query;
        this.path = path;
    }        
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }        

    public MetaPath getPath() {
        return path;
    }

    public void setPath(MetaPath path) {
        this.path = path;
    }        
    
    @Override
    public abstract String toString();

    /**
     * Validate the semantics of the current statement. This method checks the
     * existing metadata to determine that all referenced entities exists in the
     * {@code targetKeyspace} and the types are compatible with the assignations
     * or comparisons.
     * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
     *                 the required information.
     * @param targetKeyspace The target keyspace where the query will be executed.
     * @return A {@link com.stratio.meta.common.result.MetaResult} with the validation result.
     */
    public abstract MetaResult validate(MetadataManager metadata, String targetKeyspace);

    public abstract String getSuggestion();
    
    public abstract String translateToCQL();

    //public abstract String parseResult(ResultSet resultSet);

    public abstract Statement getDriverStatement();

    public abstract DeepResult executeDeep();
    
    public abstract List<MetaStep> getPlan();
    
}
