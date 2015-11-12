/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    public void assertResult(String rows, DataTable table){
        commonspec.getLogger().info("The result obtained is: ");
        commonspec.getXdContext().showDataframe();
        asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
        asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
        asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResults(table.raw());
    }

    @Then(value = "The result has to have '(.*?)' rows$")
    public void assertResultLengh(String rows, DataTable table){
        commonspec.getLogger().info("The result obtained is: ");
        commonspec.getXdContext().showDataframe();
        asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
        asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));

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