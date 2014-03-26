package com.stratio.meta.core.validator.statements;

import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class DropKeyspaceStatementTest extends BasicValidatorTest {

    @Test
    public void validate_ok(){
        String inputText = "DROP KEYSPACE demo;";
        validateOk(inputText, "validate_ok");
    }

    @Test
    public void validate_ifNotExists(){
        String inputText = "DROP KEYSPACE IF EXISTS unknown;";
        validateOk(inputText, "validate_ok");
    }

    @Test
    public void validate_notExists(){
        String inputText = "DROP KEYSPACE unknown;";
        validateFail(inputText, "validate_notExists");
    }
}
