package com.stratio.meta2.core.validator.statements;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetadataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.AttachConnectorStatement;
import com.stratio.meta2.core.validator.Validator;

public class AttachConnectorStatementTest {

    @Test
    public void attachExistingConnector() {
        String query = "ATTACH Connector CassandraConnector TO Cassandra WITH OPTIONS {'comment':'a comment'}";

        AttachConnectorStatement attachConnectorStatement = new AttachConnectorStatement("CassandraConnector",
                "Cassandra", "{'comment':'a comment'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachConnectorID", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("The Connector must not exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void attachConnectorUnknown() {
        String query = "ATTACH Connector unknown TO myCluster WITH OPTIONS {'comment':'a comment'}";

        AttachConnectorStatement attachConnectorStatement = new AttachConnectorStatement("unknown", "myCluster",
                "{'comment':'a comment'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachConnectorID", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void attachConnectorUnknownCluster() {
        String query = "ATTACH Connector newConnector TO unknown WITH OPTIONS {'comment':'a comment'}";

        AttachConnectorStatement attachConnectorStatement = new AttachConnectorStatement("CassandraConnector",
                "unknown", "{'comment':'a comment'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachConnectorID", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void attachConnectorEmptyOptions() {
        String query = "ATTACH Connector CassandraConnector TO Cassandra WITH OPTIONS";

        AttachConnectorStatement attachConnectorStatement = new AttachConnectorStatement("CassandraConnector",
                "Cassandra", "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachConnectorID", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("The options cannot be empty");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

}
