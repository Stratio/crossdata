package com.stratio.sdh.meta.structures;

public class RelationInterrogation extends MetaRelation {

    public RelationInterrogation(String identifier) {
        this.identifier = identifier;
        this.type = TYPE_INTERROGATION;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(identifier);
        sb.append(" IN ?");
        return sb.toString();
    }
    
}
