package com.stratio.meta.structures;

public class IntTerm extends IdentIntOrLiteral {
    
    private int term;

    public IntTerm(String identifier, char operator, int term) {
        this.identifier = identifier;
        this.operator = operator;
        this.term = term;
    }        
    
    public IntTerm(int term){
        this.term = term;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }    
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(string());
        sb.append(Integer.toString(term));
        return sb.toString();
    }
}
