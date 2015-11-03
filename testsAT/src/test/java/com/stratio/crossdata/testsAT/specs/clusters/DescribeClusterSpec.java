package com.stratio.crossdata.testsAT.specs.clusters;

import com.stratio.crossdata.specs.BaseSpec;
import com.stratio.crossdata.specs.Common;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DescribeClusterSpec extends BaseSpec{
	
	private String query = "";
    private String clusterName = "";
	//Constructor de la clase
	public DescribeClusterSpec(Common spec) {
		this.commonspec = spec;
		
	}
	
	@When("^I execute a DESCRIBE_CLUSTER query: '(.*?)'$")
	public void describe_cluster_query(String cluster_name){
		//Inicializacion de array de excepciones
		commonspec.getExceptions().clear();
		clusterName = cluster_name;
		query = commonspec.getQueryUtils().describeClusterQuery(cluster_name);
		commonspec.getLogger().info("DESCRIBE CLUSTER QUERY AUTOMATED:" + query);
		//Ejecucion de la query dentro de sentencia try-catch
		//Dentro del catch añadir a la lista de exepciones.
	}
	
	@Then("^The result of DESCRIBE_CLUSTER query is '(.*)'$")
	public void result_describe_cluster_query(String result_expected){
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
	
	@When("^I describe all the clusters$")
	public void describe_clusters_query(){
		//Inicializacion de array de excepciones
		commonspec.getExceptions().clear();
		query = commonspec.getQueryUtils().describeClusterQuery("");
		commonspec.getLogger().info("DESCRIBE STORAGES QUERY AUTOMATED:" + query);
		//Ejecucion de la query dentro de sentencia try-catch
		//Dentro del catch añadir a la lista de exepciones.
	}
	
	@Then("^The result of describe all clusters is '(.*)'$")
	public void result_describe_clusters(String Result){
		 if (commonspec.getExceptions().size() > 0){
			 //Obtejemos la excepcion
			 String exception = commonspec.getExceptions().get(0).getClass().getSimpleName();
			 commonspec.getLogger().info("RESULT OBTAINED : " + exception);
			 //commonspec.getLogger().info("RESULT EXPECTED : " + result_expected);
			 //Aqui va el assert
		 } else{
			 //Obtencion del resultado del driver de meta
			 
			 //commonspec.getLogger().info("RESULT OBTAINED : " + result);
			 //commonspec.getLogger().info("RESULT EXPECTED : " + result_expected);
			 //Aqui va el assert
		 }
	}
	
}
