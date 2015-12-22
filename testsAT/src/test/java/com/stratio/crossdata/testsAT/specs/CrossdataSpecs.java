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


import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static com.stratio.tests.utils.DataFrameAssert.asserThat;
import static com.stratio.tests.utils.SQLResultAssert.asserThat;
import com.stratio.tests.utils.ThreadProperty;

/**
 * Created by hdominguez on 13/10/15.
 */
public class CrossdataSpecs extends BaseSpec  {

    public CrossdataSpecs(Common spec) {
        this.commonspec = spec;
    }

    @When(value = "^I execute '(.*?)'$")
    public void execute(String query){
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Executing native query: " + query );
        if(ThreadProperty.get("Driver").equals("context")) {
            try {
                commonspec.getXdContext().executeQuery(query);
            } catch (Exception e) {
                commonspec.getLogger().info(e.toString());
                commonspec.getExceptions().add(e);
            }
            commonspec.getLogger().info("Query executed");
        }else{
            commonspec.getLogger().info("Executing native query in driver: " + query );
            try {
            commonspec.getXdDriver().executeSyncQuery(query);
            } catch (Exception e) {
                commonspec.getLogger().info(e.toString());
                commonspec.getExceptions().add(e);
            }
            commonspec.getLogger().info("Query executed");

        }
    }

    @Then(value = "The result has to have '(.*?)' rows:$")
    public void assertResult(String rows, DataTable table){
        if(ThreadProperty.get("Driver").equals("context")) {
            commonspec.getLogger().info("The result obtained is: ");
            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResultsNative(table.raw());
        }else{
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
            asserThat(commonspec.getXdDriver().getResult()).equalsSQLResults(table.raw());
        }
    }

    @Then(value = "The spark result has to have '(.*?)' rows:$")
    public void assertResultSpark(String rows, DataTable table){
        if(ThreadProperty.get("Driver").equals("context")) {
            commonspec.getLogger().info("The result obtained is: ");
            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResultsSpark(table.raw());
        }else{
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
            asserThat(commonspec.getXdDriver().getResult()).equalsSQLResults(table.raw());
        }
    }

    @Then(value = "The result has to have '(.*?)' rows$")
    public void assertResultLengh(String rows, DataTable table){
        if(ThreadProperty.get("Driver").equals("context")) {
            commonspec.getLogger().info("The result obtained is: ");
            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
        }else{
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
        }
    }

    @Then(value = "The result has to have '(.*?)' rows ignoring the order:$")
    public void assertResultLenghIgnoringOrder(String rows, DataTable table){
        commonspec.getLogger().info("The result obtained is: ");
        if(ThreadProperty.get("Driver").equals("context")) {

            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResultsIgnoringOrderNative(table.raw());
        }else{
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
            asserThat(commonspec.getXdDriver().getResult()).equalsResultsIgnoringOrderNative(table.raw());
        }
    }

    @Then(value = "^Drop the spark tables$")
    public void dropTables(){
        commonspec.getLogger().info("The result obtained is: ");
        commonspec.getXdContext().dropTables();
    }
}