package com.stratio.meta.utils;

public class MetaStep {
    private MetaPath path;
    private String query;

    public MetaStep(MetaPath path, String query) {
        this.path = path;
        this.query = query;
    }   
    
    public MetaPath getPath() {
        return path;
    }

    public void setPath(MetaPath path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
 
}
