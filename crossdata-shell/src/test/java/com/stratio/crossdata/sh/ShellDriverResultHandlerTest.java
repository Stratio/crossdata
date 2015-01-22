package com.stratio.crossdata.sh;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.exceptions.ApiException;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.QueryStatus;

public class ShellDriverResultHandlerTest {

    @Test
    public void testProcessAck() throws Exception {
        boolean ok=false;
        try {
            ShellDriverResultHandler handler = new ShellDriverResultHandler(new Shell(false));
            handler.processAck("queryId25", QueryStatus.IN_PROGRESS);
            ok=true;
        }catch(Exception e){
            Assert.fail(e.getMessage());
        }
        assertTrue(ok, "testProcessAck failed.");
    }

    @Test
    public void testProcessError() throws Exception {
        boolean ok=false;
        try {
            ShellDriverResultHandler handler = new ShellDriverResultHandler(new Shell(true));
            handler.processError(ErrorResult.createErrorResult(new ApiException("Test")));
            ok=true;
        }catch(Exception e){
            fail("testProcessError failed.");
        }
        assertTrue(ok, "testProcessError failed.");
    }

    @Test
    public void testProcessResult() throws Exception {
        boolean ok=false;
        try {
            ShellDriverResultHandler handler = new ShellDriverResultHandler(new Shell(false));
            QueryResult result = QueryResult.createSuccessQueryResult();
            result.setLastResultSet();
            handler.processResult(result);
            ok = true;
        }catch (Exception e){
            fail("testProcessResult failed.");
        }
        assertTrue(true, "testProcessResult failed.");
    }
}
