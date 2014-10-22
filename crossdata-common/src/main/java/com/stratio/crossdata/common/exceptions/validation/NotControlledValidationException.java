package com.stratio.crossdata.common.exceptions.validation;

import com.stratio.crossdata.common.exceptions.ValidationException;

public class NotControlledValidationException extends ValidationException {
    public NotControlledValidationException(Exception ex) {
        super(ex);
    }
}
