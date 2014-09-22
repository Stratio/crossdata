package com.stratio.meta2.core.validator.statements;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetaDataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.AlterClusterStatement;
import com.stratio.meta2.core.validator.Validator;
import org.testng.Assert;
import org.testng.annotations.Test;


public class AlterClusterStatementTest {
    @Test
    public void alterCluster() {
        String query = "ALTER CLUSTER IF EXISTS myCluster WITH OPTIONS {'comment':'my coments'}";


        AlterClusterStatement alterClusterStatement=new AlterClusterStatement("myCluster",true, "{'comment':'my coments'}");
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("alterClusterId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetaDataParsedQuery(baseQuery,alterClusterStatement);
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
    public void alterNonExistCluster() {
        String query = "ALTER CLUSTER IF EXISTS unknown WITH OPTIONS {'comment':'my coments'}";


        AlterClusterStatement alterClusterStatement=new AlterClusterStatement("unknown",true, "{'comment':'my coments'}");
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("alterClusterId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetaDataParsedQuery(baseQuery,alterClusterStatement);
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
    public void alterClusterEmptyOptions() {
        String query = "ALTER CLUSTER unknown WITH OPTIONS {}";


        AlterClusterStatement alterClusterStatement=new AlterClusterStatement("unknown",false, "{}");
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("alterClusterId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetaDataParsedQuery(baseQuery,alterClusterStatement);
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
