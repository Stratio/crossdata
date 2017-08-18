package com.stratio.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.qa.cucumber.testng.CucumberRunner;
import com.stratio.qa.utils.BaseTest;
import com.stratio.qa.utils.ThreadProperty;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = {
		"src/test/resources/features/Catalog/CreateCassandraExternalTables.feature",
		"src/test/resources/features/Catalog/CreateMongoDBExternalTables.feature"
	})
public class ATCreateExternalTable extends BaseTest {


	public ATCreateExternalTable() {
	}

	@BeforeClass(groups = {"basic"})
	public void setUp() {
		ThreadProperty.set("Driver", "context");
		ThreadProperty.set("Connector", "external");
	}

	@AfterClass(groups = {"basic"})
	public void cleanUp() {

	}

	@Test(enabled = false, groups = {"advanced"})
	public void ATCreateExternalTable() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}
