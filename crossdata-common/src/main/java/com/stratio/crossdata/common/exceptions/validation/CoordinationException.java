package com.stratio.crossdata.common.exceptions.validation;

public class CoordinationException extends Exception {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = -885849978386466715L;

    public CoordinationException(String message) {
        super(message);
    }

    public CoordinationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
