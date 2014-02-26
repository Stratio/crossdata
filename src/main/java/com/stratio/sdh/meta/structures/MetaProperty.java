package com.stratio.sdh.meta.structures;

public abstract class MetaProperty {
    
    public static final int TYPE_NAME_VALUE = 1;
    public static final int TYPE_COMPACT_STORAGE = 2;
    public static final int TYPE_CLUSTERING_ORDER = 3;
    
    protected int type;
    //private LinkedHashMap<String, ValueProperty> propierties = new LinkedHashMap<>(); 
    //private List<MetaOrdering> orderings = new ArrayList<>();

    public MetaProperty(int type) {
        this.type = type;
    }       

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /*
    public LinkedHashMap<String, ValueProperty> getPropierties() {
        return propierties;
    }

    public void setPropierties(LinkedHashMap<String, ValueProperty> propierties) {
        this.propierties = propierties;
    }
    
    public void addProperty(String name, ValueProperty value){
        this.propierties.put(name, value);
    }
    
    public ValueProperty getProperty(String name){
        return this.propierties.get(name);
    }
    
    public void removeProperty(String name){
        this.propierties.remove(name);
    }

    public List<MetaOrdering> getOrderings() {
        return orderings;
    }

    public void setOrderings(List<MetaOrdering> orderings) {
        this.orderings = orderings;
    }
    
    public void addOrdering(MetaOrdering ordering){
        this.orderings.add(ordering);
    }
    
    public MetaOrdering getOrdering(int index){
        return this.orderings.get(index);
    }
    
    public void removeOrdering(int index){
        this.orderings.remove(index);
    }
    */
}
