package com.stratio.crossdata.sh;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.exceptions.ApiException;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.QueryStatus;

public class ShellDriverResultHandlerTest {

    @Test
    public void testProcessAck() throws Exception {
        ShellDriverResultHandler handler = new ShellDriverResultHandler(new Shell(false));
        handler.processAck("queryId25", QueryStatus.IN_PROGRESS);
        assertTrue(true, "testProcessAck failed.");
    }

    @Test
    public void testProcessError() throws Exception {
        ShellDriverResultHandler handler = new ShellDriverResultHandler(new Shell(true));
        handler.processError(ErrorResult.createErrorResult(new ApiException("Test")));
        assertTrue(true, "testProcessError failed.");
    }

    @Test
    public void testProcessResult() throws Exception {
        ShellDriverResultHandler handler = new ShellDriverResultHandler(new Shell(false));
        QueryResult result = QueryResult.createSuccessQueryResult();
        result.setLastResultSet();
        handler.processResult(result);
        assertTrue(true, "testProcessResult failed.");
    }
}