package com.stratio.crossdata.core.metadata;

import static org.testng.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.core.MetadataManagerTestHelper;

public class MetadataManagerConstructorTest {

    @BeforeClass
    public void setUp() throws ManifestException {
        MetadataManagerTestHelper.HELPER.initHelper();
        MetadataManagerTestHelper.HELPER.createTestEnvironment();
    }

    @AfterClass
    public void tearDown() throws Exception {
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    @Test
    public void testShouldBeInitException() {
        DataStoreName name = new DataStoreName("dataStoreTest");
        String version = "0.2.0";
        Set<PropertyType> requiredProperties = new HashSet<>();
        Set<PropertyType> othersProperties = new HashSet<>();
        Set<String> behaviors = new HashSet<>();
        DataStoreMetadata dataStore = new DataStoreMetadata(name, version, requiredProperties, othersProperties,
                behaviors, null);
        try {
            MetadataManager.MANAGER.createDataStore(dataStore);
        } catch (MetadataManagerException me) {
            return;
        }
        fail();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorException() {
        MetadataManager.MANAGER.init(null, null, null);
        fail();
    }

}
