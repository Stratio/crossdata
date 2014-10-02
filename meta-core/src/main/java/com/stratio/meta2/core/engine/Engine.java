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

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;

import com.stratio.meta2.core.normalizer.Normalizer;
import com.stratio.meta2.common.data.FirstLevelName;
import com.stratio.meta2.core.api.APIManager;
import com.stratio.meta2.core.connector.ConnectorManager;
import com.stratio.meta2.core.coordinator.Coordinator;
import com.stratio.meta2.core.grid.Grid;
import com.stratio.meta2.core.grid.GridInitializer;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.parser.Parser;
import com.stratio.meta2.core.planner.Planner;
import com.stratio.meta2.core.validator.Validator;

/**
 * Execution engine that creates all entities required for processing an executing a query:
 * {@link Parser}, {@link Validator} and {@link Planner}
 */
public class Engine {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Engine.class.getName());
    /**
     * The {@link com.stratio.meta2.core.parser.Parser} responsible for parse.
     */
    private final Parser parser;
    /**
     * The {@link Validator} responsible for validation.
     */
    private final Validator validator;
    /**
     * The {@link Planner} responsible for planification.
     */
    private final Planner planner;
    /**
     * The {@link com.stratio.meta2.core.api.APIManager} responsible for API calls.
     */
    private final APIManager manager;
    private final Coordinator coordinator;

    private final ConnectorManager connectorManager;

    private final Grid grid;
    private Normalizer normalizer;

    /**
     * Class constructor.
     *
     * @param config The {@link com.stratio.meta2.core.engine.EngineConfig}.
     */
    public Engine(EngineConfig config) {

        try {
            this.grid = initializeGrid(config);
        } catch (Exception e) {
            LOG.error("Unable to start grid", e);
            throw new RuntimeException("Unable to start grid: " + config, e);
        }

        Map<FirstLevelName, Serializable> metadataMap = grid.map("meta");
        Lock lock = grid.lock("meta");
        TransactionManager tm = grid.transactionManager("meta");

        MetadataManager.MANAGER.init(metadataMap, lock, tm);

        parser = new Parser();
        validator = new Validator();
        manager = new APIManager();
        planner = new Planner();
        coordinator = new Coordinator();
        setNormalizer(new Normalizer());
        connectorManager = new ConnectorManager();
    }

    /**
     * Initializes the {@link com.stratio.meta2.core.grid.Grid} to be used using {@code config}.
     *
     * @param config An {@link com.stratio.meta2.core.engine.EngineConfig}.
     * @return a new {@link com.stratio.meta2.core.grid.Grid}.
     */
    private Grid initializeGrid(EngineConfig config) {
        GridInitializer gridInitializer = Grid.initializer();
        for (String host : config.getGridContactHosts()) {
            gridInitializer = gridInitializer.withContactPoint(host);
        }
        return gridInitializer.withPort(config.getGridPort())
                .withListenAddress(config.getGridListenAddress())
                .withMinInitialMembers(config.getGridMinInitialMembers())
                .withJoinTimeoutInMs(config.getGridJoinTimeout())
                .withPersistencePath(config.getGridPersistencePath()).init();
    }

    public Grid getGrid() {
        return grid;
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
     * @return a {@link Validator}
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * Get the planner.
     *
     * @return a {@link Planner}
     */
    public Planner getPlanner() {
        return planner;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public ConnectorManager getConnectorManager() {
        return connectorManager;
    }

    /**
     * Get the API manager.
     *
     * @return A {@link com.stratio.meta2.core.api.APIManager}.
     */
    public APIManager getAPIManager() {
        return manager;
    }

    /**
     * Close open connections.
     */
    public void shutdown() {
        grid.close();
    }

    public Normalizer getNormalizer() {
        return normalizer;
    }

    public void setNormalizer(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

}
