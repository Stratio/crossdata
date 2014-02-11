package com.stratio.sdh.meta.structures;

public class ValueAssignment {
    
    public static final int TYPE_TERM = 1;
    public static final int TYPE_IDENT_INT_OR_LITERAL = 2;
    public static final int TYPE_IDENT_MAP = 3;
    
    private Term term;
    private IdentIntOrLiteral iiol;
    private IdentMap identMap;    
    private int type;        

    public ValueAssignment(Term term, IdentIntOrLiteral iiol, IdentMap identMap, int type) {
        this.term = term;
        this.iiol = iiol;
        this.identMap = identMap;
        this.type = type;
    }    
    
    public ValueAssignment(Term term) {
        this(term, null, null, TYPE_TERM);        
    }
    
    public ValueAssignment(IdentIntOrLiteral iiol) {
        this(null, iiol, null, TYPE_IDENT_INT_OR_LITERAL);
    }    
    
    public ValueAssignment(IdentMap identMap) {
        this(null, null, identMap, TYPE_IDENT_MAP);
    }           
    
    public int getType() {
        return type;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public IdentIntOrLiteral getIiol() {
        return iiol;
    }

    public void setIiol(IdentIntOrLiteral iiol) {
        this.iiol = iiol;
    }

    public IdentMap getIdentMap() {
        return identMap;
    }

    public void setIdentMap(IdentMap identMap) {
        this.identMap = identMap;
    }

    public void setType(int type) {
        this.type = type;
    }
            
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(type == TYPE_TERM){
            sb.append(term.toString());
        } else if(type == TYPE_IDENT_INT_OR_LITERAL){
            sb.append(iiol.toString());
        } else if(type == TYPE_IDENT_MAP){
            sb.append(identMap.toString());
        }
        return sb.toString();
    }
    
}
