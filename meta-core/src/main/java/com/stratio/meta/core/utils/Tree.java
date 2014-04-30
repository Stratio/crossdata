/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.utils;

import com.datastax.driver.core.Session;
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.executor.CassandraExecutor;
import com.stratio.meta.core.executor.CommandExecutor;
import com.stratio.meta.core.executor.DeepExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements a Tree data structure.
 */
public class Tree {

    /**
     * Parent of the root of the current tree.
     */
    private Tree parent = null;

    /**
     * Data stored in the root node.
     */
    private MetaStep node = null;

    /**
     * List of children.
     */
    private List<Tree> children = null;

    /**
     * Class constructor.
     */
    public Tree() {
        children = new ArrayList<>();
    }         
    
    public Tree(MetaStep node) {
        this();
        this.node = node;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public void setNode(MetaStep node) {
        this.node = node;
    }

    public Tree addChild(Tree child){
        child.setParent(this);
        children.add(child);
        return child;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public String toStringDownTop(){ 
        StringBuilder sb = new StringBuilder();
        int deep = 0;
        for(Tree child: children){
            sb.append(child.printDownTop(deep+1)).append(System.getProperty("line.separator"));
        }
        if(node != null){
            sb.append(node.toString());
        }
        return sb.toString();
    }
    
    private String printDownTop(int deep){
        StringBuilder sb = new StringBuilder();
        sb.append(node.toString());
        for(Tree child: children){
            sb.append(child.printDownTop(deep+1)).append(System.getProperty("line.separator"));
        }        
        for(int i=0; i<deep; i++){
            sb.append("\t");
        }

        return sb.toString();
    }

    public Result executeTreeDownTop(Session session, DeepSparkContext deepSparkContext, EngineConfig engineConfig){
        // Get results from my children
        List<Result> resultsFromChildren = new ArrayList<>();
        for(Tree child: children){
            resultsFromChildren.add(child.executeTreeDownTop(session,deepSparkContext, engineConfig));
        }
        // Execute myself and return final result
        return executeMyself(session, deepSparkContext, engineConfig, resultsFromChildren);
    }

    public Result executeMyself(Session session, DeepSparkContext deepSparkContext, EngineConfig engineConfig, List<Result> resultsFromChildren){
        if(node == null){
            return QueryResult.createSuccessQueryResult();
        }
        MetaStep myStep = (MetaStep) node;
        MetaPath myPath = myStep.getPath();
        if(myPath == MetaPath.COMMAND){
            return CommandExecutor.execute(myStep.getStmt(), session);
        } else if(myPath == MetaPath.CASSANDRA){
            return CassandraExecutor.execute(myStep, session);
        } else if(myPath == MetaPath.DEEP){
            return DeepExecutor.execute(myStep.getStmt(), resultsFromChildren, isRoot(), session, deepSparkContext, engineConfig);
        } else if(myPath == MetaPath.UNSUPPORTED){
            return QueryResult.createFailQueryResult("Query not supported.");
        } else {
            return QueryResult.createFailQueryResult("Query not supported yet.");
        }
    }

    public boolean isEmpty(){
        return (node == null);
    }

    public MetaStep getNode(){
        return node;
    }

    public  List<Tree> getChildren(){
        return children;
    }

}
