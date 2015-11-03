package com.stratio.crossdata.testsAT.automated.dataProvider;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.collections.Lists;

import com.stratio.tests.utils.OperationsUtilsES;

public class DataProviderElasticSearchConnector {

    @DataProvider(parallel = false)
    public static Iterator<String[]> dataProvider(ITestContext context, Constructor<?> testConstructor)
            throws Exception {
        OperationsUtilsES op = new OperationsUtilsES();
        ArrayList<String> esTests = op.getFeatures();
        List<String[]> lData = Lists.newArrayList();
        for (String input : esTests) {
            lData.add(new String[] { input });
        }
        return lData.iterator();
    }

}
