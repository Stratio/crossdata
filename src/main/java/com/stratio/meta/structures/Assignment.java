package com.stratio.meta.structures;

public class Assignment {

    private IdentifierAssignment ident; 
    private ValueAssignment value; 

    public Assignment(IdentifierAssignment ident, ValueAssignment value) {
        this.ident = ident;
        this.value = value;
    }   
    
    public IdentifierAssignment getIdent() {
        return ident;
    }

    public void setIdent(IdentifierAssignment ident) {
        this.ident = ident;
    }

    public ValueAssignment getValue() {
        return value;
    }

    public void setValue(ValueAssignment value) {
        this.value = value;
    }   
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(ident.toString());
        sb.append(" = ");
        sb.append(value.toString());
        return sb.toString();
    }
    
}
