package com.stratio.sdh.meta.structures;

import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.ArrayList;

public class RelationBetween extends MetaRelation {

    public RelationBetween(String identifier) {
        this.terms = new ArrayList<>();
        this.type = TYPE_BETWEEN;
        this.operator = "BETWEEN";
        this.identifiers = new ArrayList<>();
        this.identifiers.add(identifier);
    }       
    
    public RelationBetween(String identifier, Term term1, Term term2) {
        this(identifier);        
        this.terms.add(term1);
        this.terms.add(term2);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(identifiers.get(0));
        sb.append(" ").append(operator).append(" ").append(MetaUtils.StringList(terms, " AND "));
        return sb.toString();
    } 
    
}
