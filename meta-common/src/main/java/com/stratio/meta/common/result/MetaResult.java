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

package com.stratio.meta.common.result;

import org.apache.log4j.Logger;

import java.io.Serializable;

public class MetaResult implements Serializable {

    transient final Logger logger = Logger.getLogger(MetaResult.class);
    
    private boolean hasError = false;
    private String errorMessage = null;
    private boolean ksChanged = false;
    private String currentKeyspace = null;

    public boolean hasError() {
        return hasError;
    }

    public void setHasError() {
        this.hasError = true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the error message and set {@code hasError} to true.
     * @param errorMessage The error message.
     */
    public void setErrorMessage(String errorMessage) {
        this.hasError = true;
        this.errorMessage = errorMessage;
    }   

    public boolean isKsChanged() {
        return ksChanged;
    }

    public void setKsChanged() {
        this.ksChanged = true;
    }   
    
    public String getCurrentKeyspace() {
        return currentKeyspace;
    }

    public void setCurrentKeyspace(String currentKeyspace) {
        this.ksChanged = true;
        this.currentKeyspace = currentKeyspace;
    }        
    
    public void print(){
        logger.info("\033[32mResult:\033[0m"+System.getProperty("line.separator")+errorMessage);
    }
    
    @Override
    public String toString(){
        return System.getProperty("line.separator")+errorMessage;
    }

    public static MetaResult createMetaResultError(String errorMessage){
        MetaResult result=new MetaResult();
        result.setErrorMessage(errorMessage);
        result.setHasError();
        return result;
    }
    public static MetaResult createMetaResultWelcome(){
        MetaResult result=new MetaResult();
        result.setKsChanged();
        return result;
    }
    
}
