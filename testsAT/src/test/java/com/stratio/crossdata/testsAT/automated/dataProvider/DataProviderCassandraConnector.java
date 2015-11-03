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

public class DataProviderCassandraConnector {

    @DataProvider(parallel = false)
    public static Iterator<String[]> dataProvider(ITestContext context, Constructor<?> testConstructor)
            throws Exception {
        URL url = DataProviderCassandraConnector.class.getResource("/manifest/CassandraConnector.xml");
        File file = new File(url.getPath());
        Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        d.getDocumentElement().normalize();
        OperationsUtils op = new OperationsUtils();
        ArrayList<String> cassandraTests = op.getFeatures(d.getElementsByTagName("operation"));
        ArrayList<String> cassandraFunctionsNoNative = op.getFeatures(d.getElementsByTagName("FunctionName"));
           List<String[]> lData = Lists.newArrayList();
        for (String input : cassandraTests) {
            lData.add(new String[] { input });
        }
        for (String input : cassandraFunctionsNoNative) {
            lData.add(new String[] { input });
        }
        if (lData.size() == 0) {
            lData.add(new String[] { "" });
        }
        return lData.iterator();
    }

}
