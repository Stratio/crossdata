package com.stratio.crossdata.common.ask;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import scala.collection.Iterator;

public class QueryTest {

    private Query query;
    private String queryId;
    private String sessionId;
    private String catalog;
    private String statement;
    private String user;

    @BeforeClass
    public void setUp() throws Exception {
        queryId = String.valueOf(UUID.randomUUID());
        sessionId = String.valueOf(UUID.randomUUID());
        catalog = "catalogTest";
        statement = "SELECT * FROM tableTest;";
        user = "tester";
        query = new Query(queryId, catalog, statement, user,sessionId);
    }

    @Test
    public void testQueryId() throws Exception {
        assertTrue(query.queryId().equalsIgnoreCase(queryId), "'" + query.queryId() + "' should be equals to " +
                System.lineSeparator() + "'" + queryId + "'");
    }

    @Test
    public void testStatement() throws Exception {
        assertTrue(query.statement().equalsIgnoreCase(statement), "'" + query.statement() + "' should be equals to " +
                System.lineSeparator() + "'" + statement + "'");
    }

    @Test
    public void testUser() throws Exception {
        assertTrue(query.user().equalsIgnoreCase(user), "'" + query.user() + "' should be equals to " +
                System.lineSeparator() + "'" + user + "'");
    }

    @Test
    public void testCatalog() throws Exception {
        assertTrue(query.catalog().equalsIgnoreCase(catalog), "'" + query.catalog() + "' should be equals to " +
                System.lineSeparator() + "'" + catalog + "'");
    }

    @Test
    public void testProductIterator() throws Exception {
        Iterator<Object> result = query.productIterator();
        assertNotNull(result, result + " shouldn't be null");
    }

    @Test
    public void testProductPrefix() throws Exception {
        String result = query.productPrefix();
        assertNotNull(result, result + " shouldn't be null");
    }

    @Test
    public void testCurried() throws Exception {
        Object result = query.curried();
        assertNotNull(result, result + " shouldn't be null");
    }

    @Test
    public void testTupled() throws Exception {
        Object result = query.tupled();
        assertNotNull(result, result + " shouldn't be null");
    }

}
