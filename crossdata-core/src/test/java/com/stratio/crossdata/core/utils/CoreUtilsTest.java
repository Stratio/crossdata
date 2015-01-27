package com.stratio.crossdata.core.utils;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.statements.structures.AsteriskSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.core.metadata.MetadataManagerTestHelper;

public class CoreUtilsTest extends MetadataManagerTestHelper {

    private TableName table;

    @BeforeClass
    public void setUp() throws ManifestException {
        //super.setUp();
        createTestDatastore();
        createTestCluster("clusterTest", new DataStoreName("dataStoreTest"));
        createTestCatalog("catalogTest");
        ClusterName clusterName = new ClusterName("clusterTest");
        String catalogName = "catalogTest";
        String tableName = "tableTest";
        String[] columnNames = new String[7];
        ColumnType[] columnTypes = new ColumnType[7];
        columnNames[0] = "BigIntColumn";
        columnTypes[0] = ColumnType.BIGINT;
        columnNames[1] = "BooleanColumn";
        columnTypes[1] = ColumnType.BOOLEAN;
        columnNames[2] = "DoubleColumn";
        columnTypes[2] = ColumnType.DOUBLE;
        columnNames[3] = "FloatColumn";
        columnTypes[3] = ColumnType.FLOAT;
        columnNames[4] = "IntColumn";
        columnTypes[4] = ColumnType.INT;
        columnNames[5] = "TextColumn";
        columnTypes[5] = ColumnType.TEXT;
        columnNames[6] = "ListColumn";
        columnTypes[6] = ColumnType.LIST;
        String[] partitionKeys = new String[0];
        String[] clusteringKeys = new String[0];
        createTestTable(clusterName, catalogName, tableName, columnNames, columnTypes,
                partitionKeys, clusteringKeys, null);
        table = new TableName("catalogTest", "tableTest");
    }

    @Test
    public void testConstructor() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        assertNotNull(coreUtils, "CoreUtils instance should be null");
    }

    @Test
    public void testConvertSelectorToLong() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new IntegerSelector(table, 25);
        ColumnName columnName = new ColumnName(table, "BigIntColumn");
        Object result = coreUtils.convertSelectorToObject(selector, columnName);
        assertTrue(result instanceof Long, "Result should be a Long. Found: " + result.getClass().getCanonicalName());
    }

    @Test
    public void testConvertSelectorToDouble() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new FloatingPointSelector(table, 25.7);
        ColumnName columnName = new ColumnName(table, "DoubleColumn");
        Object result = coreUtils.convertSelectorToObject(selector, columnName);
        assertTrue(result instanceof Double,
                "Result should be a Double. Found: " + result.getClass().getCanonicalName());
    }

    @Test
    public void testConvertSelectorToFloat() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new FloatingPointSelector(table, 25.7);
        ColumnName columnName = new ColumnName(table, "FloatColumn");
        Object result = coreUtils.convertSelectorToObject(selector, columnName);
        assertTrue(result instanceof Float,
                "Result should be a Float. Found: " + result.getClass().getCanonicalName());
    }

    @Test(expectedExceptions = PlanningException.class)
    public void testConvertSelectorToUnsupportedType() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new IntegerSelector(table, 25);
        ColumnName columnName = new ColumnName(table, "ListColumn");
        coreUtils.convertSelectorToObject(selector, columnName);
        fail();
    }

    @Test(expectedExceptions = PlanningException.class)
    public void testFailToFloat() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new StringSelector(table, "test");
        ColumnName columnName = new ColumnName(table, "FloatColumn");
        coreUtils.convertSelectorToObject(selector, columnName);
        fail();
    }

    @Test(expectedExceptions = PlanningException.class)
    public void testFailToDouble() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new StringSelector(table, "test");
        ColumnName columnName = new ColumnName(table, "DoubleColumn");
        coreUtils.convertSelectorToObject(selector, columnName);
        fail();
    }

    @Test(expectedExceptions = PlanningException.class)
    public void testFailToInteger() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new StringSelector(table, "test");
        ColumnName columnName = new ColumnName(table, "IntColumn");
        coreUtils.convertSelectorToObject(selector, columnName);
        fail();
    }

    @Test(expectedExceptions = PlanningException.class)
    public void testFailToLong() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new StringSelector(table, "test");
        ColumnName columnName = new ColumnName(table, "BigIntColumn");
        coreUtils.convertSelectorToObject(selector, columnName);
        fail();
    }

    @Test(expectedExceptions = PlanningException.class)
    public void testFailToString() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new AsteriskSelector(table);
        ColumnName columnName = new ColumnName(table, "TextColumn");
        coreUtils.convertSelectorToObject(selector, columnName);
        fail();
    }

    @Test(expectedExceptions = PlanningException.class)
    public void testFailToBoolean() throws Exception {
        CoreUtils coreUtils = CoreUtils.create();
        Selector selector = new StringSelector(table, "test");
        ColumnName columnName = new ColumnName(table, "BooleanColumn");
        coreUtils.convertSelectorToObject(selector, columnName);
        fail();
    }
}
