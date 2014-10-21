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

package com.stratio.crossdata.core.grid;

import java.io.Closeable;
import java.util.concurrent.locks.Lock;

import org.jgroups.JChannel;

/**
 * A distributed {@link java.util.concurrent.locks.Lock} factory/manager.
 * <p/>
 * The created {@link java.util.concurrent.locks.Lock}s are based in a JGroups channel.
 * <p/>
 * It must be closed ({@link #close()}) when its created {@link com.stratio.crossdata.core.grid.StoreService}s are not needed anymore.
 */
public class LockService implements Closeable {

    private final JChannel channel;
    private final org.jgroups.blocks.locking.LockService lockService;

    /**
     * Builds a ne {@link com.stratio.crossdata.core.grid.LockService} based on the specified JGroups
     * channel.
     *
     * @param channel the JGroups channel to be used.
     */
    public LockService(JChannel channel) {
        this.channel = channel;
        try {
            channel.connect("lock");
        } catch (Exception e) {
            throw new GridException(e);
        }
        lockService = new org.jgroups.blocks.locking.LockService(channel);
    }

    /**
     * Returns a new distributed {@link java.util.concurrent.locks.Lock} with the specified identifying name.
     *
     * @param name the  {@link java.util.concurrent.locks.Lock}'s identifying name.
     * @return a new distributed {@link java.util.concurrent.locks.Lock}.
     */
    public Lock build(String name) {
        return lockService.getLock(name);
    }

    /**
     * Closes this service and all its created {@link java.util.concurrent.locks.Lock}s.
     */
    @Override
    public void close() {
        lockService.unlockAll();
    }

}
