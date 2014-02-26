package com.stratio.meta.structures;

public class Term extends ValueCell {
    
    private String term;

    public Term(String term) {
        this.term = term;
        this.type = TYPE_TERM;
    }   
    
    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return term;
    }
    
}
