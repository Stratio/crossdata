package com.stratio.crossdata.common.exceptions.validation;

import com.stratio.crossdata.common.exceptions.ValidationException;

public class NotConnectionException extends ValidationException {
    public NotConnectionException(Exception ex) {
        super(ex);
    }
}
