package com.stratio.sdh.meta.structures;

import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.ArrayList;
import java.util.List;

public class RelationTerms extends MetaRelation {
    
    private List<Term> terms;

    public RelationTerms(String identifier) {
        terms = new ArrayList<>();
        this.type = TYPE_TERMS;
        this.identifier = identifier;
    }
    
    public RelationTerms(String identifier, List<Term> terms) {
        this(identifier);
        this.terms = terms;
    }   
    
    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }        
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(identifier);
        sb.append(" IN ").append("(").append(MetaUtils.StringList(terms, ", ")).append(")");
        return sb.toString();
    }
    
}
