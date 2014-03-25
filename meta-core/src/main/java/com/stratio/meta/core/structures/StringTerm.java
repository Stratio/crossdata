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

public class StringTerm extends Term{

    private String term;
    private boolean quotedLiteral = false;

    public StringTerm(String term, boolean quotedLiteral) {
        this.term = term;
        this.quotedLiteral = quotedLiteral;
    }

    public StringTerm(String term) {
        this.term = term;
        this.type = TYPE_TERM;
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

    /** {@inheritDoc} */
    @Override
    public String toString() {
        if(this.isQuotedLiteral()){
            return "'"+term+"'";
        } else {
            return term;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Class getTermClass() {
        return String.class;
    }

    /** {@inheritDoc} */
    @Override
    public Object getTermValue() {
        return term;
    }
}
