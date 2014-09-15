/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.core.engine;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

import com.hazelcast.config.Config;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.core.normalizer.Normalizer;
import com.stratio.meta2.core.api.APIManager;
import com.stratio.meta2.core.coordinator.Coordinator;
import com.stratio.meta2.core.executor.Executor;
import com.stratio.meta2.core.parser.Parser;
import com.stratio.meta2.core.planner.Planner;
import com.stratio.meta2.core.validator.Validator;

/**
 * Execution engine that creates all entities required for processing an executing a query:
 * {@link com.stratio.meta2.core.parser.Parser}, {@link com.stratio.meta.core.validator.Validator},
 * {@link com.stratio.meta.core.planner.Planner}, and {@link com.stratio.meta.core.executor.Executor}.
 */
public class Engine {

  /**
   * The {@link com.stratio.meta2.core.parser.Parser} responsible for parse.
   */
  private final Parser parser;

  /**
   * The {@link com.stratio.meta.core.validator.Validator} responsible for validation.
   */
  private final Validator validator;

  /**
   * The {@link com.stratio.meta.core.planner.Planner} responsible for planification.
   */
  private final Planner planner;

  /**
   * The {@link com.stratio.meta.core.executor.Executor} responsible for execution.
   */
  private final Executor executor;

  /**
   * The {@link com.stratio.meta.core.api.APIManager} responsible for API calls.
   */
  private final APIManager manager;


  private Normalizer normalizer;
  
  private final Coordinator coordinator;

  /**
   * Hazelcast instance.
   */
  //private final HazelcastInstance hazelcast;

  //private final Map<String, byte[]> hazelcastMap;

  /**
   * Deep Spark context.
   */
  private final DeepSparkContext deepContext;


  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Engine.class.getName());

  /**
   * Class constructor.
   *
   * @param config The {@link com.stratio.meta2.core.engine.EngineConfig}.
   */
  public Engine(EngineConfig config) {

    this.deepContext = initializeDeep(config);

    //this.session=initializeDB(config);

    //hazelcast = initializeHazelcast(config);
    //hazelcastMap = hazelcast.getMap(config.getHazelcastMapName());

    //IStratioStreamingAPI stratioStreamingAPI = initializeStreaming(config);

    parser = new Parser();
    //validator = new Validator(session, stratioStreamingAPI, config);
    validator = new Validator();
    //manager = new APIManager(session, stratioStreamingAPI);
    manager = new APIManager();
    //planner = new Planner(session, stratioStreamingAPI);
    planner = new Planner();
    //executor = new Executor(session, stratioStreamingAPI, deepContext, config);
    executor = new Executor(deepContext, null);
    coordinator = new Coordinator();
    setNormalizer(new Normalizer());
  }

  /**
   * Initialize the connection with Stratio Streaming.
   * @param config The {@link com.stratio.meta2.core.engine.EngineConfig}.
   * @return An instance of {@link com.stratio.streaming.api.IStratioStreamingAPI}.
   */
  /*private IStratioStreamingAPI initializeStreaming(EngineConfig config){
    IStratioStreamingAPI stratioStreamingAPI = null;
    if(config.getKafkaServer() != null && config.getZookeeperServer() != null
       && !"null".equals(config.getKafkaServer()) && !"null".equals(config.getZookeeperServer())) {
      try {
        stratioStreamingAPI = StratioStreamingAPIFactory.create().initializeWithServerConfig(
            config.getKafkaServer(),
            config.getKafkaPort(),
            config.getZookeeperServer(),
            config.getZookeeperPort());
      } catch (Exception e) {
        StringBuilder sb = new StringBuilder("Cannot connect with Stratio Streaming");
        sb.append(System.lineSeparator());
        sb.append("Zookeeper: ");
        sb.append(config.getZookeeperServer());
        sb.append(":");
        sb.append(config.getZookeeperPort());
        sb.append(", Kafka: ");
        sb.append(config.getKafkaServer());
        sb.append(":");
        sb.append(config.getKafkaPort());
        LOG.error(sb.toString(), e);
      }
    }else{
      LOG.warn("Skipping connection with Streaming Engine."
               + " Please configure zookeeper and kafka servers");
    }
    return stratioStreamingAPI;
  }*/

  /**
   * Initialize the connection to the underlying database.
   * @param config The {@link com.stratio.meta2.core.engine.EngineConfig}.
   * @return A new Session.
   */

	  /*
  private Session initializeDB(EngineConfig config){
    Cluster cluster = Cluster.builder()
        .addContactPoints(config.getCassandraHosts())
        .withPort(config.getCassandraPort()).build();

    LOG.info("Connecting to Cassandra on "
             + Arrays.toString(config.getCassandraHosts()) + ":" + config.getCassandraPort());
    Session result = null;

    try {
      result = cluster.connect();
    }catch(NoHostAvailableException nhae){
      LOG.error("Cannot connect to Cassandra", nhae);
    }

    return result;
  }
    */

  /**
   * Initialize the DeepSparkContext adding the required jars if the deployment is not local.
   * @param config The {@link com.stratio.meta2.core.engine.EngineConfig}
   * @return A new context.
   */
  private DeepSparkContext initializeDeep(EngineConfig config){
    //DeepSparkContext result = new DeepSparkContext(config.getSparkMaster(), config.getJobName());
    SparkConf sparkConf = new SparkConf().set("spark.driver.port",
                                              "0")//String.valueOf(StreamingUtils.findFreePort()))
                                         .set("spark.ui.port",
                                              "0");//String.valueOf(StreamingUtils.findFreePort()));
    DeepSparkContext result = new DeepSparkContext(new SparkContext(config.getSparkMaster(), config.getJobName(), sparkConf));

    if(!config.getSparkMaster().toLowerCase().startsWith("local")){
      for(String jar : config.getJars()){
        result.addJar(jar);
      }
    }

    return  result;
  }

  /**
   * Initializes the {@link com.hazelcast.core.HazelcastInstance} to be used using {@code config} .
   * @param config An {@link com.stratio.meta2.core.engine.EngineConfig}.
   * @return a new {@link com.hazelcast.core.HazelcastInstance}.
   */
  private HazelcastInstance initializeHazelcast(EngineConfig config) {

    MapConfig mapCfg = new MapConfig();
    mapCfg.setName(config.getHazelcastMapName());
    mapCfg.setBackupCount(config.getHazelcastMapBackups());
    mapCfg.getMaxSizeConfig().setSize(config.getHazelcastMapSize());
    mapCfg.getMaxSizeConfig().setMaxSizePolicy(MaxSizeConfig.MaxSizePolicy.PER_NODE);
    mapCfg.setInMemoryFormat(InMemoryFormat.OBJECT);

    Config cfg = new Config();
    cfg.getNetworkConfig().setPort(config.getHazelcastPort());
    cfg.getNetworkConfig().setPortAutoIncrement(false);
    cfg.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    cfg.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
    cfg.getNetworkConfig().getJoin().getTcpIpConfig()
        .setMembers(new ArrayList<String>(Arrays.asList(config.getHazelcastHosts())));
    cfg.addMapConfig(mapCfg);

    return Hazelcast.newHazelcastInstance(cfg);
  }

  public DeepSparkContext getDeepContext() {
    return deepContext;
  }

  /**
   * Get the parser.
   *
   * @return a {@link com.stratio.meta2.core.parser.Parser}
   */
  public Parser getParser() {
    return parser;
  }

  /**
   * Get the validator.
   *
   * @return a {@link com.stratio.meta.core.validator.Validator}
   */
  public Validator getValidator() {
    return validator;
  }

  /**
   * Get the planner.
   *
   * @return a {@link com.stratio.meta.core.planner.Planner}
   */
  public Planner getPlanner() {
    return planner;
  }

  /**
   * Get the executor.
   *
   * @return a {@link com.stratio.meta.core.executor.Executor}
   */
  public Executor getExecutor() {
    return executor;
  }

  public Coordinator getCoordinator() {
    return coordinator;
  }

  /**
   * Get the API manager.
   * @return A {@link com.stratio.meta2.core.api.APIManager}.
   */
  public APIManager getAPIManager(){
    return manager;
  }

  /*
  public Map<String, byte[]> getHazelcastMap() {
    return hazelcastMap;
  }
  */

  /**
   * Close open connections.
   */
  public void shutdown(){
    deepContext.stop();
    //session.close();
    //hazelcast.shutdown();
  }

public Normalizer getNormalizer() {
	return normalizer;
}

public void setNormalizer(Normalizer normalizer) {
	this.normalizer = normalizer;
}

}
