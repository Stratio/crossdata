package com.stratio.crossdata.testsAT.specs.clusters;

import com.stratio.crossdata.testsAT.specs.BaseSpec;
import com.stratio.crossdata.testsAT.specs.Common;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class DetachConnector extends BaseSpec{

	private String query = "";
	private String connectorName = "";
	//Constructor de la clase
	public DetachConnector(Common spec) {
		this.commonspec = spec;
	}
	
	@Given("^I execute a DETACH_CONNECTOR query with this options: '(.*?)' and '(.*?)'$")
	public void detach_connector_statement(String connector_name, String cluster_name){
		commonspec.getExceptions().clear();
		connectorName = connector_name;
		query = commonspec.getQueryUtils().detachConnector(connector_name, cluster_name);
		commonspec.getLogger().info("DETACH CONNECTOR QUERY AUTOMATED:" + query);
	}
	@Then("^The result of DETACH_CONNECTOR query is (.*?)$")
	public void result_detach_connector_statement(String result_expected){
		 if (commonspec.getExceptions().size() > 0){
			 //Obtejemos la excepcion
			 String exception = commonspec.getExceptions().get(0).getClass().getSimpleName();
			 commonspec.getLogger().info("RESULT OBTAINED : " + exception);
			 commonspec.getLogger().info("RESULT EXPECTED : " + result_expected);
			 //Aqui va el assert
		 } else{
			 //Obtencion del resultado del driver de meta
			 //commonspec.getLogger().info("RESULT OBTAINED : " + result);
			 commonspec.getLogger().info("RESULT EXPECTED : " + result_expected);
			 //Aqui va el assert
		 }
	}
}
