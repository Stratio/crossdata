/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.statements.structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RelationDisjunction extends AbstractRelation {

    private static final long serialVersionUID = 2085700590246602145L;

    private final List<RelationTerm> terms = new ArrayList<>();

    public RelationDisjunction() {
    }

    public RelationDisjunction(RelationTerm... terms) {
        for(int i = 0; i<terms.length; i++){
            this.terms.add(terms[i]);
        }
    }

    public List<RelationTerm> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(isParenthesis()){
            sb.append("(");
        }

        Iterator<RelationTerm> iter = terms.iterator();
        while(iter.hasNext()){
            RelationTerm rt = iter.next();
            sb.append(rt);
            if(iter.hasNext()){
                sb.append(" OR ");
            }
        }

        if(isParenthesis()){
            sb.append(")");
        }
        return sb.toString();
    }

    public String getSelectorTablesAsString() {
        Set<String> allTables = new LinkedHashSet<>();
        // Inner relations
        for(RelationTerm rt: terms){
            allTables.add(getSelectorTablesAsString(rt));
        }
        // Create final String without duplicates
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = allTables.iterator();
        while(iter.hasNext()){
            String name = iter.next();
            sb.append(name);
            if(iter.hasNext()){
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public String getFirstSelectorTablesAsString() {
        Set<String> allTables = new HashSet<>();
        // Inner relations
        for(RelationTerm rt: terms){
            allTables.add(getSelectorTablesAsString(rt));
        }
        // Create final String without duplicates
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = allTables.iterator();
        if(iter.hasNext()){
            String name = iter.next();
            if (name.contains("-")){
                sb.append(name.substring(0, name.indexOf("-")));
            }else {
                sb.append(name);
            }
        }

        return sb.toString();
    }

    public String getSelectorTablesAsString(RelationTerm relationTerm) {
        Set<String> allTables = new HashSet<>();
        allTables.add(relationTerm.getSelectorTablesAsString());
        // Create final String without duplicates
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = allTables.iterator();
        while(iter.hasNext()){
            String name = iter.next();
            sb.append(name);
            if(iter.hasNext()){
                sb.append("-");
            }
        }
        return sb.toString();
    }

}
