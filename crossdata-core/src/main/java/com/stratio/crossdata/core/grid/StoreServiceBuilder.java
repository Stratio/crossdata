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

import org.jgroups.JChannel;

/**
 * Builder for creating a new {@link com.stratio.crossdata.core.grid.StoreService}.
 */
public class StoreServiceBuilder {

    private JChannel channel;
    private String name;
    private String path;

    /**
     * Returns a {@link StoreServiceBuilder} using the specified JGroups channel.
     *
     * @param channel the JGroups channel to be used
     * @return a {@link StoreServiceBuilder} using the specified JGroups channel
     */
    public StoreServiceBuilder setChannel(JChannel channel) {
        this.channel = channel;
        return this;
    }

    /**
     * Returns a {@link StoreServiceBuilder} using the specified cluster name.
     *
     * @param name the cluster name
     * @return a {@link StoreServiceBuilder} using the specified cluster name.
     */
    public StoreServiceBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns a {@link StoreServiceBuilder} using the specified files persistence path.
     *
     * @param path the files persistence path
     * @return a {@link StoreServiceBuilder} using the specified files persistence path
     */
    public StoreServiceBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * Returns the new {@link StoreService} defined by this.
     *
     * @return a new {@link StoreService} defined by this
     */
    public StoreService build() {
        return new StoreService(channel, name, path);
    }

}
