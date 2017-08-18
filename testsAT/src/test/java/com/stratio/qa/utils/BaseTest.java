package com.stratio.qa.utils;

import java.lang.reflect.Method;

import com.stratio.qa.utils.BaseGTest;
import com.stratio.qa.utils.ThreadProperty;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

abstract public class BaseTest extends BaseGTest {

    protected String browser = "";

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext context) {
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        ThreadProperty.set("browser", this.browser);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method) {
    }

    @AfterClass()
    public void afterClass() {
    }
}