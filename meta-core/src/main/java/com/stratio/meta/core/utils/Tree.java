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
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
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
    private Object node = null;

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
    
    public Tree(Object node) {
        this();
        this.node = node;
    }        

    public Tree(Tree parent, Object node) {
        this();
        this.parent = parent;
        this.node = node;
    }        

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public void setNode(Object node) {
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

    public Result executeTreeDownTop(Session session){
        // Get results from my children
        List<Result> resultsFromChildren = new ArrayList<>();
        for(Tree child: children){
            resultsFromChildren.add(child.executeTreeDownTop(session));
        }
        // Execute myself and return final result
        return executeMyself(session, resultsFromChildren);
    }

    public Result executeMyself(Session session, List<Result> resultsFromChildren){
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
            return DeepExecutor.execute(myStep.getStmt(), resultsFromChildren, isRoot(), session);
        } else if(myPath == MetaPath.UNSUPPORTED){
            return QueryResult.createFailQueryResult("Query not supported.");
        } else {
            return QueryResult.createFailQueryResult("Query not supported yet.");
        }
    }

    public String toStringTopDown(){
        StringBuilder sb = new StringBuilder();
        int deep = 0;
        sb.append(node.toString()).append(System.lineSeparator());
        for(Tree child: children){
            sb.append(child.printTopDown(deep+1)).append(System.lineSeparator());
        }                
        return sb.toString();
    }
    
    private String printTopDown(int deep){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<deep; i++){
            sb.append("\t");
        }
        sb.append(node.toString());
        for(Tree child: children){
            sb.append(child.printDownTop(deep+1)).append(System.lineSeparator());
        }                
        return sb.toString();
    }

}
