package com.stratio.meta2.core.statements;

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.Tree;

public class DetachConnectorStatement extends MetaStatement {

  private String name;

  public DetachConnectorStatement(String name){
    this.name = name;
  }

  @Override
  public String toString() {
    return "DETACH CONNECTOR "+name;
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
