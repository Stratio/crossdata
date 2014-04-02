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

import com.datastax.driver.core.*;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.UseStatement;
import com.stratio.meta.core.utils.AntlrError;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.QueryStatus;
import org.apache.log4j.Logger;

public class Executor {

    private final Logger logger = Logger.getLogger(Executor.class);
    private final Session session;

    public Executor(String [] hosts, int port){
        Cluster cluster = Cluster.builder().addContactPoints(hosts)
                .withPort(port).build();
        this.session=cluster.connect();
    }
    
    public Executor(Session session) {
        this.session = session;
    }
    
    public MetaQuery executeQuery(MetaQuery metaQuery) {
        
        metaQuery.setStatus(QueryStatus.EXECUTED);
        MetaStatement stmt = metaQuery.getStatement();
        
        if(stmt.isCommand()){
            if(stmt instanceof DescribeStatement){
                DescribeStatement descrStmt =  (DescribeStatement) stmt;
                metaQuery.setResult(new CommandResult(System.getProperty("line.separator")+descrStmt.execute(session)));
            } else {
                metaQuery.setErrorMessage("Not supported yet.");
                return metaQuery;
            }
            return metaQuery;
        }
        
        StringBuilder sb = new StringBuilder();
        if(!stmt.getPlan().isEmpty()){
            sb.append("PLAN: ").append(System.getProperty("line.separator"));
            sb.append(stmt.getPlan().toStringDownTop());
            logger.info(sb.toString());
            metaQuery.setErrorMessage("Deep execution is not supported yet");
            return metaQuery;
        }       
                
        QueryResult queryResult = new QueryResult();
        Statement driverStmt = null;
        
        ResultSet resultSet;
        try{
            driverStmt = stmt.getDriverStatement();
            if(driverStmt != null){
                resultSet = session.execute(driverStmt);
            } else {
                resultSet = session.execute(stmt.translateToCQL());
            }

            queryResult.setResultSet(transformToMetaResultSet(resultSet));
            
            if(stmt instanceof UseStatement){
                UseStatement useStatement = (UseStatement) stmt;
                queryResult.setCurrentKeyspace(useStatement.getKeyspaceName());
            }

        } catch (Exception ex) {
            metaQuery.hasError();
            queryResult.setErrorMessage("Cassandra exception: "+ex.getMessage());
            if (ex instanceof UnsupportedOperationException){
                queryResult.setErrorMessage("Unsupported operation by C*: "+ex.getMessage());
            }
            if(ex.getMessage().contains("line") && ex.getMessage().contains(":")){
                String queryStr;
                if(driverStmt != null){
                    queryStr = driverStmt.toString();
                } else {
                    queryStr = stmt.translateToCQL();
                }
                String[] cMessageEx =  ex.getMessage().split(" ");
                sb = new StringBuilder();
                sb.append(cMessageEx[2]);
                for(int i=3; i<cMessageEx.length; i++){
                    sb.append(" ").append(cMessageEx[i]);
                }
                AntlrError ae = new AntlrError(cMessageEx[0]+" "+cMessageEx[1], sb.toString());
                queryStr = ParserUtils.getQueryWithSign(queryStr, ae);
                queryResult.setErrorMessage(ex.getMessage()+System.getProperty("line.separator")+"\t"+queryStr);
                logger.error(queryStr);
            }
        }
        /*
        if(!queryResult.hasError()){            
            logger.info("\033[32mResult:\033[0m "+stmt.parseResult(resultSet)+System.getProperty("line.separator"));
            //logger.info("\033[32mResult:\033[0m Cannot execute command"+System.getProperty("line.separator"));        
        } else {
            List<MetaStep> steps = stmt.getPlan();
            for(MetaStep step: steps){
                logger.info(step.getPath()+"-->"+step.getQuery());
            }
            DeepResult deepResult = stmt.executeDeep();
            if(deepResult.hasErrors()){
                logger.error("\033[31mUnsupported operation by Deep:\033[0m "+deepResult.getErrors()+System.getProperty("line.separator"));
            } else {
                logger.info("\033[32mResult:\033[0m "+deepResult.getResult()+System.getProperty("line.separator"));
            }
        }*/
        metaQuery.setResult(queryResult);
        return metaQuery;
    }

    private com.stratio.meta.common.data.ResultSet transformToMetaResultSet(ResultSet resultSet) {
        com.stratio.meta.common.data.CassandraResultSet crs = new com.stratio.meta.common.data.CassandraResultSet();
        for(Row row: resultSet.all()){
            com.stratio.meta.common.data.Row metaRow = new com.stratio.meta.common.data.Row();
            for (ColumnDefinitions.Definition def: row.getColumnDefinitions().asList()){
                Cell metaCell = null;
                if((def.getType() == DataType.ascii())
                    || (def.getType() == DataType.text())
                    || (def.getType() == DataType.varchar())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getString(def.getName()));
                } else if ((def.getType() == DataType.bigint())
                        || (def.getType() == DataType.counter())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getLong(def.getName()));
                } else if ((def.getType() == DataType.cboolean())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getBool(def.getName()));
                } else if ((def.getType() == DataType.blob())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getBytes(def.getName()));
                } else if ((def.getType() == DataType.decimal())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getDecimal(def.getName()));
                } else if ((def.getType() == DataType.cdouble())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getDouble(def.getName()));
                } else if ((def.getType() == DataType.cfloat())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getFloat(def.getName()));
                } else if ((def.getType() == DataType.inet())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getInet(def.getName()));
                } else if ((def.getType() == DataType.cint())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getInt(def.getName()));
                } else if ((def.getType() == DataType.timestamp())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getDate(def.getName()));
                } else if ((def.getType() == DataType.uuid())
                        || (def.getType() == DataType.timeuuid())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getUUID(def.getName()));
                } else if ((def.getType() == DataType.varint())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getVarint(def.getName()));
                }
                metaRow.addCell(def.getName(), metaCell);
                /*
                - ASCII     (1,  String.class),
                - BIGINT    (2,  Long.class),
                - BLOB      (3,  ByteBuffer.class),
                - BOOLEAN   (4,  Boolean.class),
                - COUNTER   (5,  Long.class),
                - DECIMAL   (6,  BigDecimal.class),
                - DOUBLE    (7,  Double.class),
                - FLOAT     (8,  Float.class),
                - INET      (16, InetAddress.class),
                - INT       (9,  Integer.class),
                - TEXT      (10, String.class),
                - TIMESTAMP (11, Date.class),
                - UUID      (12, UUID.class),
                - VARCHAR   (13, String.class),
                - VARINT    (14, BigInteger.class),
                - TIMEUUID  (15, UUID.class),
                LIST      (32, List.class),
                SET       (34, Set.class),
                MAP       (33, Map.class),
                CUSTOM    (0,  ByteBuffer.class);
                */
            }
            crs.addRow(metaRow);
        }
        logger.info("Returning "+crs.size()+" rows");
        return crs;
    }

}
