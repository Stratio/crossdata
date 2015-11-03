package com.stratio.crossdata.testsAT.specs.clusters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.testng.Assert;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.testsAT.specs.BaseSpec;
import com.stratio.crossdata.testsAT.specs.Common;
import com.stratio.tests.utils.ThreadProperty;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AttachConnectorSpec extends BaseSpec {
    String query = "";
    String clusterName = "";

    public AttachConnectorSpec(Common spec) {
        this.commonspec = spec;
    }

    @When("^I execute a ATTACH CONNECTOR query  for a connector:$")
    public void executeAttachClusterFromDataTable(DataTable table) {
        String connector = ThreadProperty.get("connectorPath");
        List<List<String>> rows = table.raw();
        for (int i = 1; i < rows.size(); i++) {
            if (connector.contains(rows.get(i).get(0))) {
                executeAttachConnectorQuery(rows.get(i).get(1), rows.get(i).get(2), rows.get(i).get(3));
            }
        }
    }

    @When("^I execute a ATTACH CONNECTOR query  for '(.*?)', '(.*?)' and '(.*?)'$")
    public void executeAttachConnectorQuery(String ConnectorName, String ClusterName, String Options) {
        commonspec.getLogger().info("Clearing exception list");
        commonspec.getExceptions().clear();
        query = commonspec.getQueryUtils().attachConnector(ConnectorName, ClusterName, Options);
        commonspec.getLogger().info("Query to execute: " + query);
        try {
            commonspec.getMetaDriver().executeQuery(query);

        } catch (ParsingException | ExecutionException | UnsupportedException | ConnectionException
                | ValidationException e) {
            commonspec.getExceptions().add(e);
            commonspec.getLogger().error(e.getMessage());
        }
    }

    @Then("^The result of ATTACH CONNECTOR query is '(.*?)'$")
    public void checkAttachClusterQuery(String expectedResult) {
        Boolean expected = Boolean.valueOf(expectedResult);
        CommandResult result = (CommandResult) commonspec.getMetaDriver().getResult();
        commonspec.getLogger().info("Checking the result of attaching new connector");
        assertThat("An exception exists", commonspec.getExceptions(), hasSize(equalTo(0)));
        Assert.assertTrue(Boolean.valueOf(expected).equals(result.hasError()), "Error adding a new connector");
        assertThat("The result is different from expected", result.getResult().toString(),
                equalTo("CONNECTOR attached successfully"));
    }
    
}
