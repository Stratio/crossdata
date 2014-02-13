package com.stratio.sdh.meta.structures;

public class SelectionSelector {
    
    SelectorMeta selector;
    boolean identInc;
    String identifier;

    public SelectionSelector(SelectorMeta selector, boolean identInc, String identifier) {
        this.selector = selector;
        this.identInc = identInc;
        this.identifier = identifier;
    }
    
    public SelectionSelector(SelectorMeta selector) {
        this(selector, false, null);
    }
    
    public SelectorMeta getSelector() {
        return selector;
    }

    public void setSelector(SelectorMeta selector) {
        this.selector = selector;
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
        this.identInc = true;
        this.identifier = identifier;
    }        
        
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(selector.toString());
        if(identInc){
            sb.append(" AS ").append(identifier);
        }
        return sb.toString();
    }
    
}
