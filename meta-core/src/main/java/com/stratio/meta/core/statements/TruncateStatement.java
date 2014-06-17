/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.statements;

import static com.datastax.driver.core.querybuilder.QueryBuilder.truncate;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Truncate;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

public class TruncateStatement extends MetaStatement {

  private String ident;

  public TruncateStatement(String ident) {
    this.command = false;
    this.ident = ident;
    if (ident.contains(".")) {
      String[] ksAndTableName = ident.split("\\.");
      keyspace = ksAndTableName[0];
      this.ident = ksAndTableName[1];
      keyspaceInc = true;
    }
  }

  public boolean isKeyspaceInc() {
    return keyspaceInc;
  }

  public void setKeyspaceInc(boolean keyspaceInc) {
    this.keyspaceInc = keyspaceInc;
  }

  public String getKeyspace() {
    return keyspace;
  }

  public void setKeyspace(String keyspace) {
    this.keyspace = keyspace;
  }

  public String getIdent() {
    return ident;
  }

  public void setIdent(String ident) {
    if (ident.contains(".")) {
      String[] ksAndTablename = ident.split("\\.");
      keyspace = ksAndTablename[0];
      this.ident = ksAndTablename[1];
      keyspaceInc = true;
    } else {
      this.ident = ident;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TRUNCATE ");
    if (keyspaceInc) {
      sb.append(keyspace).append(".");
    }
    sb.append(ident);
    return sb.toString();
  }


  @Override
  public String translateToCQL() {
    return this.toString();
  }

  @Override
  public Statement getDriverStatement() {
    Truncate truncateQuery;
    if (keyspaceInc) {
      truncateQuery = truncate(keyspace, ident);
    } else {
      truncateQuery = truncate(ident);
    }
    return truncateQuery;
  }

    @Override
    public Result validate(MetadataManager metadata) {
      Result result = QueryResult.createSuccessQueryResult();

      String effectiveKeyspace = getEffectiveKeyspace();
      if(keyspaceInc){
        effectiveKeyspace = keyspace;
      }

      //Check that the keyspace and table exists.
      if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
        result= Result.createValidationErrorResult("Target keyspace missing or no keyspace has been selected.");
      }else{
        KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
        if(ksMetadata == null){
          result= Result.createValidationErrorResult("Keyspace " + effectiveKeyspace + " does not exist.");
        }else {
          TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, ident);
          if (tableMetadata == null) {
            result= Result.createValidationErrorResult("Table " + ident + " does not exist.");
          }
        }
      }
      return result;

    }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
