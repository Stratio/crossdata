package com.stratio.sdh.meta.structures;

import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.ArrayList;
import java.util.List;

public class RelationToken extends MetaRelation {
    
    public RelationToken(List<String> identifiers) {
        this.terms = new ArrayList<>();
        this.type = TYPE_TOKEN;
        this.identifiers = new ArrayList<>();
        this.identifiers = identifiers;
    }   
    
    public RelationToken(List<String> identifiers, String operator) {
        this(identifiers);
        this.operator = operator;
    }
    
    public RelationToken(List<String> identifiers, String operator, Term term) {
        this(identifiers, operator);        
        this.terms.add(term);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TOKEN(");
        sb.append(MetaUtils.StringList(identifiers, ", ")).append(")");
        sb.append(" ").append(operator).append(" ").append(MetaUtils.StringList(terms, ", "));
        return sb.toString();
    }
    
}
