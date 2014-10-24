package com.stratio.crossdata.core.metadata;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;

public class MetadataManagerConstructorTest {

    @Test(expectedExceptions = MetadataManagerException.class)
    public void testShouldBeInitException(){
        DataStoreName name = new DataStoreName("dataStoreTest");
        String version = "0.1.0";
        Set<PropertyType> requiredProperties = new HashSet<>();
        Set<PropertyType> othersProperties = new HashSet<>();
        Set<String> behaviors = new HashSet<>();
        DataStoreMetadata dataStore = new DataStoreMetadata(name, version, requiredProperties, othersProperties, behaviors);
        MetadataManager.MANAGER.createDataStore(dataStore);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorException(){
        MetadataManager.MANAGER.init(null, null, null);
    }

}
