package com.stratio.crossdata.testsAT.specs.clusters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.io.FileNotFoundException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.testng.Assert;

import com.stratio.crossdata.common.exceptions.ApiException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.validation.ExistNameException;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.specs.BaseSpec;
import com.stratio.crossdata.specs.Common;
import com.stratio.tests.utils.ThreadProperty;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AddConnectorSpec extends BaseSpec {

    Result result;

    public AddConnectorSpec(Common spec) {
        this.commonspec = spec;
    }

    @When(value="^I execute a ADD_CONNECTOR query with this options: '(.*?)'$", timeout=30000)
    public void executeAddConnectorQuery(String connectorPath) {
        ThreadProperty.set("connectorPath", connectorPath);
        commonspec.getLogger().info("Clearing exception list");
        commonspec.getExceptions().clear();
        URL url = AddConnectorSpec.class.getResource("/manifest/" + connectorPath);
        commonspec.getLogger().info("Adding new connector xml file : " + connectorPath);
        try {
            result = commonspec.getMetaDriver().addConnector(url.getPath());

        } catch (FileNotFoundException | JAXBException | ExistNameException | ApiException e) {
            commonspec.getExceptions().add(e);
            commonspec.getLogger().error(e.getMessage());
        }
    }
    
    @Then("^The result of add new connector query is '(.*?)'$")
    public void checkResultOfAddingConnector(String expected_result){
        Boolean expected = Boolean.valueOf(expected_result);
        commonspec.getLogger().info("Checking the result of adding new connector");
        assertThat("An exception exists", commonspec.getExceptions(),hasSize(equalTo(0)));
        Assert.assertTrue(Boolean.valueOf(expected).equals(result.hasError()),"Error adding a new connector");
        commonspec.getExceptions().clear();
    }
    
    @When(value="^I want to know the description of a connector '(.*?)'", timeout=30000)
    public void executeDescribeDatastore(String conectorName){
        commonspec.getLogger().info("Executing a describe datastore query");
        commonspec.getMetaDriver().describeConnector(conectorName);
    }
    

}
