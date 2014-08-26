package com.stratio.meta2.core.statements;

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.Tree;
import com.stratio.meta2.core.statements.MetaStatement;

public class DropConnectorStatement extends MetaStatement {

  private String name;

  public DropConnectorStatement(String name){
    this.name = name;
  }

  @Override
  public String toString() {
    return "DROP CONNECTOR "+name;
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
