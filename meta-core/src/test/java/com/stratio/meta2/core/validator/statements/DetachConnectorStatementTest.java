package com.stratio.meta2.core.validator.statements;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetadataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.DetachConnectorStatement;
import com.stratio.meta2.core.validator.BasicValidatorTest;
import com.stratio.meta2.core.validator.Validator;

public class DetachConnectorStatementTest extends BasicValidatorTest{

    @Test
    public void detachConnector() {
        String query = "DETACH CONNECTOR CassandraConnector FROM cluster";
        DetachConnectorStatement detachConnectorStatement = new DetachConnectorStatement(
                new ConnectorName("CassandraConnector"), new ClusterName("cluster"));
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("detachConnectorId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, detachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertFalse(false);
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void detachUnknownConnector() {
        String query = "DETACH CONNECTOR Unknown FROM cluster";
        DetachConnectorStatement detachConnectorStatement = new DetachConnectorStatement(new ConnectorName("Unknown"),
                new ClusterName("cluster"));
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("detachConnectorId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, detachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("Connector must exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void detachConnectorUnknownCluster() {
        String query = "DETACH CONNECTOR CassandraConnector FROM Unknown";

        DetachConnectorStatement detachConnectorStatement = new DetachConnectorStatement(
                new ConnectorName("CassandraConnector"), new ClusterName("Unknown"));
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("detachConnectorId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, detachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("Datastore must exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }
}
