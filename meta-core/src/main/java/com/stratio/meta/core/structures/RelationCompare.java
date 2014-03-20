package com.stratio.meta.core.structures;

import com.stratio.meta.core.utils.ParserUtils;

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
        sb.append(" ").append(operator).append(" ").append(ParserUtils.stringList(terms, ", "));
        return sb.toString();
    }
    
}
