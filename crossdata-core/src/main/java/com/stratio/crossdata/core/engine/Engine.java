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

package com.stratio.crossdata.core.engine;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.data.FirstLevelName;
import com.stratio.crossdata.common.metadata.IMetadata;
import com.stratio.crossdata.core.api.APIManager;
import com.stratio.crossdata.core.connector.ConnectorManager;
import com.stratio.crossdata.core.coordinator.Coordinator;
import com.stratio.crossdata.core.execution.ExecutionManager;
import com.stratio.crossdata.core.grid.Grid;
import com.stratio.crossdata.core.grid.GridException;
import com.stratio.crossdata.core.grid.GridInitializer;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.normalizer.Normalizer;
import com.stratio.crossdata.core.parser.Parser;
import com.stratio.crossdata.core.planner.Planner;
import com.stratio.crossdata.core.validator.Validator;

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
     * The {@link com.stratio.crossdata.core.parser.Parser} responsible for parse.
     */
    private final Parser parser;
    /**
     * The {@link Validator} responsible for validation.
     */
    private final Validator validator;
    /**
     * The {@link Planner} responsible for planning.
     */
    private final Planner planner;
    /**
     * The {@link com.stratio.crossdata.core.api.APIManager} responsible for API calls.
     */
    private final APIManager manager;
    private final Coordinator coordinator;

    private final ConnectorManager connectorManager;

    private final Grid grid;
    private Normalizer normalizer;

    /**
     * Class constructor.
     *
     * @param config The {@link com.stratio.crossdata.core.engine.EngineConfig}.
     */
    public Engine(EngineConfig config) {

        try {
            this.grid = initializeGrid(config);
        } catch (Exception e) {
            LOG.error("Unable to start grid", e);
            throw new GridException("Unable to start grid: " + config, e);
        }

        // Initialize MetadataManager
        Map<FirstLevelName, IMetadata> metadataMap = grid.map("metadata");
        Lock lock = grid.lock("metadata");
        TransactionManager tm = grid.transactionManager("metadata");
        MetadataManager.MANAGER.init(metadataMap, lock, tm);

        // Initialize ExecutionManager
        Map<String, Serializable> executionMap = grid.map("executionData");
        Lock lockExecution = grid.lock("executionData");
        TransactionManager tmExecution = grid.transactionManager("executionData");
        ExecutionManager.MANAGER.init(executionMap, lockExecution, tmExecution);

        parser = new Parser();
        validator = new Validator();
        manager = new APIManager();
        planner = new Planner();
        coordinator = new Coordinator();
        setNormalizer(new Normalizer());
        connectorManager = new ConnectorManager();
    }

    /**
     * Initializes the {@link com.stratio.crossdata.core.grid.Grid} to be used using {@code config}.
     *
     * @param config An {@link com.stratio.crossdata.core.engine.EngineConfig}.
     * @return a new {@link com.stratio.crossdata.core.grid.Grid}.
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
     * @return a {@link com.stratio.crossdata.core.parser.Parser}
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
     * @return A {@link com.stratio.crossdata.core.api.APIManager}.
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
