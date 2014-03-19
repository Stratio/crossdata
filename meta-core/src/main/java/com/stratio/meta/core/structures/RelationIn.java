package com.stratio.meta.core.structures;

import com.stratio.meta.sh.utils.ShUtils;

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
        sb.append(" ").append(operator).append(" ").append("(").append(ShUtils.StringList(terms, ", ")).append(")");
        return sb.toString();
    }
    
}
