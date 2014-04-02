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

package com.stratio.meta.common.result;

/*
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
*/

import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;

import java.util.Map;

public class QueryResult extends MetaResult {

    private ResultSet resultSet;
    private String message;

    public QueryResult() {
    }    
    
    public QueryResult(ResultSet resultSet) {
        this.resultSet = resultSet;
    }   

    public QueryResult(String message) {
        this.message = message;
    }        
    
    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int size(){
        CassandraResultSet crs = (CassandraResultSet) resultSet;
        return crs.size();
    }

    @Override
    public void print(){
        logger.info(toString());
    }

    @Override
    public String toString(){
        CassandraResultSet crs = (CassandraResultSet) resultSet;
        crs.reset();
        StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
        sb.append("------------------------------------------------------------------");
        sb.append(System.getProperty("line.separator"));
        boolean firstRow = true;
        while(resultSet.hasNext()){
            Row row = resultSet.next();
            sb.append(" | ");
            if(firstRow){
                for(String key: row.getCells().keySet()){
                    sb.append(key+" | ");
                }
                sb.append(System.getProperty("line.separator"));
                sb.append("------------------------------------------------------------------");
                sb.append(System.getProperty("line.separator"));
                sb.append(" | ");
                firstRow = false;
            }
            Map<String,Cell> cells = row.getCells();
            for(String key: cells.keySet()){
                Cell cell = cells.get(key);
                sb.append(cell.getDatatype().cast(cell.getValue())).append(" | ");
            }
            sb.append(System.getProperty("line.separator"));
        }
        sb.append("------------------------------------------------------------------");
        crs.reset();
        return sb.toString();
    }

//    @Override
//    public void print(){
//        logger.info("\033[32mResult:\033[0m "+resultSet.toString());
//        /////////////////////////////////////////
//        ColumnDefinitions colDefs = resultSet.getColumnDefinitions();
//        int nCols = colDefs.size();
//        List<Row> rows = resultSet.all();
//        if(rows.isEmpty()){
//            logger.info("Empty"+System.getProperty("line.separator"));
//            return;
//        }
//        ArrayList<ArrayList<String>> table = new ArrayList<>();
//        HashMap<Integer, Integer> lenghts = new HashMap<>();
//        int nColumn = 0;
//        int extraSpace = 2;
//        for(ColumnDefinitions.Definition def: colDefs){
//            lenghts.put(nColumn, def.getName().length()+extraSpace);
//            nColumn++;
//        }
//
//        if((lenghts.get(0)-extraSpace) < (rows.size()+" rows").length()){
//            lenghts.put(0, (rows.size()+" rows").length()+extraSpace);
//        }
//
//        for(Row row: rows){
//            ArrayList<String> currentRow = new ArrayList<>();
//            for(int nCol=0; nCol<nCols; nCol++){
//                String cell = "null";
//                com.datastax.driver.core.DataType cellType = colDefs.getType(nCol);
//                if((cellType == com.datastax.driver.core.DataType.varchar()) || (cellType == com.datastax.driver.core.DataType.text())){
//                    cell = row.getString(nCol);
//                } else if (cellType == com.datastax.driver.core.DataType.cint()){
//                    cell = Integer.toString(row.getInt(nCol));
//                } else if (cellType == com.datastax.driver.core.DataType.uuid() || cellType == com.datastax.driver.core.DataType.timeuuid()){
//                    UUID uuid = row.getUUID(nCol);
//                    if(uuid!=null){
//                        cell = uuid.toString();
//                    }
//                } else if (cellType == com.datastax.driver.core.DataType.bigint()){
//                    //BigInteger bi = row.getVarint(nCol);
//                    //cell = bi.toString();
//                    ByteBuffer bb = row.getBytesUnsafe(nCol);
//                    IntBuffer intbb = bb.asIntBuffer();
//                    int tmpInt = 0;
//                    while(intbb.remaining() > 0){
//                        tmpInt = intbb.get();
//                        //System.out.println(tmpInt);
//                    }
//                    //int tmp = bb.asIntBuffer().get(bb.remaining()-1);
//                    cell = Integer.toString(tmpInt);
//                } else if (cellType == com.datastax.driver.core.DataType.timestamp()){
//                    Date date = row.getDate(nCol);
//                    if (date != null){
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss.SSS");
//                        cell = sdf.format(date);
//                    }
//                }
//                // TODO: add all data types
//                currentRow.add(cell);
//                if((cell != null) && ((lenghts.get(nCol)-extraSpace) < cell.length())){
//                    lenghts.put(nCol, cell.length()+extraSpace);
//                }
//                /*if(lenghts.containsKey(nCol)){
//                    if(lenghts.get(nCol) < cell.length()){
//                        lenghts.put(nCol, cell.length());
//                    }
//                } else {
//                    lenghts.put(nCol, cell.length());
//                }*/
//            }
//            table.add(currentRow);
//        }
//
//        // ADD ENDING ROW
//        ArrayList<String> currentRow = new ArrayList<>();
//        for(int nCol=0; nCol<nCols; nCol++){
//            char[] chars = new char[lenghts.get(nCol)];
//            Arrays.fill(chars, '-');
//            currentRow.add(new String(chars));
//        }
//        table.add(currentRow);
//        table.add(currentRow);
//
//        // ADD STARTING ROW
//        table.add(0, currentRow);
//
//        // ADD SEPARATING ROW
//        table.add(1, currentRow);
//
//        // ADD HEADER ROW
//        currentRow = new ArrayList<>();
//        for(int nCol=0; nCol<nCols; nCol++){
//            currentRow.add(colDefs.getName(nCol));
//        }
//        table.add(1, currentRow);
//
//        // ADD INFO ROW
//        currentRow = new ArrayList<>();
//        if(rows.size() != 1){
//            currentRow.add(rows.size()+" rows");
//        } else {
//            currentRow.add("1 row");
//        }
//        for(int nCol=1; nCol<nCols; nCol++){
//            currentRow.add(" ");
//        }
//        table.add(table.size()-1, currentRow);
//
//
//        /*for(Definition definition: resultSet.getColumnDefinitions().asList()){
//            sb.append("\033[4m").append(definition.getName()).append("\033[0m");
//            //sb.append(": ").append(definition.getType().getName().toString()).append(" | ");
//        }*/
//        //sb.append(System.getProperty("line.separator"));
//        /*Properties props = System.getProperties();
//        for(String propKey: props.stringPropertyNames()){
//            System.out.println(propKey+": "+props.getProperty(propKey));
//        }*/
//        /*for(Row row: rows){
//            sb.append("\t").append(row.toString()).append(System.getProperty("line.separator"));
//        }*/
//        StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
//        sb.append(System.getProperty("line.separator"));
//        int nRow = 0;
//        for(ArrayList<String> tableRow: table){
//            if((nRow == 0) || (nRow == 2) || (nRow == (table.size()-3)) || (nRow == (table.size()-1))){
//                sb.append(" ").append("+");
//            } else {
//                sb.append(" ").append("|");
//            }
//            int nCol = 0;
//            //StringUtils.leftPad(query, nCol);
//            for(String cell: tableRow){
//                if(cell == null){
//                    cell = "null";
//                }
//                if((colDefs.getType(nCol) != com.datastax.driver.core.DataType.cint()) && (colDefs.getType(nCol) != com.datastax.driver.core.DataType.bigint())){
//                    cell = StringUtils.rightPad(cell, lenghts.get(nCol)-1);
//                    cell = StringUtils.leftPad(cell, lenghts.get(nCol));
//                } else {
//                    cell = StringUtils.leftPad(cell, lenghts.get(nCol)-1);
//                    cell = StringUtils.rightPad(cell, lenghts.get(nCol));
//                }
//                if(nRow == 1){
//                    sb.append("\033[33;1m").append(cell).append("\033[0m");
//                } else {
//                    sb.append(cell);
//                }
//
//                if((nRow == 0) || (nRow == (table.size()-3)) || (nRow == (table.size()-1))){
//                    /*
//                    if((nCol < (tableRow.size()-1)) && (nCol > 0)){
//                        sb.append("-");
//                    }  else {
//                        sb.append("+");
//                    }
//                    */
//                    if(nCol == (tableRow.size()-1)){
//                        sb.append("+");
//                    } else {
//                        sb.append("-");
//                    }
//                } else if(nRow == 2) {
//                    //if(nCol < (tableRow.size()-1)){
//                        sb.append("+");
//                    //} else {
//                    //    sb.append("|");
//                    //}
//                } else if(nRow == (table.size()-2)){
//                    if(nCol > tableRow.size()-2){
//                        sb.append("|");
//                    } else {
//                        sb.append(" ");
//                    }
//                } else {
//                    sb.append("|");
//                }
//                nCol++;
//            }
//            sb.append(System.getProperty("line.separator"));
//            nRow++;
//        }
//        logger.info(sb.toString());
//    }
    
//    @Override
//    public String toString(){
//        StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
//        sb.append(resultSet.toString());
//        /////////////////////////////////////////
//        ColumnDefinitions colDefs = resultSet.getColumnDefinitions();
//        int nCols = colDefs.size();
//        List<Row> rows = resultSet.all();
//        if(rows.isEmpty()){
//            return "Empty"+System.getProperty("line.separator");
//        }
//        ArrayList<ArrayList<String>> table = new ArrayList<>();
//        HashMap<Integer, Integer> lenghts = new HashMap<>();
//        int nColumn = 0;
//        int extraSpace = 2;
//        for(ColumnDefinitions.Definition def: colDefs){
//            lenghts.put(nColumn, def.getName().length()+extraSpace);
//            nColumn++;
//        }
//
//        if((lenghts.get(0)-extraSpace) < (rows.size()+" rows").length()){
//            lenghts.put(0, (rows.size()+" rows").length()+extraSpace);
//        }
//
//        for(Row row: rows){
//            ArrayList<String> currentRow = new ArrayList<>();
//            for(int nCol=0; nCol<nCols; nCol++){
//                String cell = "null";
//                com.datastax.driver.core.DataType cellType = colDefs.getType(nCol);
//                if((cellType == com.datastax.driver.core.DataType.varchar()) || (cellType == com.datastax.driver.core.DataType.text())){
//                    cell = row.getString(nCol);
//                } else if (cellType == com.datastax.driver.core.DataType.cint()){
//                    cell = Integer.toString(row.getInt(nCol));
//                } else if (cellType == com.datastax.driver.core.DataType.uuid() || cellType == com.datastax.driver.core.DataType.timeuuid()){
//                    UUID uuid = row.getUUID(nCol);
//                    if(uuid!=null){
//                        cell = uuid.toString();
//                    }
//                } else if (cellType == com.datastax.driver.core.DataType.bigint()){
//                    //BigInteger bi = row.getVarint(nCol);
//                    //cell = bi.toString();
//                    ByteBuffer bb = row.getBytesUnsafe(nCol);
//                    IntBuffer intbb = bb.asIntBuffer();
//                    int tmpInt = 0;
//                    while(intbb.remaining() > 0){
//                        tmpInt = intbb.get();
//                        //System.out.println(tmpInt);
//                    }
//                    //int tmp = bb.asIntBuffer().get(bb.remaining()-1);
//                    cell = Integer.toString(tmpInt);
//                } else if (cellType == com.datastax.driver.core.DataType.timestamp()){
//                    Date date = row.getDate(nCol);
//                    if (date != null){
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss.SSS");
//                        cell = sdf.format(date);
//                    }
//                }
//                // TODO: add all data types
//                currentRow.add(cell);
//                if((cell != null) && ((lenghts.get(nCol)-extraSpace) < cell.length())){
//                    lenghts.put(nCol, cell.length()+extraSpace);
//                }
//                /*if(lenghts.containsKey(nCol)){
//                    if(lenghts.get(nCol) < cell.length()){
//                        lenghts.put(nCol, cell.length());
//                    }
//                } else {
//                    lenghts.put(nCol, cell.length());
//                }*/
//            }
//            table.add(currentRow);
//        }
//
//        // ADD ENDING ROW
//        ArrayList<String> currentRow = new ArrayList<>();
//        for(int nCol=0; nCol<nCols; nCol++){
//            char[] chars = new char[lenghts.get(nCol)];
//            Arrays.fill(chars, '-');
//            currentRow.add(new String(chars));
//        }
//        table.add(currentRow);
//        table.add(currentRow);
//
//        // ADD STARTING ROW
//        table.add(0, currentRow);
//
//        // ADD SEPARATING ROW
//        table.add(1, currentRow);
//
//        // ADD HEADER ROW
//        currentRow = new ArrayList<>();
//        for(int nCol=0; nCol<nCols; nCol++){
//            currentRow.add(colDefs.getName(nCol));
//        }
//        table.add(1, currentRow);
//
//        // ADD INFO ROW
//        currentRow = new ArrayList<>();
//        if(rows.size() != 1){
//            currentRow.add(rows.size()+" rows");
//        } else {
//            currentRow.add("1 row");
//        }
//        for(int nCol=1; nCol<nCols; nCol++){
//            currentRow.add(" ");
//        }
//        table.add(table.size()-1, currentRow);
//
//
//        /*for(Definition definition: resultSet.getColumnDefinitions().asList()){
//            sb.append("\033[4m").append(definition.getName()).append("\033[0m");
//            //sb.append(": ").append(definition.getType().getName().toString()).append(" | ");
//        }*/
//        //sb.append(System.getProperty("line.separator"));
//        /*Properties props = System.getProperties();
//        for(String propKey: props.stringPropertyNames()){
//            System.out.println(propKey+": "+props.getProperty(propKey));
//        }*/
//        /*for(Row row: rows){
//            sb.append("\t").append(row.toString()).append(System.getProperty("line.separator"));
//        }*/
//        sb.append(System.getProperty("line.separator"));
//        sb.append(System.getProperty("line.separator"));
//        int nRow = 0;
//        for(ArrayList<String> tableRow: table){
//            if((nRow == 0) || (nRow == 2) || (nRow == (table.size()-3)) || (nRow == (table.size()-1))){
//                sb.append(" ").append("+");
//            } else {
//                sb.append(" ").append("|");
//            }
//            int nCol = 0;
//            //StringUtils.leftPad(query, nCol);
//            for(String cell: tableRow){
//                if(cell == null){
//                    cell = "null";
//                }
//                if((colDefs.getType(nCol) != com.datastax.driver.core.DataType.cint()) && (colDefs.getType(nCol) != com.datastax.driver.core.DataType.bigint())){
//                    cell = StringUtils.rightPad(cell, lenghts.get(nCol)-1);
//                    cell = StringUtils.leftPad(cell, lenghts.get(nCol));
//                } else {
//                    cell = StringUtils.leftPad(cell, lenghts.get(nCol)-1);
//                    cell = StringUtils.rightPad(cell, lenghts.get(nCol));
//                }
//                if(nRow == 1){
//                    sb.append("\033[33;1m").append(cell).append("\033[0m");
//                } else {
//                    sb.append(cell);
//                }
//
//                if((nRow == 0) || (nRow == (table.size()-3)) || (nRow == (table.size()-1))){
//                    /*
//                    if((nCol < (tableRow.size()-1)) && (nCol > 0)){
//                        sb.append("-");
//                    }  else {
//                        sb.append("+");
//                    }
//                    */
//                    if(nCol == (tableRow.size()-1)){
//                        sb.append("+");
//                    } else {
//                        sb.append("-");
//                    }
//                } else if(nRow == 2) {
//                    //if(nCol < (tableRow.size()-1)){
//                        sb.append("+");
//                    //} else {
//                    //    sb.append("|");
//                    //}
//                } else if(nRow == (table.size()-2)){
//                    if(nCol > tableRow.size()-2){
//                        sb.append("|");
//                    } else {
//                        sb.append(" ");
//                    }
//                } else {
//                    sb.append("|");
//                }
//                nCol++;
//            }
//            sb.append(System.getProperty("line.separator"));
//            nRow++;
//        }
//        return sb.toString();
//    }
    
}
