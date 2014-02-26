package com.stratio.meta.structures;

import java.util.List;

public class PropertyClusteringOrder extends MetaProperty{

    private List<MetaOrdering> order;
    
    public PropertyClusteringOrder() {
        super(TYPE_CLUSTERING_ORDER);
    }

    public PropertyClusteringOrder(List<MetaOrdering> order) {
        super(TYPE_CLUSTERING_ORDER);
        this.order = order;
    }
   
    
    public List<MetaOrdering> getOrder() {
        return order;
    }

    public void setOrder(List<MetaOrdering> order) {
        this.order = order;
    }        
    
}
