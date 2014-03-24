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

public class Term extends ValueCell {
        
    private String term;
    private boolean quotedLiteral = false;

    public Term(String term, boolean quotedLiteral) {
        this.term = term;
        this.quotedLiteral = quotedLiteral;
    }   
    
    public Term(String term) {
        this.term = term;
        this.type = TYPE_TERM;
    }   
    
    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    
    public void setTerm(String term, boolean quotedLiteral) {
        this.term = term;
        this.quotedLiteral = quotedLiteral;
    }

    public boolean isQuotedLiteral() {
        return quotedLiteral;
    }

    public void setQuotedLiteral(boolean quotedLiteral) {
        this.quotedLiteral = quotedLiteral;
    }    

    @Override
    public String toString() {
        if(this.isQuotedLiteral()){
            return "'"+term+"'";
        } else {
            return term;
        }
    }
    
}
