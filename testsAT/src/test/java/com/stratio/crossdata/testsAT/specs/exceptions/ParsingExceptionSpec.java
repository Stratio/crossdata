package com.stratio.crossdata.specs.exceptions;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.specs.BaseSpec;
import com.stratio.crossdata.specs.Common;

import cucumber.api.java.en.When;

public class ParsingExceptionSpec extends BaseSpec{

	public ParsingExceptionSpec(Common spec) {
		this.commonspec = spec;
	}
	   
	@When("^I execute a bad formed statement: '(.*?)'$")
	public void execute_bad_formed_statement(String statement){
		commonspec.getLogger().info("Cleaning exceptions list");
		commonspec.getExceptions().clear();
		commonspec.getLogger().info("Executing a bad formed statement : " + statement);
		try {
			commonspec.getMetaDriver().executeQuery(statement);
		} catch (ParsingException | ValidationException | ExecutionException
				| UnsupportedException | ConnectionException e) {
			commonspec.getExceptions().add(e);
		}
	}
}
