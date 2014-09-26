package com.stratio.meta2.core.statements;

import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

public class DropConnectorStatement extends MetadataStatement {

    private String name;

    public DropConnectorStatement(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DROP CONNECTOR " + name;
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(Validation.MUST_EXIST_CONNECTOR);
    }

    public String getName() {
        return name;
    }
}
