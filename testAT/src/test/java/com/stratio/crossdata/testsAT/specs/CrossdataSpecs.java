package com.stratio.crossdata.testsAT.specs;

import org.apache.spark.sql.DataFrame;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static com.stratio.tests.utils.DataFrameAssert.asserThat;
/**
 * Created by hdominguez on 13/10/15.
 */
public class CrossdataSpecs extends BaseSpec  {
    int a = 0;
    public CrossdataSpecs(Common spec) {
        this.commonspec = spec;
    }
    DataFrame df;
    @When(value = "^I execute '(.*?)'$")
    public void execute(String query){
        commonspec.getLogger().info("Executing native query: " + query );
        commonspec.getXdContext().executeQuery(query);
        commonspec.getLogger().info("Query executed");
    }

    @Then(value = "The result has to have '(.*?)' rows:$")
    public void assertResultLengh(String rows, DataTable table){
        commonspec.getLogger().info("The result obtained is: ");
        commonspec.getXdContext().showDataframe();
        asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
        asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
        asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResults(table.raw());
    }

    @Then(value = "The result has to have '(.*?)' rows ignoring the order:$")
    public void assertResultLenghIgnoringOrder(String rows, DataTable table){
        commonspec.getLogger().info("The result obtained is: ");
        commonspec.getXdContext().showDataframe();
        asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
        asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
        asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResultsIgnoringOrder(table.raw());
    }

}