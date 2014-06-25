/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.engine;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.core.api.APIManager;
import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.parser.Parser;
import com.stratio.meta.core.planner.Planner;
import com.stratio.meta.core.validator.Validator;
import com.stratio.meta.streaming.StreamingUtils;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.SparkEnv;
import org.apache.spark.storage.BlockManagerMasterActor;
import org.apache.spark.storage.StorageStatus;

import java.util.Arrays;
import java.util.Map;

import scala.Tuple2;
import scala.collection.Iterator;

/**
 * Execution engine that creates all entities required for processing an executing a query:
 * {@link com.stratio.meta.core.parser.Parser}, {@link com.stratio.meta.core.validator.Validator},
 * {@link com.stratio.meta.core.planner.Planner}, and {@link com.stratio.meta.core.executor.Executor}.
 * Additionally, it also maintains the {@link com.datastax.driver.core.Session} with the Cassandra backend.
 */
public class Engine {

  /**
   * The {@link com.stratio.meta.core.parser.Parser} responsible for parse.
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

  /**
   * Datastax Java Driver session.
   */
  private final Session session;

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
   * @param config The {@link com.stratio.meta.core.engine.EngineConfig}.
   */
  public Engine(EngineConfig config) {

    this.deepContext = initializeDeep(config);

    this.session=initializeDB(config);

    IStratioStreamingAPI stratioStreamingAPI = initializeStreaming(config);

    parser = new Parser();
    validator = new Validator(session, stratioStreamingAPI, config);
    manager = new APIManager(session, stratioStreamingAPI);
    planner = new Planner(session, stratioStreamingAPI);
    executor = new Executor(session, stratioStreamingAPI, deepContext, config);
  }

  /**
   * Initialize the connection with Stratio Streaming.
   * @param config The {@link com.stratio.meta.core.engine.EngineConfig}.
   * @return An instance of {@link com.stratio.streaming.api.IStratioStreamingAPI}.
   */
  private IStratioStreamingAPI initializeStreaming(EngineConfig config){
    IStratioStreamingAPI stratioStreamingAPI = null;
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
    return stratioStreamingAPI;
  }

  /**
   * Initialize the connection to the underlying database.
   * @param config The {@link com.stratio.meta.core.engine.EngineConfig}.
   * @return A new Session.
   */
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

  /**
   * Initialize the DeepSparkContext adding the required jars if the deployment is not local.
   * @param config The {@link com.stratio.meta.core.engine.EngineConfig}
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

  public DeepSparkContext getDeepContext() {
    return deepContext;
  }

  /**
   * Get the parser.
   *
   * @return a {@link com.stratio.meta.core.parser.Parser}
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

  /**
   * Get the API manager.
   * @return A {@link com.stratio.meta.core.api.APIManager}.
   */
  public APIManager getAPIManager(){
    return manager;
  }

  /**
   * Close open connections.
   */
  public void shutdown(){
    deepContext.stop();
    session.close();
  }

}
