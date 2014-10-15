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

package com.stratio.meta2.core.grid;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

import org.jgroups.JChannel;

/**
 * Class providing several in-memory data grid artifacts, as distributed data stores, locks and
 * broadcast channels.
 */
public class Grid implements Closeable {

    private static final String FORK_CHANNEL_PREFIX = "fork-";

    private final ChannelService channelService;
    private final LockService lockService;
    private final StoreService storeService;

    /**
     * Builds a new {@link Grid}.
     *
     * @param channelService the distributed channeling service
     * @param lockService    the distributed locking service
     * @param storeService   the distributed storing service
     */
    private Grid(ChannelService channelService, LockService lockService, StoreService storeService) {
        this.channelService = channelService;
        this.lockService = lockService;
        this.storeService = storeService;
    }

    /**
     * Returns a new {@link GridInitializer} for building this.
     *
     * @return a new {@link GridInitializer} for building this.
     */
    public static GridInitializer initializer() {
        return new GridInitializer();
    }

    public static Grid getInstance() {
        return Singleton.INSTANCE.grid;
    }

    /**
     * Initializes the singleton instance.
     *
     * @param channelService the distributed channeling service
     * @param lockService    the distributed locking service
     * @param storeService   the distributed storing service
     * @return the singleton instance
     */
    static Grid init(ChannelService channelService,
            LockService lockService,
            StoreService storeService) {
        if (Singleton.INSTANCE.grid != null) {
            Singleton.INSTANCE.grid.close();
        }
        Singleton.INSTANCE.grid = new Grid(channelService, lockService, storeService);
        return Singleton.INSTANCE.grid;
    }

    /**
     * Returns a distributed {@link java.util.Map} associated to the specified name. It returns the
     * existent instance (if any).
     *
     * @param name the name of the {@link java.util.Map}
     * @param <K>  the class of the map's keys
     * @param <V>  the class of the map's values
     * @return a distributed {@link java.util.Map} associated to the specified name
     */
    public <K, V> Map<K, V> map(String name) {
        return storeService.map(name);
    }

    /**
     * Returns a {@link javax.transaction.TransactionManager} for the {@link java.util.Map} associated
     * to the specified name.
     *
     * @param name the name of the {@link java.util.Map}
     * @return a {@link javax.transaction.TransactionManager}
     */
    public TransactionManager transactionManager(String name) {
        return storeService.transactionManager(name);
    }

    /**
     * Returns a distributed {@link java.util.concurrent.locks.Lock} with the specified name. It
     * returns any existent instance or, otherwise, a new one.
     *
     * @param name the {@link java.util.concurrent.locks.Lock}'s name
     * @return a distributed {@link java.util.concurrent.locks.Lock} with the specified name
     */
    public Lock lock(String name) {
        return lockService.build(name);
    }

    /**
     * Returns a distributed {@link org.jgroups.Channel} with the specified name. It returns any
     * existent instance or, otherwise, a new one.
     *
     * @param name the {@link org.jgroups.Channel}'s name
     * @return a distributed {@link org.jgroups.Channel} with the specified name
     */
    public JChannel channel(String name) {
        return channelService.build(FORK_CHANNEL_PREFIX + name);
    }

    /**
     * Closes this and all its created distributed objects.
     */
    @Override
    public void close() {
        if (Singleton.INSTANCE.grid != null) {
            storeService.close();
            lockService.close();
            channelService.close();
            Singleton.INSTANCE.grid = null;
        }
    }

    /**
     * Singleton instance.
     */
    private enum Singleton {
        INSTANCE;
        private Grid grid;
    }

}
