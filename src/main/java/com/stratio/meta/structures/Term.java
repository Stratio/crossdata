package com.stratio.meta.structures;

public class Term extends ValueCell {
        
    private String term;
    private boolean quotedLiteral = false;

    public Term(String term, boolean quotedLiteral) {
        this.term = term;
        this.quotedLiteral = quotedLiteral;
    }   
    
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
    
    public void setTerm(String term, boolean quotedLiteral) {
        this.term = term;
        this.quotedLiteral = quotedLiteral;
    }

    public boolean isQuotedLiteral() {
        return quotedLiteral;
    }

    public void setQuotedLiteral(boolean quotedLiteral) {
        this.quotedLiteral = quotedLiteral;
    }    

    @Override
    public String toString() {
        if(this.isQuotedLiteral()){
            return "'"+term+"'";
        } else {
            return term;
        }
    }
    
}
