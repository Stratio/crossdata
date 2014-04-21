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

package com.stratio.meta.sh.utils;

import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.ConnectResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import jline.console.ConsoleReader;
import jline.console.history.History;
import jline.console.history.MemoryHistory;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class ConsoleUtils {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(ConsoleUtils.class);

    /**
     * Number of days a history entry is maintained.
     */
    private static final int DAYS_HISTORY_ENTRY_VALID = 30;

    /**
     * Private class constructor as all methods are static.
     */
    private ConsoleUtils(){

    }

    public static String stringResult(Result result){
        if(result.hasError()){
            return result.getErrorMessage();
        }
        if(result instanceof QueryResult){
            QueryResult queryResult = (QueryResult) result;
            return stringQueryResult(queryResult);
        } else if (result instanceof CommandResult){
            CommandResult commandResult = (CommandResult) result;
            return commandResult.getResult();
        } else if (result instanceof ConnectResult){
            ConnectResult connectResult = (ConnectResult) result;
            return String.valueOf("Connected with SessionId=" + connectResult.getSessionId());
        } else {
            return "Unknown result";
        }
    }

    private static String stringQueryResult(QueryResult queryResult){
        if(queryResult.getResultSet().isEmpty()){
            return "OK";
        }

        ResultSet resultSet = queryResult.getResultSet();

        Map<String, Integer> colWidths = calculateColWidths(resultSet);

        String bar = StringUtils.repeat('-', getTotalWidth(colWidths) + (colWidths.values().size() * 3) + 1);

        StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
        sb.append(bar).append(System.getProperty("line.separator"));
        boolean firstRow = true;
        for(Row row: resultSet){
            sb.append("| ");

            if(firstRow){
                for(String key: row.getCells().keySet()){
                    sb.append(StringUtils.rightPad("\033[34;1m"+key+"\033[0m ", colWidths.get(key)+12)).append("| ");
                }
                sb.append(System.getProperty("line.separator"));
                sb.append(bar);
                sb.append(System.getProperty("line.separator"));
                sb.append("| ");
                firstRow = false;
            }

            Map<String, Cell> cells = row.getCells();
            for(String key: cells.keySet()){
                Cell cell = cells.get(key);
                String str = String.valueOf(cell.getValue());
                sb.append(StringUtils.rightPad(str, colWidths.get(key)));
                sb.append(" | ");
            }
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(bar).append(System.getProperty("line.separator"));
        return sb.toString();
    }

    private static Map<String, Integer> calculateColWidths(ResultSet resultSet) {
        Map<String, Integer> colWidths = new HashMap<>();
        // Get column names
        Row firstRow = resultSet.iterator().next();
        for(String key: firstRow.getCells().keySet()){
            colWidths.put(key, key.length());
        }
        // Find widest cell content of every column
        for(Row row: resultSet){
            for(String key: row.getCells().keySet()){
                String cellContent = String.valueOf(row.getCell(key).getValue());
                int currentWidth = colWidths.get(key);
                if(cellContent.length() > currentWidth){
                    colWidths.put(key, cellContent.length());
                }
            }
        }
        return colWidths;
    }

    private static int getTotalWidth(Map<String, Integer> colWidths) {
        int totalWidth = 0;
        for(int width: colWidths.values()){
            totalWidth+=width;
        }
        return totalWidth;
    }

    public static File retrieveHistory(ConsoleReader console, SimpleDateFormat sdf) throws IOException {
        Date today = new Date();
        String workingDir = System.getProperty("user.home");
        File dir = new File(workingDir, ".meta");
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dir.getPath()+"/history.txt");
        if (!file.exists()){
            file.createNewFile();
        }
        if(LOG.isDebugEnabled()) {
            LOG.debug("Retrieving history from " + file.getAbsolutePath());
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        History oldHistory = new MemoryHistory();
        DateTime todayDate = new DateTime(today);
        String line;
        String[] lineArray;
        Date lineDate;
        String lineStatement;
        try{
            while ((line = br.readLine()) != null) {
                lineArray = line.split("\\|");
                lineDate = sdf.parse(lineArray[0]);
                if(Days.daysBetween(new DateTime(lineDate), todayDate).getDays()<DAYS_HISTORY_ENTRY_VALID){
                    lineStatement = lineArray[1];
                    oldHistory.add(lineStatement);
                }
            }
        }catch(ParseException ex) {
            LOG.error("Cannot parse date", ex);
        }catch(Exception ex){
            LOG.error("Cannot read all the history", ex);
        }
        console.setHistory(oldHistory);
        LOG.info("History retrieved");
        return file;
    }

    public static void saveHistory(ConsoleReader console, File file, SimpleDateFormat sdf) throws IOException{
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file, true);
        try (BufferedWriter bufferWriter = new BufferedWriter(fileWriter)) {
            History history = console.getHistory();
            ListIterator<History.Entry> histIter = history.entries();
            while(histIter.hasNext()){
                History.Entry entry = histIter.next();
                bufferWriter.write(sdf.format(new Date()));
                bufferWriter.write("|");
                bufferWriter.write(entry.value().toString());
                bufferWriter.newLine();
            }
            bufferWriter.flush();
        }
    }

}
