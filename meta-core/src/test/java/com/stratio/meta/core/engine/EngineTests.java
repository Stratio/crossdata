package com.stratio.meta.core.engine;

import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import junit.framework.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;

public class EngineTests extends BasicCoreCassandraTest{
    private EngineConfig engineConfig;

    @BeforeClass
    public void setUp(){
        engineConfig = new EngineConfig();
        String [] cassandraHosts = {"127.0.0.1"};
        engineConfig.setCassandraHosts(cassandraHosts);
        engineConfig.setCassandraPort(9042);
        engineConfig.setSparkMaster("local");
        engineConfig.setClasspathJars("/");
        engineConfig.setJars(Arrays.asList("akka-1.0.jar", "deep-0.2.0.jar"));
    }

    @Test
    public void testCreateEngine(){
        Engine engine = new Engine(engineConfig);
        Assert.assertNotNull(engine);
        engine.shutdown();
    }
}
