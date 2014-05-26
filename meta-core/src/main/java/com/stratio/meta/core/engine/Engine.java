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
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.parser.Parser;
import com.stratio.meta.core.planner.Planner;
import com.stratio.meta.core.validator.Validator;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * Execution engine that creates all entities required for processing an executing a query:
 * {@link com.stratio.meta.core.parser.Parser}, {@link com.stratio.meta.core.validator.Validator},
 * {@link com.stratio.meta.core.planner.Planner}, and {@link com.stratio.meta.core.executor.Executor}.
 * Additionally, it also maintains the {@link com.datastax.driver.core.Session} with the Cassandra backend.
 */
public class Engine {

    /**
     * The {@link com.stratio.meta.core.parser.Parser}. Responsible for parse.
     */
    private final Parser parser;

    /**
     * The {@link com.stratio.meta.core.validator.Validator}. Responsible for validation.
     */
    private final Validator validator;

    /**
     * The {@link com.stratio.meta.core.planner.Planner}. Responsible for planification.
     */
    private final Planner planner;

    /**
     * The {@link com.stratio.meta.core.executor.Executor}. Responsible for execution.
     */
    private final Executor executor;

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

        Cluster cluster = Cluster.builder()
                .addContactPoints(config.getCassandraHosts())
                .withPort(config.getCassandraPort()).build();

        LOG.info("Connecting to Cassandra on "
                + Arrays.toString(config.getCassandraHosts()) + ":" + config.getCassandraPort());
        this.session=cluster.connect();

        this.deepContext = new DeepSparkContext(config.getSparkMaster(), config.getJobName());

        if(!config.getSparkMaster().toLowerCase().startsWith("local")){
            for(String jar : config.getJars()){
                deepContext.addJar(jar);
            }
        }

        parser = new Parser();
        validator = new Validator(session);
        planner = new Planner(session);
        executor = new Executor(session, deepContext, config);
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
     * Close open connections.
     */
    public void shutdown(){
        deepContext.stop();
        session.close();
    }

}
