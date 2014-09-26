package com.stratio.meta2.core.statements;

import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

public class DropDataStoreStatement extends MetadataStatement {

    private String name;

    public DropDataStoreStatement(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DROP DATASTORE " + name;
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(Validation.MUST_EXIST_DATASTORE);
    }

    public String getName() {
        return name;
    }
}
