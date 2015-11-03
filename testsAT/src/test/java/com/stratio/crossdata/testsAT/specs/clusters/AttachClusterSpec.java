package com.stratio.crossdata.testsAT.specs.clusters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
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
import com.stratio.crossdata.specs.BaseSpec;
import com.stratio.crossdata.specs.Common;
import com.stratio.tests.utils.ThreadProperty;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AttachClusterSpec extends BaseSpec {
    private String query = "";

    // Constructor de la clase
    public AttachClusterSpec(Common spec) {
        this.commonspec = spec;

    }

    @When("^I execute a ATTACH CLUSTER query  for a connector:$")
    public void executeAttachClusterFromDataTable(DataTable table) {
        String datastore = ThreadProperty.get("datastorePath");
        List<List<String>> rows = table.raw();
        for (int i = 1; i < rows.size(); i++) {
            if (datastore.contains(rows.get(i).get(0))) {
                executeAttachClusterQuery(rows.get(i).get(1), rows.get(i).get(2), rows.get(i).get(3));
            }
        }
    }

    @When(value="^I execute a ATTACH CLUSTER query for '(.*?)', '(.*?)' and '(.*?)'$", timeout=30000)
    public void executeAttachClusterQuery(String ClusterName, String DatastoreName, String Options) {
        commonspec.getLogger().info("Clearing exception list");
        commonspec.getExceptions().clear();
        query = commonspec.getQueryUtils().attachClusterQuery("false", ClusterName, DatastoreName, Options);
        commonspec.getLogger().info("Query to execute: " + query);
        try {
            commonspec.getMetaDriver().executeQuery(query);

        } catch (ParsingException | ExecutionException | UnsupportedException | ConnectionException
                | ValidationException e) {
            commonspec.getExceptions().add(e);
            commonspec.getLogger().error(e.getMessage());
        }
    }

    @Then("^The result of ATTACH CLUSTER query is '(.*?)'$")
    public void checkAttachClusterQuery(String expectedResult) {
        Boolean expected = Boolean.valueOf(expectedResult);
        CommandResult result = (CommandResult) commonspec.getMetaDriver().getResult();
        commonspec.getLogger().info("Checking the result of attaching new cluster");
        assertThat("An exception exists", commonspec.getExceptions(), hasSize(equalTo(0)));
        Assert.assertTrue(Boolean.valueOf(expected).equals(result.hasError()), "Error adding a new connector");
        assertThat("The result is different from expected", result.getResult().toString(),
                equalTo("Cluster attached successfully"));
    }
    
    @When("^I want to know the description of a cluster '(.*?)'")
    public void executeDescribeCluster(String clusterName){
        commonspec.getLogger().info("Executing a describe datastore query");
        commonspec.getMetaDriver().describeCluster(clusterName);
    }
    
    @Then("^The expected result of describe cluster is: '(.*?)','(.*?)' and '(.*?)'")
    public void assertDescribeCluster(String cluster, String datastoreName, String options){
        CommandResult result = (CommandResult)commonspec.getMetaDriver().getResult();
        String expected_result = result.getResult().toString().toLowerCase();
        commonspec.getLogger().info("Expected result: " + expected_result);
        assertThat("The text "+ cluster.toLowerCase() + " is not contained in the describe result" , expected_result, containsString(cluster.toLowerCase()));
        assertThat("The text "+ datastoreName.toLowerCase() + " is not contained in the describe result" , expected_result, containsString(datastoreName.toLowerCase()));
        String opts = options.substring(0, options.length()-1);
        String[] opts_array = opts.split(",");
        for(int i = 0; i < opts_array.length; i++){
            assertThat("The text "+ datastoreName.toLowerCase() + " is not contained in the describe result" , expected_result, containsString(opts_array[i].toLowerCase()));
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
