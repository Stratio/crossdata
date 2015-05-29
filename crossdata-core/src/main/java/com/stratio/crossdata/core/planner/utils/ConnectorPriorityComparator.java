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

package com.stratio.crossdata.core.planner.utils;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;

import java.util.Comparator;
import java.util.List;


/**
 * This class provides a comparator to compare one connector with another based on the clusters affected by the query.
 * In order to carry out this work, the priority assigned to each pair connector-cluster is used.
 * Soon, this will use some metrics and information about whether the connector is native or not.
 */
public class ConnectorPriorityComparator implements Comparator<ConnectorMetadata> {

    private final List<ClusterName> clusterNames;

    /**
     * Connector comparator constructor.
     * @param clusterNames Clusters where the connector must retrieve data.
     */
    public ConnectorPriorityComparator(List<ClusterName> clusterNames) {
        this.clusterNames = clusterNames;
    }



    /**
     * Overrides the compare method.
     * @param leftCM   The first connector metadata.
     * @param rightCM  The second connector metadata.
     * @returns -1, 0, 1 if the first connector is, preferable respectively to the second one, equal or is not preferable .
     */
    @Override
    public int compare(ConnectorMetadata leftCM, ConnectorMetadata rightCM) {
        int leftPriority = leftCM.getPriorityFromClusterNames(clusterNames);
        int rightPriority = rightCM.getPriorityFromClusterNames(clusterNames);

        if (leftPriority < rightPriority) {
            return -1;
        } else if (leftPriority > rightPriority) {
            return 1;
        } else {
            return compareSamePriorityConnectors(leftCM, rightCM);
        }
    }

    /**
     * Compare connectors with the same priority using the native property.
     * @param leftCM   The first connector metadata.
     * @param rightCM  The second connector metadata.
     * @returns -1, 0, 1 if the first connector is preferable respectively to the second one, equal or is not preferable .
     */
    private int compareSamePriorityConnectors(ConnectorMetadata leftCM, ConnectorMetadata rightCM) {
        if(leftCM.isNative() && !rightCM.isNative()){
            return -1;
        }else if (!leftCM.isNative() && rightCM.isNative()){
            return 1;
        }else{
            return 0;
        }
    }
}
