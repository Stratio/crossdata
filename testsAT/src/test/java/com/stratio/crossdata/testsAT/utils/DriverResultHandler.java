package com.stratio.crossdata.testsAT.specs.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.crossdata.common.result.IDriverResultHandler;
import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.specs.BaseSpec;
import com.stratio.crossdata.specs.Common;

public class DriverResultHandler extends BaseSpec implements IDriverResultHandler {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());

    public DriverResultHandler(Common spec){
        this.commonspec = spec;
    }
    
    @Override
    public void processAck(String queryId, QueryStatus status) {
        
    }

    @Override
    public void processError(Result errorResult) {
        logger.error("");
        logger.error("ERROR ASYNC RESULT: " + errorResult.toString());
        logger.error("");
        commonspec.getMetaDriver().setAsyncResults(errorResult);

    }

    @Override
    public void processResult(Result result) {
        logger.info("PROCESS ASYNC RESULT");
        commonspec.getMetaDriver().setAsyncResults(result);
        
    }

}
