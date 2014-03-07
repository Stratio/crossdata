package com.stratio.meta.structures;

public class IdentifierAssignment {
    
    public static final int TYPE_SIMPLE = 1;
    public static final int TYPE_COMPOUND = 2;
    
    private String identifier;
    private Term term;
    private int type;

    public IdentifierAssignment(String identifier, Term term, int type) {
        this.identifier = identifier;
        this.term = term;
        this.type = type;
    }
    
    public IdentifierAssignment(String identifier) {
        this(identifier, null, TYPE_SIMPLE);
    }
    
    public IdentifierAssignment(String identifier, Term term) {
        this(identifier, term, TYPE_COMPOUND);
    }
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }        
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(identifier);
        if(type == TYPE_COMPOUND){
            sb.append("[").append(term.toString()).append("]");
        }
        return sb.toString();
    }
    
}
