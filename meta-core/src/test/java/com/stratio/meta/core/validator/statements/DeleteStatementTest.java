package com.stratio.meta.core.validator.statements;


import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class DeleteStatementTest extends BasicValidatorTest {

    @Test
    public void validate_ok(){
        String inputText = "DELETE FROM demo.users WHERE name = 'name_0';";
        validateOk(inputText, "validate_ok");
    }

    @Test
    public void validate_notExists_tablename(){
        String inputText = "DELETE FROM unknown_table WHERE name = 'name_0';";
        validateFail(inputText, "validate_notExists_tablename");
    }

    @Test
    public void validate_where_2columns_ok(){
        String inputText = "DELETE FROM demo.users WHERE name = 'name_0' AND age = 15;";
        validateOk(inputText, "validate_where_2columns_ok");
    }

    @Test
    public void validate_where_columnUnknown(){
        String inputText = "DELETE FROM demo.users WHERE unknown = 'name_0';";
        validateFail(inputText, "validate_notExists_tablename");
    }

    @Test
    public void validate_where_integerFail(){
        String inputText = "DELETE FROM demo.users WHERE name = 'name_0' AND age = '15';";
        validateFail(inputText, "validate_notExists_tablename");
    }

    @Test
    public void validate_where_stringFail(){
        String inputText = "DELETE FROM demo.users WHERE name = 15 AND age = 15;";
        validateFail(inputText, "validate_notExists_tablename");
    }

}
