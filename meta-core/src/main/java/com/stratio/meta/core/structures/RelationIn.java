package com.stratio.meta.core.structures;

import com.stratio.meta.core.utils.ParserUtils;

import java.util.ArrayList;
import java.util.List;

public class RelationIn extends MetaRelation {    

    public RelationIn(String identifier) {
        this.terms = new ArrayList<>();
        this.type = TYPE_IN;
        this.operator = "IN";
        this.identifiers = new ArrayList<>();
        this.identifiers.add(identifier);
    }
    
    public RelationIn(String identifier, List<Term> terms) {
        this(identifier);
        this.terms = terms;
    }   
       
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(identifiers.get(0));
        sb.append(" ").append(operator).append(" ").append("(").append(ParserUtils.stringList(terms, ", ")).append(")");
        return sb.toString();
    }
    
}
