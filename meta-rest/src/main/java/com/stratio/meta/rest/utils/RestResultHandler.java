package com.stratio.meta.rest.utils;

import java.util.HashMap;

import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.IResultHandler;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.common.data.MetaResultSet;

public class RestResultHandler implements IResultHandler {

  private QueryStatus status;
  private String queryId;
  private Result errorResult;
  private HashMap<String, Result> lastResults = new HashMap<String, Result>();

  public RestResultHandler() {
    status = QueryStatus.NONE;
    queryId = "";
    errorResult = null;
  }

  @Override
  public void processAck(String queryId, QueryStatus status) {
    this.status = status;
  }

  @Override
  public void processError(Result errorResult) {
    this.errorResult = errorResult;
  }

  @Override
  public void processResult(Result result) {
    this.queryId = result.getQueryId();
    if (QueryResult.class.isInstance(result)) {
      QueryResult r = QueryResult.class.cast(result);
      QueryResult last = (QueryResult)lastResults.get(queryId);
      if (last == null) { // no hay resultado anterior
        lastResults.put(queryId, r);
      } else {
        if (last.getResultPage() != r.getResultPage()) { // result es nuevo
          ((MetaResultSet) last.getResultSet()).getRows().addAll(
              ((MetaResultSet) r.getResultSet()).getRows());// concatena con los resultados anteriores
          // lastResults.put(queryId, r); // actualizamos el anterior result con el actual
          // queryResult.getResultSet.asInstanceOf[MetaResultSet].getRows.addAll(
          // r.getResultSet.asInstanceOf[MetaResultSet].getRows)
        }
      }
    }else if(CommandResult.class.isInstance(result)){
      CommandResult r = CommandResult.class.cast(result);
      CommandResult last = (CommandResult)lastResults.get(queryId);
      if(last == null){
        lastResults.put(queryId, r);
      }
    }
  }

  public QueryStatus getStatus() {
    return status;
  }

  public String getQueryId() {
    return queryId;
  }

  public Result getErrorResult() {
    return errorResult;
  }

  public Result getResult(String queryId) {
    Result result = lastResults.get(queryId);
    lastResults.remove(queryId);
    return result;
  }


}
