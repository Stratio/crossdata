package com.stratio.meta.core.structures;

public class MetaOrdering {
    
    private String identifier;
    private boolean dirInc;
    private OrderDirection orderDir;

    public MetaOrdering(String identifier, boolean dirInc, OrderDirection orderDir) {
        this.identifier = identifier;
        this.dirInc = dirInc;
        this.orderDir = orderDir;
    }
    
    public MetaOrdering(String identifier) {
        this(identifier, false, null);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isDirInc() {
        return dirInc;
    }

    public void setDirInc(boolean dirInc) {
        this.dirInc = dirInc;
    }

    public OrderDirection getOrderDir() {
        return orderDir;
    }        

    public void setOrderDir(OrderDirection orderDir) {
        this.dirInc = true;
        this.orderDir = orderDir;
    }        
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(identifier);
        if(dirInc){
            sb.append(" ").append(orderDir);
        }
        return sb.toString();
    }
    
}
