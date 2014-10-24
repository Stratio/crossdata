package com.stratio.crossdata.core.metadata;

import org.testng.annotations.Test;

public class MetadataManagerConstructorTest {

    /*
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
    */

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorException(){
        MetadataManager.MANAGER.init(null, null, null);
    }

}
