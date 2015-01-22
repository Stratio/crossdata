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

package com.stratio.crossdata.common.metadata;

import com.stratio.crossdata.common.data.Status;
import com.stratio.crossdata.common.data.NodeName;

/**
 * NodeMetadata class.
 */
public class NodeMetadata implements IMetadata {

    private final NodeName name;
    private Status status;

    /**
     * Constructor Class.
     * @param name The node name.
     * @param status The status.
     */
    public NodeMetadata(NodeName name, Status status) {
        this.name = name;
        this.status = status;
    }

    /**
     * Constructor class.
     * @param name The Node name.
     */
    public NodeMetadata(NodeName name) {
        this(name, Status.OFFLINE);
    }

    /**
     * Get the node name.
     * @return A {@link com.stratio.crossdata.common.data.NodeName} .
     */
    public NodeName getName() {
        return name;
    }

    /**
     * Get the status of the node.
     * @return A {@link com.stratio.crossdata.common.data.Status} .
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set the status to the node.
     * @param status The {@link com.stratio.crossdata.common.data.Status} .
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
