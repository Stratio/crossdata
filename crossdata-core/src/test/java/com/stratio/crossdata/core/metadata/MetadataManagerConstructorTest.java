package com.stratio.crossdata.core.metadata;

import static org.testng.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;

public class MetadataManagerConstructorTest {

    @BeforeTest
    public void testShouldBeInitException() {
        DataStoreName name = new DataStoreName("dataStoreTest");
        String version = "0.1.1";
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
