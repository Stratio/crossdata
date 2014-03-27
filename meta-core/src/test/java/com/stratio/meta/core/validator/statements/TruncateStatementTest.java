package com.stratio.meta.core.validator.statements;


import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class TruncateStatementTest extends BasicValidatorTest {

    @Test
    public void validate_ok(){
        String inputText = "TRUNCATE demo.users;";
        validateOk(inputText, "validate_ok");
    }

    @Test
    public void validate_notExists_tablename(){
        String inputText = "TRUNCATE unknown_table;";
        validateFail(inputText, "validate_notExists_tablename");
    }

    @Test
    public void validate_notExists_keyspace(){
        String inputText = "TRUNCATE unknown.users;";
        validateFail(inputText, "validate_notExists_keyspace");
    }

}
