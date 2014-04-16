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


public class ConnectResult extends Result {

    private static final long serialVersionUID = -2632581413648530295L;
    private long sessionId;

    private ConnectResult(long sessionId,boolean error, String errorMessage, boolean ksChanged, String currentKeyspace){
        super(error,errorMessage,ksChanged,currentKeyspace);
        this.sessionId=sessionId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public static ConnectResult CreateSuccessConnectResult(long sessionId){
        return new ConnectResult(sessionId,false,null,false,null);
    }

    public static ConnectResult CreateFailConnectResult(String message){
        return new ConnectResult(-1,true,message,false,null);
    }

    
}
