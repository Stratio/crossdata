/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.validator.statements;

import org.testng.annotations.Test;

import com.stratio.meta.core.validator.BasicValidatorTest;

public class SelectStatementTest extends BasicValidatorTest {

  @Test
  public void validateBasicColumnOk() {
    String inputText = "SELECT users.name FROM demo.users;";
    validateOk(inputText, "validateBasicColumnOk");
  }

  @Test
  public void validateBasicCountOk() {
    String inputText = "SELECT count(*) FROM demo.users;";
    validateOk(inputText, "validateBasicCountOk");
  }

  @Test
  public void validateBasicSeveralColumnsOk() {
    String inputText = "SELECT users.name, users.age FROM demo.users;";
    validateOk(inputText, "validateBasicSeveralColumnsOk");
  }

  @Test
  public void validateColumnUnknown() {
    String inputText = "SELECT users.name, users.unknown FROM demo.users;";
    validateFail(inputText, "validateColumnUnknown");
  }

  @Test
  public void validateBasicWhereOk() {
    String inputText = "SELECT users.name, users.age FROM demo.users WHERE users.name = 'name_5';";
    validateOk(inputText, "validateBasicWhereOk");
  }

  @Test
  public void validateWhere2columnsOk() {
    String inputText =
        "SELECT users.name, users.age FROM demo.users WHERE users.name = 'name_5' AND users.age = 15;";
    validateOk(inputText, "validateWhere2columnsOk");
  }

  @Test
  public void validateWhereColumnUnknown() {
    String inputText =
        "SELECT users.name, users.age FROM demo.users WHERE users.unknown = 'name_5' AND users.age = 15;";
    validateFail(inputText, "validateWhereColumnUnknown");
  }

  @Test
  public void validateWhereIntegerFail() {
    String inputText =
        "SELECT users.name, users.age FROM demo.users WHERE users.name = 'name_5' AND users.age = '15';";
    validateFail(inputText, "validateWhereIntegerFail");
  }

  @Test
  public void validateWhereStringFail() {
    String inputText =
        "SELECT users.name, users.age FROM demo.users WHERE users.name = 15 AND users.age = 15;";
    validateFail(inputText, "validateWhereStringFail");
  }

  @Test
  public void validateOperatorStringOk() {
    String[] operators = {">", "<", ">=", "<="};
    for (String operator : operators) {
      String inputText =
          "SELECT users.name, users.age FROM demo.users WHERE users.name " + operator
              + " 'name_5';";
      validateOk(inputText, "validateOperatorStringOk on column - operator: " + operator);
    }
  }

  @Test
  public void validateOperatorBooleanFail() {
    String[] operators = {">", "<", ">=", "<="};
    for (String operator : operators) {
      String inputText =
          "SELECT users.bool FROM demo.users WHERE users.bool " + operator + " true;";
      validateFail(inputText, "validateOperatorBooleanFail on column - operator: " + operator);
    }
  }

  @Test
  public void testValidateNotEqualOk() {

    String inputText =
        "SELECT users.name, users.age FROM demo.users WHERE users.email <> 'name_1@domain.com';";

    validateOk(inputText, "testValidateNotEqualOk");
  }

  @Test
  public void testValidateNotEqualFail() {

    String inputText = "SELECT users.name, users.age FROM demo.users WHERE users.email <> 6;";

    validateFail(inputText, "testValidateNotEqualFail");
  }

  //
  // Tests with table referred columns.
  //
  @Test
  public void validateReferredOk() {
    String inputText =
        "SELECT users.name, users.age FROM demo.users WHERE users.name = 'name_5' AND users.age = 15;";
    validateOk(inputText, "validateReferredOk");
  }

  @Test
  public void validateReferredFail() {
    String inputText =
        "SELECT unknown.name, unknown.age FROM demo.users WHERE users.name = 'name_5' AND users.age = 15;";
    validateFail(inputText, "validateReferredFail");
  }

  //
  // Tests with inner joins
  //
  @Test
  public void validateInnerJoinBasicOk() {
    String inputText =
        "SELECT users.name, users.age, users.email FROM demo.users "
            + "INNER JOIN demo.users ON users.name=users.name;";
    validateOk(inputText, "validateInnerJoinBasicOk");
  }

  @Test
  public void validateUnknownKs1Fail() {
    String inputText =
        "SELECT users.name, users.age, users.email FROM unknown.users "
            + "INNER JOIN demo.users ON users.name=users.name;";
    validateFail(inputText, "validateUnknownKs1Fail");
  }

  @Test
  public void validateUnknownKs2Fail() {
    String inputText =
        "SELECT users.name, users.age, users.email FROM demo.users "
            + "INNER JOIN unknown.users ON users.name=users.name;";
    validateFail(inputText, "validateUnknownKs2Fail");
  }

  @Test
  public void validateUnknownTable2Fail() {
    String inputText =
        "SELECT users.name, users.age, users.email FROM demo.users "
            + "INNER JOIN demo.unknown ON users.name=users.name;";
    validateFail(inputText, "validateUnknownTable2Fail");
  }

  @Test
  public void validateUnknownTable2WithoutKsFail() {
    String inputText =
        "SELECT users.name, users.age, users.email FROM demo.users "
            + "INNER JOIN unknown ON users.name=users.name;";
    validateFail(inputText, "validateUnknownTable2WithoutKsFail");
  }

  @Test
  public void validateOnUnknownKsFail() {
    String inputText =
        "SELECT users.name, users.age, users.email FROM demo.users "
            + "INNER JOIN demo.users ON unknown.name=users.name;";
    validateFail(inputText, "validateOnUnknownKsFail");
  }

  @Test
  public void validateOnUnknownKs2Fail() {
    String inputText =
        "SELECT users.name, users.age, users.email FROM demo.users "
            + "INNER JOIN demo.users ON users.name=unknown.name;";
    validateFail(inputText, "validateOnUnknownKs2Fail");
  }

  @Test
  public void validateOnUnknownTableFail() {
    String inputText =
        "SELECT users.name, users.age, users.email FROM demo.users "
            + "INNER JOIN demo.users ON demo.unknown=users.name;";
    validateFail(inputText, "validateOnUnknownKsFail");
  }

  @Test
  public void validateOnUnknownTable2Fail() {
    String inputText =
        "SELECT users.name, users.age, users.email FROM demo.users "
            + "INNER JOIN demo.users ON users.name=demo.unknown;";
    validateFail(inputText, "validateOnUnknownKs2Fail");
  }


  @Test
  public void validateInnerJoin2tablesOk() {
    String inputText =
        "SELECT users.name, users.age, users_info.info FROM demo.users "
            + "INNER JOIN demo.users_info ON users.name=users_info.link_name;";
    validateOk(inputText, "validateInnerJoin2tablesOk");
  }

  @Test
  public void validateInnerJoinWhereOk() {
    String inputText =
        "SELECT users.name, users.age, users_info.info FROM demo.users "
            + "INNER JOIN demo.users_info ON users.name=users_info.link_name "
            + "WHERE users.name = 'name_3';";
    validateOk(inputText, "validateInnerJoinWhereOk");
  }

  @Test
  public void testValidateInClauseWithMixedDataFail() {

    String inputText =
        "SELECT users.name FROM demo.users WHERE users.email IN ('name_11@domain.com', 19);";
    validateFail(inputText, "testValidateInClauseWithMixedDataFail");
  }

  @Test
  public void testValidateBasicInClauseWithStringsOk() {

    String inputText =
        "SELECT users.name FROM demo.users WHERE users.email IN ('name_11@domain.com', 'name_9@domain.com');";

    validateOk(inputText, "testValidateBasicInClauseWithStringsOk");
  }

  @Test
  public void testValidateBasicInClauseWithIntegersOk() {

    String inputText = "SELECT users.name FROM demo.users WHERE users.age IN (19, 31);";

    validateOk(inputText, "testValidateBasicInClauseWithIntegersOk");
  }

  @Test
  public void testValidateBasicBetweenClauseWithStringDataOk() {

    String inputText =
        "SELECT users.name FROM demo.users WHERE users.email BETWEEN 'aaaa_00@domain.com' AND 'zzzz_99@domain.com';";

    validateOk(inputText, "testValidateBasicBetweenClauseWithStringDataOk");

  }

  @Test
  public void testValidateBasicBetweenClauseWithoutResultsOk() {

    String inputText = "SELECT users.name FROM demo.users WHERE users.email BETWEEN 'a' AND 'b';";

    validateOk(inputText, "testValidateBasicBetweenClauseWithoutResultsOk");
  }

  @Test
  public void testValidateBasicBetweenClauseWithIntegerDataOk() {

    String inputText = "SELECT users.name FROM demo.users WHERE users.age BETWEEN 10 AND 25;";

    validateOk(inputText, "testValidateBasicBetweenClauseWithIntegerDataOk");
  }


  @Test
  public void testValidateBasicBetweenClauseWithMixedDataTypeFail() {

    String inputText = "SELECT users.name FROM demo.users WHERE users.age BETWEEN 'user_1' AND 25;";

    validateFail(inputText, "testValidateBasicBetweenClauseWithMixedDataTypeFail");
  }

  @Test
  public void testValidateGroupByClauseCountOk() {

    String inputText = "SELECT users.gender, COUNT(*) FROM demo.users GROUP BY users.gender;";

    validateOk(inputText, "testValidateGroupByClauseCountOk");
  }

  @Test
  public void testValidateGroupByClauseCountWithAliasOk() {

    String inputText = "SELECT users.gender AS g, COUNT(*) FROM demo.users GROUP BY g;";
    String expectedText =
        "SELECT users.gender AS g, COUNT(*) FROM demo.users GROUP BY users.gender;";
    validateOk(inputText, expectedText, "testValidateGroupByClauseCountWithAliasOk");
  }

  @Test
  public void testValidateGroupByClauseSumOk() {

    String inputText = "SELECT users.gender, SUM(users.age) FROM demo.users GROUP BY users.gender;";

    validateOk(inputText, "testValidateGroupByClauseSumOk");
  }

  @Test
  public void testValidateGroupMissingFieldFail() {

    String inputText = "SELECT SUM(users.age) FROM demo.users GROUP BY users.gender;";

    validateFail(inputText, "testValidateGroupByWrongSumClauseFail");
  }

  @Test
  public void testGroupByWithMissingSelectorFieldFail() {

    String inputText = "SELECT sum(users.age) FROM demo.users GROUP BY users.gender;";
    validateFail(inputText, "testGroupByWithMissingSelectorFieldFail");
  }

  @Test
  public void testNoGroupWithAggregationFunctionNoGroupByOk() {

    String inputText = "SELECT users.gender, sum(users.age) FROM demo.users;";
    validateOk(inputText, "testNoGroupWithAggregationFunctionNoGroupByOk");
  }

  @Test
  public void testValidateSimpleOrderByOk() {

    String inputText = "SELECT * FROM demo.users ORDER BY users.age;";

    validateOk(inputText, "testValidateSimpleOrderByOk");
  }

  @Test
  public void testValidateMultipleOrderByOk() {

    String inputText = "SELECT * FROM demo.users ORDER BY users.gender, users.age;";

    validateOk(inputText, "testValidateMultipleOrderByOk");
  }

  @Test
  public void testValidateSimpleOrderByWithTableOk() {

    String inputText = "SELECT * FROM demo.users ORDER BY users.age;";

    validateOk(inputText, "testValidateSimpleOrderByOk");
  }

  @Test
  public void testValidateSimpleOrderByUnknownFieldFail() {

    String inputText = "SELECT * FROM demo.users ORDER BY users.unknown;";

    validateFail(inputText, "testValidateSimpleOrderByUnknownFieldFail");
  }

  @Test
  public void testValidateMultipleOrderByUnknownFieldFail() {

    String inputText = "SELECT * FROM demo.users ORDER BY users.gender, users.unknown;";

    validateFail(inputText, "testValidateSimpleOrderByUnknownFieldFail");
  }

  @Test
  public void testComplexQueryWithAliasesOk() {

    String inputText =
        "SELECT users.age AS edad, users.gender AS genero, sum(users.age) AS suma, min(gender) AS minimo, count(*) AS contador FROM demo.users "
            + "WHERE edad > 13 AND genero IN ('male', 'female') ORDER BY edad DESC GROUP BY genero;";

    String expectedText =
        "SELECT users.age AS edad, users.gender AS genero, sum(users.age) AS suma, min(users.gender) AS minimo, count(*) AS contador FROM demo.users "
            + "WHERE users.age > 13 AND users.gender IN ('male', 'female') ORDER BY users.age DESC GROUP BY users.gender;";

    validateOk(inputText, expectedText, "testComplexQueryWithAliasesOk");
  }
}
