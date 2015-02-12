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

package com.stratio.crossdata.core.statements;

import java.util.Iterator;
import java.util.Map;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

/**
 * Class that implement the statement logic of Attach connector to crossdata.
 */
public class AttachConnectorStatement extends MetadataStatement {

    /**
     * The connector name.
     */
    private ConnectorName connectorName;

    /**
     * The cluster name where the connector will be attached.
     */
    private ClusterName clusterName;

    /**
     * The map of options passed to the connector during its attachment.
     */
    private Map<Selector, Selector> options = null;

    /**
     * The priority passed to the connector during its attachment.
     */
    private Integer priority;

    /**
     * Constructor class.
     *
     * @param connectorName The connector name.
     * @param clusterName   The cluster where the connector will be attached.
     * @param json          A json with the options of the attach connector sentence.
     * @param priority      The connector's priority for the associated cluster.
     */
    public AttachConnectorStatement(ConnectorName connectorName, ClusterName clusterName, String json, Integer priority) {
        this.connectorName = connectorName;
        this.clusterName = clusterName;
        this.options = StringUtils.convertJsonToOptions(null, json);
        this.priority = priority;
    }

    /**
     * Get the validation requirements to attach connector.
     *
     * @return A {@link com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation} .
     */
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(ValidationTypes.MUST_EXIST_CLUSTER)
                .add(ValidationTypes.MUST_EXIST_CONNECTOR)
                .add(ValidationTypes.VALID_CONNECTOR_OPTIONS)
                .add(ValidationTypes.MUST_BE_CONNECTED)
                .add(ValidationTypes.VALIDATE_PRIORITY);
    }

    /**
     * Get the connector name.
     *
     * @return A {@link com.stratio.crossdata.common.data.ConnectorName} .
     */
    public ConnectorName getConnectorName() {
        return connectorName;
    }

    /**
     * Get the Cluster name.
     *
     * @return A {@link com.stratio.crossdata.common.data.ClusterName } .
     */
    public ClusterName getClusterName() {
        return clusterName;
    }

    /**
     * Get the Options of the attach connector sentence.
     *
     * @return A Map of {@link com.stratio.crossdata.common.statements.structures.Selector},
     *          {@link com.stratio.crossdata.common.statements.structures.Selector}
     */
    public Map<Selector, Selector> getOptions() {
        return options;
    }

    /**
     * Get the priority. The maximum priority is 1, whereas the minimum is 10.
     * @return The connector's priority for the associated cluster.
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Transform the options of the attach connector to a String.
     * @param options The map of options.
     * @return A String.
     */
    private String getStringFromOptions(Map<Selector, Selector> options) {
        StringBuilder sb = new StringBuilder("{");
        Iterator<Map.Entry<Selector, Selector>> entryIt = options.entrySet().iterator();
        Map.Entry<Selector, Selector> e;
        while (entryIt.hasNext()) {
            e = entryIt.next();
            sb.append(e.getKey()).append(": ").append(e.getValue());
            if (entryIt.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ATTACH CONNECTOR " + connectorName + " TO " + clusterName + " WITH OPTIONS " + getStringFromOptions(
                options) +" AND WITH PRIORITY = "+priority;
    }
}
