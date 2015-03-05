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

package com.stratio.crossdata.common.data;

/**
 * Cluster name class.
 */
public class ClusterName extends FirstLevelName {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -5998186945261214393L;

    /**
     * Name of the cluster.
     */
    private final String name;

    /**
     * Constructor.
     * @param clusterName Cluster Name.
     */
    public ClusterName(String clusterName) {
        super();
        this.name = clusterName;
    }

    /**
     * Get the Cluster Name.
     * @return Cluster Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the qualified name for the cluster.
     * @return qualified name.
     */
    public String getQualifiedName() {
        return QualifiedNames.getClusterQualifiedName(getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameType getType() {
        return NameType.CLUSTER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ClusterName that = (ClusterName) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
