package com.stratio.crossdata.testsAT.automated.dataProvider;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.collections.Lists;
import org.w3c.dom.Document;

import com.stratio.tests.utils.OperationsUtils;
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
