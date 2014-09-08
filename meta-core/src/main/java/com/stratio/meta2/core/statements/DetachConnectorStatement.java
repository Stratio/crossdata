package com.stratio.meta2.core.statements;

import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.core.validator.Validation;
import com.stratio.meta2.core.validator.ValidationRequirements;

public class DetachConnectorStatement extends MetaDataStatement {

  private ConnectorName connectorName;
  private ClusterName clusterName;

  public DetachConnectorStatement(ConnectorName connectorName, ClusterName clusterName){
    this.connectorName = connectorName;
    this.clusterName = clusterName;
  }

  @Override
  public String toString() {
    return "DETACH CONNECTOR " + connectorName.getQualifiedName() + " FROM " + clusterName.getQualifiedName();
  }

  @Override
  public ValidationRequirements getValidationRequirements() {
    return new ValidationRequirements().add(Validation.MUST_EXIST_CLUSTER)
        .add(Validation.MUST_EXIST_ATTACH_CONNECTOR_CLUSTER);
  }
}
