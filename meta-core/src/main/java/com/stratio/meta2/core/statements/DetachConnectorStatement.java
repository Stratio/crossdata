package com.stratio.meta2.core.statements;

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.Tree;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ConnectorName;

public class DetachConnectorStatement extends MetaStatement {

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
  public String translateToCQL() {
    return null;
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    return null;
  }
}
