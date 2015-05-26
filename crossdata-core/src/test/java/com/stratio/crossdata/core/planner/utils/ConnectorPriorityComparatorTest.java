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
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class ConnectorPriorityComparatorTest {

    public static final String CONNECTOR_1 = "con1";
    public static final String CONNECTOR_2 = "con2";

    public static final ClusterName CLUSTER_MONGO = new ClusterName("mongo");
    public static final ClusterName CLUSTER_CASSANDRA = new ClusterName("cassandra");
    public static final ClusterName CLUSTER_HDFS = new ClusterName("hdfs");

    @Test
    public void compareEqualPriority() throws ManifestException{

        ConnectorMetadata connector = createConnectorMetadata(CONNECTOR_1, Pair.of(CLUSTER_MONGO, 1) );
        ConnectorMetadata connectorWithSamePriority = createConnectorMetadata(CONNECTOR_2, Pair.of(CLUSTER_MONGO, 1));

        ConnectorPriorityComparator comparator = new ConnectorPriorityComparator(Arrays.asList(CLUSTER_MONGO));
        Assert.assertEquals(comparator.compare(connector, connectorWithSamePriority), 0, "Equals connectors must have the same priority");

    }

    @Test
    public void compareGreaterPriority() throws ManifestException{

        ConnectorMetadata connector = createConnectorMetadata(CONNECTOR_1, Pair.of(CLUSTER_MONGO, 1) );
        ConnectorMetadata connectorWithLowerPriority = createConnectorMetadata(CONNECTOR_2, Pair.of(CLUSTER_MONGO, 4));

        ConnectorPriorityComparator comparator = new ConnectorPriorityComparator(Arrays.asList(CLUSTER_MONGO));
        Assert.assertEquals(comparator.compare(connector, connectorWithLowerPriority), -1, "Connector with priority 1 must be choose rather than priority 4");

    }

    @Test
    public void compareLowerPriority() throws ManifestException{

        ConnectorMetadata connector = createConnectorMetadata(CONNECTOR_1, Pair.of(CLUSTER_MONGO, 4) );
        ConnectorMetadata connectorWithGreaterPriority = createConnectorMetadata(CONNECTOR_2, Pair.of(CLUSTER_MONGO, 1));

        ConnectorPriorityComparator comparator = new ConnectorPriorityComparator(Arrays.asList(CLUSTER_MONGO));
        Assert.assertEquals(comparator.compare(connector, connectorWithGreaterPriority), 1, "Connector with priority 1 must be choose rather than priority 4");

    }

    @Test
    public void compareSeveralClusterPriority() throws ManifestException{

        ConnectorMetadata connector = createConnectorMetadata(CONNECTOR_1, Pair.of(CLUSTER_MONGO, 1), Pair.of(CLUSTER_CASSANDRA, 4) );
        ConnectorMetadata otherConnector = createConnectorMetadata(CONNECTOR_2, Pair.of(CLUSTER_MONGO,4), Pair.of(CLUSTER_CASSANDRA, 1));

        ConnectorPriorityComparator comparator = new ConnectorPriorityComparator(Arrays.asList(CLUSTER_MONGO, CLUSTER_CASSANDRA));
        Assert.assertEquals(comparator.compare(connector, otherConnector), 0, "Connector with several clusters must take all of them into consideration");

    }

    @Test
    public void compareClusterNotAffectedPriority() throws ManifestException{

        ConnectorMetadata connector = createConnectorMetadata(CONNECTOR_1, Pair.of(CLUSTER_MONGO, 1), Pair.of(CLUSTER_CASSANDRA, 4) );
        ConnectorMetadata otherConnector = createConnectorMetadata(CONNECTOR_2, Pair.of(CLUSTER_MONGO,4), Pair.of(CLUSTER_CASSANDRA, 1));

        ConnectorPriorityComparator comparator = new ConnectorPriorityComparator(Arrays.asList(CLUSTER_MONGO));
        Assert.assertEquals(comparator.compare(connector, otherConnector), -1, CLUSTER_CASSANDRA +" must not be taken into account");

    }

    @Test
    public void compareConnectorWithAdditionalClusterPriority() throws ManifestException{

        ConnectorMetadata connector = createConnectorMetadata(CONNECTOR_1, Pair.of(CLUSTER_MONGO, 2), Pair.of(CLUSTER_CASSANDRA, 2) );
        ConnectorMetadata otherConnector = createConnectorMetadata(CONNECTOR_2, Pair.of(CLUSTER_MONGO,2), Pair.of(CLUSTER_CASSANDRA, 2) , Pair.of(CLUSTER_HDFS,2));

        ConnectorPriorityComparator comparator = new ConnectorPriorityComparator(Arrays.asList(CLUSTER_MONGO, CLUSTER_CASSANDRA));
        Assert.assertEquals(comparator.compare(connector, otherConnector), 0, CLUSTER_HDFS +" must not be taken into account");

    }

    private ConnectorMetadata createConnectorMetadata(String name, Pair<ClusterName, Integer>... clusterWithPriorities) throws ManifestException {
        Map<ClusterName, Integer> clusterWP = new HashMap<>(clusterWithPriorities.length);
        for (Pair<ClusterName, Integer> clusterPriority : clusterWithPriorities) {
            clusterWP.put(clusterPriority.getLeft(), clusterPriority.getRight());
        }
        ConnectorName connectorName = new ConnectorName(name);
        return new ConnectorMetadata(connectorName, "", null, null, clusterWP, null, null, null, null);
    }
}
