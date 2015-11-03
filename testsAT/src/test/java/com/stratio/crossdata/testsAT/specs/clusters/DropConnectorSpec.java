package com.stratio.crossdata.testsAT.specs.clusters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.testng.Assert;

import com.stratio.crossdata.common.exceptions.ApiException;
import com.stratio.crossdata.common.exceptions.validation.NotExistNameException;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.testsAT.specs.BaseSpec;
import com.stratio.crossdata.testsAT.specs.Common;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DropConnectorSpec extends BaseSpec {
    private Result result;

    public DropConnectorSpec(Common spec) {
        this.commonspec = spec;
    }

    @When("^I execute a DROP_CONNECTOR query of the datastore '(.*?)'$")
    public void dropDatastore(String connector_name) {
        commonspec.getLogger().info("Clearing exception list");
        commonspec.getExceptions().clear();
        try {
            commonspec.getLogger().info("Dropping  connector " + connector_name);
            result = commonspec.getMetaDriver().dropConnector(connector_name);
        } catch (NotExistNameException | ApiException e) {
            commonspec.getExceptions().add(e);
            commonspec.getLogger().error(e.getMessage());
        }
    }

    @Then("^The result of drop a connector is '(.*?)'$")
    public void assertResultDropDatastore(String expectedResult) {
        Boolean expected = Boolean.valueOf(expectedResult);
        commonspec.getLogger().info("Checking the result of dropping new datastore");
        assertThat("An exception exists", commonspec.getExceptions(),hasSize(equalTo(0)));
        Assert.assertTrue(Boolean.valueOf(expected).equals(result.hasError()),"Error dropping a new datastore");
        commonspec.getExceptions().clear();
    }
}
