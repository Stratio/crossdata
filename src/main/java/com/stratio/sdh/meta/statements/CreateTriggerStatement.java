/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

/**
 *
 * @author aalcocer
 */
public class CreateTriggerStatement extends Statement {
    
        private String trigger_name;

    public CreateTriggerStatement(String trigger_name, String table_name, String class_name) {
        this.trigger_name = trigger_name;
        this.table_name = table_name;
        this.class_name = class_name;
    }

    public String getTrigger_name() {
        return trigger_name;
    }

    public void setTrigger_name(String trigger_name) {
        this.trigger_name = trigger_name;
    }

    private String table_name;

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    private String class_name;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;  
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Create trigger ");
        sb.append(trigger_name);
        sb.append(" on ");
        sb.append(table_name);
        sb.append(" using ");
        sb.append(class_name);
        return sb.toString();
    }
    
}
