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

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.Tree;

public class RemoveUDFStatement extends MetaStatement {

    /**
     * The target jar name.
     */
    private String jarName = null;

    /**
     * Class constructor.
     * @param jarName The name of the target jar.
     */
    public RemoveUDFStatement(String jarName){
        this.command = true;
        this.jarName = jarName;
    }

    @Override
    public String toString() {
            return "REMOVE UDF \"" + jarName + "\"";
    }

    @Override
    public String translateToCQL(MetadataManager metadataManager) {
        return this.toString();
    }

    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        return new Tree();
    }
    
}
