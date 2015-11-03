package com.stratio.crossdata.testsAT.specs;

import com.stratio.tests.utils.Driver;
import com.stratio.tests.utils.QueryUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class HookSpec extends BaseSpec {

	public QueryUtils query;
	public Driver metaDriver;
	public HookSpec() {		
	}
	
	public HookSpec(Common spec) {
		this.commonspec = spec;
	}
	
	@Before()
	public void Setup(){
		query = new QueryUtils();
		commonspec.setQueryUtils(query);
	}

		
	@After()
	public void CleanUp() {

	}
}
