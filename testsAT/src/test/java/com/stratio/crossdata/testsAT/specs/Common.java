package com.stratio.crossdata.specs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.specs.CommonG;
import com.stratio.tests.utils.Driver;
import com.stratio.tests.utils.DriverUtil;
import com.stratio.tests.utils.JDBCDriver;
import com.stratio.tests.utils.JDBCDriverUtil;
import com.stratio.tests.utils.MongoDBUtil;
import com.stratio.tests.utils.QueryUtils;
import com.stratio.tests.utils.ThreadProperty;

public class Common extends CommonG {

	private final Logger logger = LoggerFactory.getLogger(ThreadProperty.get("class"));
	private final DriverUtil METADRIVER =  DriverUtil.getInstance();
	private final JDBCDriverUtil JDBCDRIVER = JDBCDriverUtil.getInstance();
	
	private QueryUtils queryUtils;
	private ResultSet result;
	
	public Logger getLogger() {
		return this.logger;
	}
	
	public void setQueryUtils(QueryUtils queryUtils){
		this.queryUtils = queryUtils;
	}
	
	public QueryUtils getQueryUtils() {
		return queryUtils;
	}
	
	public void setResultSet(ResultSet res){
	    this.result = res;
	}
	
	public ResultSet getResultSet(){
	    return this.result;
	}

	public Driver getMetaDriver(){
	    return this.METADRIVER.getDriver();
	}
	
	   public JDBCDriver getJDBCDriver(){
         return this.JDBCDRIVER.getDriver();
	    }
}
