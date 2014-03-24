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

package com.stratio.meta.core.utils;

import com.stratio.meta.core.statements.MetaStatement;

public class AntlrResult {

    private MetaStatement statement;
    private ErrorsHelper foundErrors;

    public AntlrResult(MetaStatement statement, ErrorsHelper foundErrors) {
        this.statement = statement;
        this.foundErrors = foundErrors;
    }

    public MetaStatement getStatement() {
        return statement;
    }

    public void setStatement(MetaStatement statement) {
        this.statement = statement;
    }

    public ErrorsHelper getFoundErrors() {
        return foundErrors;
    }

    public void setFoundErrors(ErrorsHelper foundErrors) {
        this.foundErrors = foundErrors;
    }        
    
    public String toString(String query){        
        return foundErrors.toString(query, statement);      
    }

    public boolean hasErrors() {
        return !foundErrors.isEmpty();
    }
    
}
