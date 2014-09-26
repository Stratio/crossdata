package com.stratio.meta2.core.validator.statements;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetadataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.AttachClusterStatement;
import com.stratio.meta2.core.validator.Validator;

public class AttachClusterStatementTest {

    @Test
    public void attachClusterNoOptions() {
        String query = "ATTACH CLUSTER myCluster on DATASTORE Cassandra";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("myCluster"), true,
                new DataStoreName("Cassandra"), "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);

        try {
            validator.validate(parsedQuery);
            Assert.fail("Options are required for ATTACH CLUSTER statement");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void attachClusterUnknownDatastore() {
        String query = "ATTACH CLUSTER myCluster on DATASTORE unknown";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("myCluster"), true,
                new DataStoreName("unknown"), "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("Datastore must exists before ATTACH CLUSTER statement");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void attachClusterWithOptions() {
        String query = "ATTACH CLUSTER myCluster on DATASTORE Cassandra with options {'comment':'attach cluster'}";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("myCluster"), true,
                new DataStoreName("Cassandra"),
                "{'comment':'attach cluster'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }
    }

}
