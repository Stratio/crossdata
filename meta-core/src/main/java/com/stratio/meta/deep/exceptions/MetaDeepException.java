package com.stratio.meta.deep.exceptions;


public class MetaDeepException extends RuntimeException {
    private static final long serialVersionUID = -5451808319155104979L;

    public MetaDeepException(String message) {
        super(message);
    }

    public MetaDeepException(Throwable cause) {
        super(cause);
    }

    public MetaDeepException(String message, Throwable cause) {
        super(message, cause);
    }
}
