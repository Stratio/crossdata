package com.stratio.sdh.meta.structures;

public class SelectionCount extends SelectionClause {

    private char symbol;
    private boolean identInc;
    private String identifier;

    public SelectionCount() {
        this.type = TYPE_COUNT;
    }
    
    public SelectionCount(char symbol, boolean identInc, String identifier) {
        this.type = TYPE_COUNT;
        this.symbol = symbol;
        this.identInc = identInc;
        this.identifier = identifier;
    }
    
    public SelectionCount(char symbol) {
        this(symbol, false, null);
    }
    
    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isIdentInc() {
        return identInc;
    }

    public void setIdentInc(boolean identInc) {
        this.identInc = identInc;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }        
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("COUNT(");        
        sb.append(symbol).append(")");
        if(identInc){
            sb.append(" AS ").append(identifier);
        }
        return sb.toString();
    }
    
}
