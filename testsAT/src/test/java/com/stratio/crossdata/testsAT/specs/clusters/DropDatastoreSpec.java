package com.stratio.crossdata.testsAT.specs.clusters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.testng.Assert;

import com.stratio.crossdata.common.exceptions.ApiException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.validation.NotExistNameException;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.specs.BaseSpec;
import com.stratio.crossdata.specs.Common;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DropDatastoreSpec extends BaseSpec {
    private Result result;

    public DropDatastoreSpec(Common spec) {
        this.commonspec = spec;
    }

    @When("^I execute a DROP_DATASTORE query of the datastore '(.*?)'$")
    public void dropDatastore(String datastore_name) {
        commonspec.getLogger().info("Clearing exception list");
        commonspec.getExceptions().clear();
        try {
            commonspec.getLogger().info("Dropping  datastore " + datastore_name);
            result =  commonspec.getMetaDriver().dropDatastore(datastore_name);
        } catch (NotExistNameException | ApiException e) {
            commonspec.getExceptions().add(e);
            commonspec.getLogger().error(e.getMessage());
        } catch (Exception e){
            commonspec.getExceptions().add(e);
            commonspec.getLogger().error(e.getMessage());
        }
    }
    
    @Then("^The result of drop a datastore is '(.*?)'$")
    public void assertResultDropDatastore(String expectedResult){
        commonspec.getLogger().info("Checking the result of dropping a datastore");
        assertThat("An exception exists", commonspec.getExceptions(),hasSize(equalTo(0)));
        Assert.assertTrue(Boolean.valueOf(expectedResult).equals(result.hasError()), "Error adding a new connector");
    }
}
