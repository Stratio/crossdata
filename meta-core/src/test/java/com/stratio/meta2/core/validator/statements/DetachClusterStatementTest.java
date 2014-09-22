package com.stratio.meta2.core.validator.statements;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetadataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.DetachClusterStatement;
import com.stratio.meta2.core.validator.Validator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DetachClusterStatementTest {

    @Test
    public void detachCluster() {
        String query = "DROP CLUSTER myCluster";


        DetachClusterStatement detachClusterStatement=new DetachClusterStatement("myCluster");
        Validator validator=new Validator();

        BaseQuery baseQuery=new BaseQuery("CreateTableId",query, new CatalogName("demo"));

        ParsedQuery parsedQuery=new MetadataParsedQuery(baseQuery,detachClusterStatement);
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
