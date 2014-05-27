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

package com.stratio.meta.core.structures;

public class IdentMap {
    
    private String identifier;
    private MapLiteralProperty mlp;

    public IdentMap(String identifier) {
        this.identifier = identifier;
        this.mlp = new MapLiteralProperty();
    }
    
    public IdentMap(String identifier, MapLiteralProperty mlp) {
        this(identifier);
        this.mlp = mlp;
    }   
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public MapLiteralProperty getMlp() {
        return mlp;
    }

    public void setMlp(MapLiteralProperty mlp) {
        this.mlp = mlp;
    }   
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(identifier);
        sb.append(" + ");
        sb.append(mlp.toString());
        return sb.toString();
    }
    
}
