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

public class AttachConnectorStatement extends MetadataStatement {

    private ConnectorName connectorName;
    private ClusterName clusterName;

    /**
     * The map of options passed to the connector during its attachment.
     */
    private Map<Selector, Selector> options = null;

    public AttachConnectorStatement(ConnectorName connectorName, ClusterName clusterName, String json) {
        this.connectorName = connectorName;
        this.clusterName = clusterName;
        this.options = StringUtils.convertJsonToOptions(json);
    }

    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(ValidationTypes.MUST_EXIST_CLUSTER)
                .add(ValidationTypes.MUST_EXIST_CONNECTOR)
                .add(ValidationTypes.VALID_CONNECTOR_OPTIONS)
                .add(ValidationTypes.MUST_BE_CONNECTED);
    }

    public ConnectorName getConnectorName() {
        return connectorName;
    }

    public ClusterName getClusterName() {
        return clusterName;
    }

    public Map<Selector, Selector> getOptions() {
        return options;
    }

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
                options);
    }
}
