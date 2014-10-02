package com.stratio.meta.common.exceptions;

/**
 * Critical execution exceptions.
 */
public class CriticalExecutionException extends ExecutionException{

    public CriticalExecutionException(String message) {
        super(message);
    }

    public CriticalExecutionException(String message, Exception exception){
        super(message, exception);
    }
}
