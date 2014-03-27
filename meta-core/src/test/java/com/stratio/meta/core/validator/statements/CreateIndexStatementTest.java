package com.stratio.meta.core.validator.statements;

import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class CreateIndexStatementTest extends BasicValidatorTest {

    @Test
    public void validate_default_noName_ok(){
        String inputText = "CREATE DEFAULT INDEX ON demo.users (email);";
        validateOk(inputText, "validate_default_noName_ok");
    }

    @Test
    public void validate_default_named_ok(){
        String inputText = "CREATE DEFAULT INDEX new_index ON demo.users (email);";
        validateOk(inputText, "validate_default_named_ok");
    }

    @Test
    public void validate_default_ifNotExists_ok(){
        String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS users_gender_idx ON demo.users (gender);";
        validateOk(inputText, "validate_default_ifNotExists_ok");
    }

    @Test
    public void validate_default_exists_fail(){
        String inputText = "CREATE DEFAULT INDEX users_gender_idx ON demo.users (gender);";
        validateFail(inputText, "validate_default_exists_fail");
    }

    @Test
    public void validate_notExists_tablename(){
        String inputText = "CREATE DEFAULT INDEX users_gender_idx ON demo.unknown (gender);";
        validateFail(inputText, "validate_notExists_tablename");
    }

    @Test
    public void validate_notExists_keyspace(){
        String inputText = "CREATE DEFAULT INDEX users_gender_idx ON unknown.users (gender);";
        validateFail(inputText, "validate_notExists_keyspace");
    }

    //
    // --- Lucene ---
    //

    @Test
    public void validate_lucene_noName_ok(){
        String inputText = "CREATE LUCENE INDEX ON demo.users (email);";
        validateOk(inputText, "validate_lucene_noName_ok");
    }

    @Test
    public void validate_lucene_named_ok() {
        String inputText = "CREATE LUCENE INDEX new_index ON demo.users (email);";
        validateOk(inputText, "validate_lucene_named_ok");
    }

    @Test
    public void validate_lucene_2columns_ok() {
        String inputText = "CREATE LUCENE INDEX new_index ON demo.users (email, name);";
        validateOk(inputText, "validate_lucene_2columns_ok");
    }

    @Test
    public void validate_lucene_stratioName_fail() {
        String inputText = "CREATE LUCENE INDEX stratio_new_index ON demo.users (email, name);";
        validateFail(inputText, "validate_lucene_stratioName_fail");
    }

    @Test
    public void validate_lucene_stratioColumn_fail() {
        String inputText = "CREATE LUCENE INDEX new_index ON demo.users (email, name, stratio_lucene_index_1);";
        validateFail(inputText, "validate_lucene_stratioColumn_fail");
    }

    @Test
    public void validate_lucene_withOptions_fail() {
        String inputText = "CREATE LUCENE INDEX new_index ON demo.users (email, bool, age)"
                + " WITH OPTIONS schema = '{refresh_seconds:1}';";
        validateFail(inputText, "validate_lucene_withOptions_fail");
    }

}
