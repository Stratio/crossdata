package com.stratio.crossdata.core.structures;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.statements.structures.GroupByFunction;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.SelectorFunction;
import com.stratio.crossdata.common.statements.structures.SelectorGroupBy;
import com.stratio.crossdata.common.statements.structures.SelectorIdentifier;
import com.stratio.crossdata.common.statements.structures.SelectorMeta;
import com.stratio.crossdata.common.statements.structures.StringSelector;

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


    @Test
    public void testSelectionArsteriskList() throws Exception {

        SelectionList selectionList=new SelectionList(false, new SelectionAsterisk());
        selectionList.addTablename(new TableName("catalogTest","tableTest"));

        assertFalse(selectionList.containsFunctions());

        assertTrue(selectionList.getIds().isEmpty());

        assertTrue(selectionList.getSelectorsGroupBy().isEmpty());
    }

    @Test
    public void testSelectionSelectorsList() throws Exception {
        List<SelectionSelector> selectors= new ArrayList<>();

        SelectorMeta selectorIdentifier=new SelectorIdentifier("test");
        SelectionSelector selectionSelector=new SelectionSelector(selectorIdentifier,false,"");

        List<SelectorMeta> param= new ArrayList<>();
        param.add(selectorIdentifier);
        SelectorMeta selectorFunction=new SelectorFunction(new TableName("catalogTest","tableTest"),param);
        SelectionSelector selectionSelectorFunction=new SelectionSelector(selectorFunction,false,"");
        SelectorMeta selectorGroupby=new SelectorGroupBy(GroupByFunction.AVG,selectorIdentifier);
        SelectionSelector selectionSelectorGrupby=new SelectionSelector(selectorGroupby,false,"");

        selectors.add(selectionSelector);
        selectors.add(selectionSelectorFunction);
        selectors.add(selectionSelectorGrupby);

        SelectionSelectors selectionSelectors=new SelectionSelectors(selectors);

        SelectionList selectionList=new SelectionList(false, selectionSelectors);
        selectionList.addTablename(new TableName("catalogTest","tableTest"));

        assertTrue(selectionList.containsFunctions());

        assertFalse(selectionList.getIds().isEmpty());

        assertFalse(selectionList.getSelectorsGroupBy().isEmpty());
    }


}
