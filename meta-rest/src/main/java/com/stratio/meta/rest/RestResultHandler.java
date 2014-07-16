package com.stratio.meta.rest;

import com.stratio.meta.common.result.IResultHandler;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.common.result.Result;

public class RestResultHandler implements IResultHandler {

  private QueryStatus status;
  private String queryId;
  private Result errorResult;
  private Result result;

  public RestResultHandler() {
    status = QueryStatus.NONE;
    queryId = "";
    errorResult = null;
    result = null;
  }

  @Override
  public void processAck(String queryId, QueryStatus status) {
    this.queryId = queryId;
    this.status = status;
  }

  @Override
  public void processError(Result errorResult) {
    this.errorResult = errorResult;
  }

  @Override
  public void processResult(Result result) {
    this.result = result;
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

  public Result getResult() {
    return result;
  }


}
