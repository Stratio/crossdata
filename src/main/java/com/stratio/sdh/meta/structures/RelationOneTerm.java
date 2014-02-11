package com.stratio.sdh.meta.structures;

public class RelationOneTerm extends MetaRelation {
    
    private Term term;

    public RelationOneTerm(String identifier, Term term) {
        this.identifier = identifier;
        this.term = term;
        this.type = TYPE_ONE_TERM;
    }   
    
    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(identifier);
        sb.append(" = ").append(term.toString());
        return sb.toString();
    }
    
}
