package com.stratio.crossdata.core.structures;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.TableName;

public class SelectionClauseTest {

    SelectionClause selectionClause;

    @BeforeMethod
    public void setUp() throws Exception {
        selectionClause = new SelectionCount();
    }

    @Test
    public void testGetType() throws Exception {
        assertTrue(selectionClause.getType() == SelectionClause.TYPE_COUNT,
                "'" + selectionClause.getType() + "' should be equals to " + System.lineSeparator() + "'" + SelectionClause.TYPE_COUNT + "'");
    }

    @Test
    public void testGetSelectorsGroupBy() throws Exception {
        assertTrue(selectionClause.getSelectorsGroupBy().isEmpty(),
                "SelectorsGroupBy should be empty");
    }

    @Test
    public void testAddTablename() throws Exception {
        selectionClause.addTablename(new TableName("catalogTest", "tableTest"));
        assertNotNull(selectionClause);
    }

    @Test
    public void testGetIds() throws Exception {
        assertTrue(selectionClause.getIds().isEmpty(),
                "Ids should be empty");
    }

    @Test
    public void testToString() throws Exception {
        String selectionClauseStr = "COUNT(*)";
        assertTrue(selectionClause.toString().equalsIgnoreCase(selectionClauseStr),
                "'" + selectionClause.toString() + "' should be equals to " + System.lineSeparator() + "'" + selectionClauseStr + "'");
    }

    @Test
    public void testGetFields() throws Exception {
        assertTrue(selectionClause.getFields().isEmpty(),
                "Fields should be empty");
    }

    @Test
    public void testContainsFunctions() throws Exception {
        assertFalse(selectionClause.containsFunctions(),
                selectionClause.toString() + " shouldn't contain functions");
    }
}
