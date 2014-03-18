package com.stratio.meta.core.structures;

import com.stratio.meta.common.utils.MetaUtils;

import java.util.ArrayList;

public class RelationCompare extends MetaRelation {

    public RelationCompare(String identifier) {
        this.terms = new ArrayList<>();
        this.type = TYPE_COMPARE;
        this.identifiers = new ArrayList<>();
        this.identifiers.add(identifier);
    }   
    
    public RelationCompare(String identifier, String operator) {
        this(identifier);
        this.operator = operator;
    }
    
    public RelationCompare(String identifier, String operator, Term term) {
        this(identifier, operator);        
        this.terms.add(term);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(identifiers.get(0));
        sb.append(" ").append(operator).append(" ").append(MetaUtils.StringList(terms, ", "));
        return sb.toString();
    }
    
}
