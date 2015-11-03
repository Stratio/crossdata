package com.stratio.crossdata.testsAT.specs.clusters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.testng.Assert;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.specs.BaseSpec;
import com.stratio.crossdata.specs.Common;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DetachClusterSpec  extends BaseSpec {


	//Constructor de la clase
	public DetachClusterSpec(Common spec) {
		this.commonspec = spec;
		
	}
	
	@When(value="^I execute a DETACH_CLUSTER query over '(.*?)'$", timeout=30000)
	public void detachCluster(String clusterName){
	    commonspec.getLogger().info("Clearing exception list");
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Detaching " + clusterName + " from datastore");
        String query = "DETACH CLUSTER " + clusterName + ";";
        commonspec.getLogger().info("Parsed query: " + query);
        try {
            commonspec.getMetaDriver().executeQuery(query);
        } catch (ParsingException | ExecutionException | UnsupportedException | ConnectionException
                | ValidationException e) {
            commonspec.getExceptions().add(e);
            commonspec.getLogger().error(e.getMessage());
        }
	}
	
	@Then("^The result of detach a cluster is '(.*?)'$")
	public void resultDetachCluster(String expected_result){
	    Boolean expected = Boolean.valueOf(expected_result);
        CommandResult result = (CommandResult) commonspec.getMetaDriver().getResult();
        commonspec.getLogger().info("Checking the result of detaching new cluster");
        assertThat("An exception exists", commonspec.getExceptions(), hasSize(equalTo(0)));
        Assert.assertTrue(Boolean.valueOf(expected).equals(result.hasError()), "Error adding a new connector");
//        assertThat("The result is different from expected", result.getResult().toString(),
//                equalTo("Cluster attached successfully"));
	}
}
