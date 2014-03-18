package com.stratio.meta.core.structures;

public class IdentMap {
    
    private String identifier;
    private MapLiteralProperty mlp;

    public IdentMap(String identifier) {
        this.identifier = identifier;
        this.mlp = new MapLiteralProperty();
    }
    
    public IdentMap(String identifier, MapLiteralProperty mlp) {
        this(identifier);
        this.mlp = mlp;
    }   
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public MapLiteralProperty getMlp() {
        return mlp;
    }

    public void setMlp(MapLiteralProperty mlp) {
        this.mlp = mlp;
    }   
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(identifier);
        sb.append(" + ");
        sb.append(mlp.toString());
        return sb.toString();
    }
    
}
