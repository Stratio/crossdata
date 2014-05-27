package com.stratio.meta.core.executor;

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.statements.CreateTableStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.streaming.MetaStream;
import com.stratio.streaming.commons.constants.ColumnType;
import com.stratio.streaming.messaging.ColumnNameType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Created by aalcocer on 5/27/14. To generate unit test of proxy actor
 */
public class StreamExecutor {

  public static Result execute(MetaStatement stmt) {

      Result result = QueryResult.createSuccessQueryResult();

      if (stmt instanceof CreateTableStatement) {
          CreateTableStatement cts= (CreateTableStatement) stmt;
          String tableEphimeralName= cts.getTableName() ;
          List<ColumnNameType> columnList = Arrays.asList();
          for (Map.Entry<String, String> column : cts.getColumns().entrySet()) {
              ColumnType type=null;
              if (column.getValue().equalsIgnoreCase("string"))type=ColumnType.STRING;
              else if (column.getValue().equalsIgnoreCase("boolean"))type=ColumnType.BOOLEAN;
              else if (column.getValue().equalsIgnoreCase("doble"))type=ColumnType.DOUBLE;
              else if (column.getValue().equalsIgnoreCase("float"))type=ColumnType.FLOAT;
              else if (column.getValue().equalsIgnoreCase("integer"))type=ColumnType.INTEGER;
              else if (column.getValue().equalsIgnoreCase("long"))type=ColumnType.LONG;
              ColumnNameType StreamColumn= new ColumnNameType(column.getKey(), type);
              columnList.add(StreamColumn);
          }
          return MetaStream.createStream(tableEphimeralName, columnList);
      } else {
          return QueryResult.createFailQueryResult("Not sopported yet");
      }
  }

}

