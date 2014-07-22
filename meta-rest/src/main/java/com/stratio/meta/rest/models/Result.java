package com.stratio.meta.rest.models;

public class Result {

  private String currentCatalog;

  private boolean lastResultSet;

  private boolean catalogChanged;

  private ResultSet resultSet;

  private int resultPage;

  private String queryId;

  public Result(String currentCatalog, boolean lastResultSet, boolean catalogChanged,
      ResultSet resultSet, int resultPage, String queryId) {
    super();
    this.currentCatalog = currentCatalog;
    this.lastResultSet = lastResultSet;
    this.catalogChanged = catalogChanged;
    this.resultSet = resultSet;
    this.resultPage = resultPage;
    this.queryId = queryId;
  }

  public Result() {}

  public String getCurrentCatalog() {
    return currentCatalog;
  }

  public void setCurrentCatalog(String currentCatalog) {
    this.currentCatalog = currentCatalog;
  }

  public boolean isLastResultSet() {
    return lastResultSet;
  }

  public void setLastResultSet(boolean lastResultSet) {
    this.lastResultSet = lastResultSet;
  }

  public boolean isCatalogChanged() {
    return catalogChanged;
  }

  public void setCatalogChanged(boolean catalogChanged) {
    this.catalogChanged = catalogChanged;
  }

  public ResultSet getResultSet() {
    return resultSet;
  }

  public void setResultSet(ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  public int getResultPage() {
    return resultPage;
  }

  public void setResultPage(int resultPage) {
    this.resultPage = resultPage;
  }

  public String getQueryId() {
    return queryId;
  }

  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  
}
