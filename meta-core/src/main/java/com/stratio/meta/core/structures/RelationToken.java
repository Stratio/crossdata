package com.stratio.meta.core.structures;

import com.stratio.meta.common.utils.MetaUtils;

import java.util.ArrayList;
import java.util.List;

public class RelationToken extends MetaRelation {
    
    private boolean righSideTokenType = false;
    
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
    
    public RelationToken(List<String> identifiers, String operator, List<Term> terms) {
        this(identifiers, operator); 
        this.terms = terms;
        this.righSideTokenType = true;
    }

    public boolean isRighSideTokenType() {
        return righSideTokenType;
    }

    public void setRighSideTokenType(boolean righSideTokenType) {
        this.righSideTokenType = righSideTokenType;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TOKEN(");
        sb.append(MetaUtils.StringList(identifiers, ", ")).append(")");
        sb.append(" ").append(operator).append(" ");
        if(righSideTokenType){
            sb.append("TOKEN(").append(MetaUtils.StringList(terms, ", ")).append(")");
        } else {
            sb.append(MetaUtils.StringList(terms, ", "));
        }
        return sb.toString();
    }
    
}
