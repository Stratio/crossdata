package com.stratio.meta.core.engine;

import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;

import com.hazelcast.core.*;
import com.hazelcast.config.*;


public class EngineTests extends BasicCoreCassandraTest {
  private EngineConfig engineConfig;

  @BeforeClass
  public void setUp() {

    engineConfig = new EngineConfig();
    String[] cassandraHosts = {"127.0.0.1"};
    engineConfig.setCassandraHosts(cassandraHosts);
    engineConfig.setCassandraPort(9042);
    engineConfig.setSparkMaster("local");
    engineConfig.setClasspathJars("/");
    engineConfig.setJars(Arrays.asList("akka-1.0.jar", "deep-0.2.0.jar"));
    engineConfig.setHazelcastHosts(new String[]{"localhost"});
    engineConfig.setHazelcastPort(5900);
    engineConfig.setHazelcastMapName("schema");
    engineConfig.setHazelcastMapBackups(2);
    engineConfig.setHazelcastMapSize(1000);
  }

  @Test
  public void testCreateEngine() {
    Engine engine = new Engine(engineConfig);
    Assert.assertNotNull(engine);
    engine.shutdown();
  }

  @Test
  public void testHazelcastMap() {
    Engine engine = new Engine(engineConfig);
    Map<String, byte[]> map = engine.getHazelcastMap();
    Assert.assertNotNull(map);
    Assert.assertEquals(0, map.size());
    map.put("key_1", "value_1".getBytes());
    Assert.assertEquals(1, map.size());
    Assert.assertTrue(map.containsKey("key_1"));
    Assert.assertEquals(map.get("key_1").length, "value_1".getBytes().length);
    map.remove("key_1");
    Assert.assertEquals(0, map.size());
    Assert.assertFalse(map.containsKey("key_1"));
    engine.shutdown();
  }
}
