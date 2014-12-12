package com.stratio.crossdata.core.grammar.statements;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.core.grammar.ParsingTest;

public class WrongStatements extends ParsingTest {

    @Test
    public void unknownFirstWordOfStatement() {
        boolean test=true;
        String inputText = "WINDOWS GO HOME;";
        testParserFails(inputText, "unknown_first_word_of_statement");
        Assert.assertTrue(test);
    }

}
