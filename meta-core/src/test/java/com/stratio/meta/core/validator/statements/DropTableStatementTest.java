package com.stratio.meta.core.validator.statements;

import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class DropTableStatementTest extends BasicValidatorTest {

    @Test
    public void validate_ok(){
        String inputText = "DROP TABLE demo.users;";
        validateOk(inputText, "validate_ok");
    }

    @Test
    public void validate_notExists_tablename(){
        String inputText = "DROP TABLE unknown_table;";
        validateFail(inputText, "validate_notExists_tablename");
    }

    @Test
    public void validate_notExists_keyspace(){
        String inputText = "DROP TABLE unknown.users;";
        validateFail(inputText, "validate_notExists_keyspace");
    }

}
