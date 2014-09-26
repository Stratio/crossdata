package com.stratio.meta2.core.statements;

import java.util.Map;

import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

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

    @Override
    public String toString() {
        return "ATTACH CONNECTOR " + connectorName + " TO " + clusterName + " WITH OPTIONS " + StringUtils
                .getStringFromOptions(options);
    }

    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(Validation.MUST_EXIST_CLUSTER)
                .add(Validation.MUST_EXIST_CONNECTOR)
                .add(Validation.VALID_CONNECTOR_OPTIONS);
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
}
