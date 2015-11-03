package com.stratio.crossdata.specs.exceptions;

import com.stratio.crossdata.specs.BaseSpec;
import com.stratio.crossdata.specs.Common;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;

import cucumber.api.java.en.When;

public class ConnectionExceptionSpec  extends BaseSpec{

	public ConnectionExceptionSpec(Common spec) {
		this.commonspec = spec;
	}
	
	@When("^I disconnect the driver from the server$")
	public void dis_from_server(){
		commonspec.getLogger().info("Cleaning exceptions list");
		commonspec.getExceptions().clear();
		try {
			commonspec.getLogger().info("Disconnection of the driver from the server");
			commonspec.getMetaDriver().disconnect();
		} catch (ConnectionException e) {
		    commonspec.getLogger().error(e.toString());
			commonspec.getExceptions().add(e);
		}
	}
	@When("^I execute a simple query: '(.*?)'$")
	public void execute_simple_query(String statement){
		commonspec.getLogger().info("Executing a simple query : " + statement);
		try {
			commonspec.getMetaDriver().executeQuery(statement);
		} catch (ParsingException | ValidationException | ExecutionException
				| UnsupportedException | ConnectionException e) {
		    commonspec.getLogger().error(e.toString());
			commonspec.getExceptions().add(e);
		}
	}
}
