/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.validator.statements;

import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class ExplainPlanStatementTest extends BasicValidatorTest {

    // Test with CORRECT statements
    @Test
    public void validateExplainPlanForSelect(){
        String inputText = "EXPLAIN PLAN FOR SELECT name FROM demo.users;";
        validateOk(inputText, "validateExplainPlanForSelect");
    }

    @Test
    public void validateExplainPlanForDropIndex(){
        String inputText = "EXPLAIN PLAN FOR DROP INDEX users_gender_idx;";
        validateOk(inputText, "validateExplainPlanForDropIndex");
    }

    // Test with WRONG statements
    @Test
    public void validateExplainPlanForWrongSelect(){
        String inputText = "EXPLAIN PLAN FOR SELECT name FROM demo.idk;";
        validateFail(inputText, "validateExplainPlanForWrongSelect");
    }

    @Test
    public void validateExplainPlanForWrongDropIndex(){
        String inputText = "EXPLAIN PLAN FOR DROP INDEX idk;";
        validateFail(inputText, "validateExplainPlanForWrongDropIndex");
    }
}