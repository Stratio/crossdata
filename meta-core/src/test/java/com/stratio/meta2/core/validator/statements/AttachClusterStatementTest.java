package com.stratio.meta2.core.validator.statements;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta2.common.data.*;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetadataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.AttachClusterStatement;
import com.stratio.meta2.core.validator.Validator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AttachClusterStatementTest {

    @Test
    public void attachClusterNoOptions() {
        String query = "ATTACH CLUSTER myCluster on DATASTORE Cassandra";


        AttachClusterStatement attachClusterStatement=new AttachClusterStatement("myCluster",true, "Cassandra", "");
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("CreateTableId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetadataParsedQuery(baseQuery,attachClusterStatement);

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
    public void attachClusterUnknown() {
        String query = "ATTACH CLUSTER unknown on DATASTORE cassadra";


        AttachClusterStatement attachClusterStatement=new AttachClusterStatement("unknownr",true, "cassandra", "");
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("CreateTableId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetadataParsedQuery(baseQuery,attachClusterStatement);
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
    public void attachClusterUnknownDatastore() {
        String query = "ATTACH CLUSTER myCluster on DATASTORE unknown";


        AttachClusterStatement attachClusterStatement=new AttachClusterStatement("myCluster",true, "unknown", "");
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("CreateTableId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetadataParsedQuery(baseQuery,attachClusterStatement);
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
    public void attachClusterWithOptions() {
        String query = "ATTACH CLUSTER myCluster on DATASTORE unknown with options {'comment':'attach cluster'}";


        AttachClusterStatement attachClusterStatement=new AttachClusterStatement("myCluster",true, "unknown", "{'comment':'attach cluster'}");
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("CreateTableId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetadataParsedQuery(baseQuery,attachClusterStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertFalse(false);
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }


}
