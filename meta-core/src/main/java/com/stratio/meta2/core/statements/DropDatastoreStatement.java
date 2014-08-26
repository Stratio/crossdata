package com.stratio.meta2.core.statements;

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.Tree;

public class DropDatastoreStatement extends MetaStatement {

  private String name;

  public DropDatastoreStatement(String name){
    this.name = name;
  }

  @Override
  public String toString() {
    return null;
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
