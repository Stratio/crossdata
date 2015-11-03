package com.stratio.crossdata.testsAT.automated.dataProvider;

import java.util.Iterator;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.collections.Lists;

public class DataProviderCommonTests {

    @DataProvider(parallel = false)
    public static Iterator<String[]> dataProvider(ITestContext context, Constructor<?> testConstructor)
            throws Exception {
        ArrayList<String> commonTest = new ArrayList<String>();

        // EXCEPTIONS TESTS
//        commonTest.add("/Exceptions/ParsingExceptions");
//        commonTest.add("/Exceptions/ConnectionException");
//        // DATASTORE
        commonTest.add("/Datastore/AddDatastore");
//        commonTest.add("/Attach/AttachCluster");
//        // // CONNECOTR
//        commonTest.add("/Connectors/AddConnector");
//        // // CATALOGS
//        commonTest.add("/Catalog/CreateCatalog");
        List<String[]> lData = Lists.newArrayList();
        for (String input : commonTest) {
            lData.add(new String[] { input });
        }
        if (lData.size() == 0) {
            lData.add(new String[] { "" });
        }
        return lData.iterator();
    }

}
