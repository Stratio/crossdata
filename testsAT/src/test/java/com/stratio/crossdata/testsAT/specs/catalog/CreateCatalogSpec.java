package com.stratio.crossdata.testsAT.specs.catalog;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.testsAT.specs.BaseSpec;
import com.stratio.crossdata.testsAT.specs.Common;
import org.testng.Assert;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateCatalogSpec extends BaseSpec{
	private String query = "";
	private String catalog_name = "";
	//Constructor de la clase
	public CreateCatalogSpec(Common spec) {
		this.commonspec = spec;
		
	}
	
	@When(value= "^I execute a CREATE_CATALOG query with this options: '(.*?)' , '(.*?)' and '(.*?)'$", timeout=15000)
	public void create_catalog_query(String if_not_exists, String catalogName, String properties){
		//Inicializacion de array de excepciones
		commonspec.getExceptions().clear();
		catalog_name = catalogName;
		query = commonspec.getQueryUtils().createCatalogQuery(if_not_exists, catalogName, properties);
		commonspec.getLogger().info("CREATE CATALOG QUERY AUTOMATED: " + query);
		try {
            commonspec.getMetaDriver().executeQuery(query);
        } catch (ParsingException | ExecutionException | UnsupportedException | ConnectionException
                | ValidationException e) {
            commonspec.getLogger().error(e.toString());
            commonspec.getExceptions().add(e);
        }
	}
	
	@When(value="^The catalog '(.*?)' has to be droped$", timeout=15000)
    public void drop_catalog_query( String catalogName) {
        // Inicializacion de array de excepciones
        commonspec.getExceptions().clear();
        catalog_name = catalogName;
        query = commonspec.getQueryUtils().dropCatalogQuery("true", catalog_name);
        commonspec.getLogger().info("DROP CATALOG QUERY AUTOMATED:" + query);
        try {
            commonspec.getMetaDriver().executeQuery(query);
        } catch (ParsingException | ExecutionException | UnsupportedException | ConnectionException
                | ValidationException e) {
            commonspec.getLogger().error(e.toString());
            commonspec.getExceptions().add(e);
        }
    }     
	
	@When(value="^The catalog if exists '(.*?)' has to be droped$", timeout=15000)
    public void drop_catalog_query_if_exists( String catalogName) {
        // Inicializacion de array de excepciones
        commonspec.getExceptions().clear();
        catalog_name = catalogName;
        query = commonspec.getQueryUtils().dropCatalogQuery("false", catalog_name);
        commonspec.getLogger().info("DROP CATALOG QUERY AUTOMATED:" + query);
        try {
            commonspec.getMetaDriver().executeQuery(query);
        } catch (ParsingException | ExecutionException | UnsupportedException | ConnectionException
                | ValidationException e) {
            commonspec.getLogger().error(e.toString());
            commonspec.getExceptions().add(e);
        }
    }
	
	@Then("^The result of create catalog query is '(.*)'$")
	public void result_create_catalog_query(String result_expected){
	    Assert.assertTrue(commonspec.getMetaDriver().existsCatalog(result_expected));
	}
	
}
