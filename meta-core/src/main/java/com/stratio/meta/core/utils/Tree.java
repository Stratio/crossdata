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

public class Tree {

    private Tree parent;
    private Object node;
    private List<Tree> children = new ArrayList<>();

    public Tree() {
        children = new ArrayList<>();
    }         
    
    public Tree(Object node) {
        this.node = node;
    }        

    public Tree(Tree parent, Object node) {
        this.parent = parent;
        this.node = node;
    }        
    
    public Tree getParent() {
        return parent;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public Object getNode() {
        return node;
    }

    public void setNode(Object node) {
        this.node = node;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Tree> children) {
        for(Tree child: children){
            child.setParent(this);
        }
        this.children = children;
    }
    
    public Tree addChild(Tree child){
        child.setParent(this);
        children.add(child);
        return child;
    }
    
    public Tree getChild(int index){
        return children.get(index);
    }   
    
    public boolean isLeaf(){
        return children.isEmpty();
    }
    
    public boolean isEmpty() {
        return (isLeaf() && (node == null));
    }
    
    public boolean isRoot() {
        return (parent == null);
    }
        
    public Tree getRoot(){
        if(parent == null){
            return this;
        } else {
            return parent.getParent();
        }
    }
    
    public Tree getFirstLeaf(){
        if(isLeaf()){
            return this;
        } else {
            return children.get(0);
        }
    }
    
    public boolean areSiblings(Tree tree){
        return (tree.getParent().equals(parent));
    }
    
    public int getDeep(){
        int deep = -1;
        if(!isRoot()){
            deep = parent.getCurrentDeep(deep);
        }
        return deep+1;
    }
    
    private int getCurrentDeep(int deep){
        if(!isRoot()){
            deep = parent.getCurrentDeep(deep);
        }
        return deep+1;
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
            return QueryResult.CreateSuccessQueryResult();
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
            return QueryResult.CreateFailQueryResult("Query not supported.");
        } else {
            return QueryResult.CreateFailQueryResult("Query not supported yet.");
        }
    }

    public String toStringTopDown(){
        StringBuilder sb = new StringBuilder();
        int deep = 0;
        sb.append(node.toString()).append(System.getProperty("line.separator"));
        for(Tree child: children){
            sb.append(child.printTopDown(deep+1)).append(System.getProperty("line.separator"));
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
            sb.append(child.printDownTop(deep+1)).append(System.getProperty("line.separator"));
        }                
        return sb.toString();
    }

    public List<Tree> getSiblings() {
        List<Tree> allSiblings = parent.getChildren();
        List<Tree> siblings = new ArrayList<>();
        for(Tree sibling: allSiblings){
            if(sibling != this){
                try {
                    siblings.add((Tree) sibling.clone());
                } catch (CloneNotSupportedException e) {
                    return null;
                }
            }
        }
        return siblings;
    }
}
