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

package com.stratio.meta.core.executor;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.structures.DescribeType;

public class CommandExecutor {

    /**
     * Private class constructor as all methods are static.
     */
    private CommandExecutor(){

    }

    public static Result execute(MetaStatement stmt, Session session) {
        try {
            if(stmt instanceof DescribeStatement){
                return executeDescribe((DescribeStatement) stmt, session);
            } else {
                return CommandResult.createFailCommanResult("Not supported yet.");
            }
        } catch (RuntimeException rex){
            return CommandResult.createFailCommanResult(rex.getMessage());
        }
    }

    private static Result executeDescribe(DescribeStatement dscrStatement, Session session){
        MetadataManager mm = new MetadataManager(session);
        mm.loadMetadata();
        Result result = null;
        String info = null;
        String errorMessage = null;
        if(dscrStatement.getType() == DescribeType.KEYSPACE){ // KEYSPACE
            KeyspaceMetadata ksInfo = mm.getKeyspaceMetadata(dscrStatement.getKeyspace());
            if(ksInfo == null){
                errorMessage = "KEYSPACE " + dscrStatement.getKeyspace() + " was not found";
            } else {
                info = ksInfo.exportAsString();
            }
        } else { // TABLE
            TableMetadata tableInfo = mm.getTableMetadata(dscrStatement.getKeyspace(), dscrStatement.getTableName());
            if(tableInfo == null){
                errorMessage = "TABLE " + dscrStatement.getTableName() + " was not found";
            } else {
                info = tableInfo.exportAsString();
            }
        }
        if(info!=null){
            result = CommandResult.createSuccessCommandResult(info);
        }else{
            result = CommandResult.createFailCommanResult(errorMessage);
        }
        return result;
    }

}
