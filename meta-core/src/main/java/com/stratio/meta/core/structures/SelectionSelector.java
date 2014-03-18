package com.stratio.meta.core.structures;

public class SelectionSelector {
    
    SelectorMeta selector;
    boolean aliasInc;
    String alias;

    public SelectionSelector(SelectorMeta selector, boolean aliasInc, String alias) {
        this.selector = selector;
        this.aliasInc = aliasInc;
        this.alias = alias;
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

    public boolean isAliasInc() {
        return aliasInc;
    }

    public void setAliasInc(boolean aliasInc) {
        this.aliasInc = aliasInc;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.aliasInc = true;
        this.alias = alias;
    }        
        
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(selector.toString());
        if(aliasInc){
            sb.append(" AS ").append(alias);
        }
        return sb.toString();
    }
    
}
