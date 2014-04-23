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

import org.apache.log4j.Logger;

public class AntlrError {

    private final String header;
    private final String message;
    
    public AntlrError(String header, String message) {
        this.header = header;
        this.message = message;
    }

    public String getHeader() {
        return header;
    }

    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("\tError recognized: ");        
        sb.append(header).append(": ").append(message);
        return sb.toString();        
    }
    
    public String toStringWithTokenTranslation(){
        StringBuilder sb = new StringBuilder("\tError recognized: ");        
        sb.append(header).append(": ");
        sb.append(ParserUtils.translateToken(message));
        return sb.toString();        
    }
    
}
