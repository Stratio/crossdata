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


public class CommandResult extends Result {

    
    private final String result;

    private CommandResult(String result,boolean error, String errorMessage, boolean ksChanged, String currentKeyspace){
        super(error,errorMessage,ksChanged,currentKeyspace);
        this.result=result;
    }

    public String getResult() {
        return result;
    }

    public static CommandResult CreateSuccessCommandResult(String result){
        return new CommandResult(result,false,null,false,null);
    }

    public static CommandResult CreateFailCommanResult(String errorMessage){
        return new CommandResult(null,true,errorMessage,false,null);
    }


}
