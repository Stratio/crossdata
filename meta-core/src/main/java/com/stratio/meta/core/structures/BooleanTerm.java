package com.stratio.meta.core.structures;


public class BooleanTerm extends Term{

    /**
     * The boolean value stored by this term.
     */
    private final Boolean value;

    /**
     * Class constructor.
     * @param term The string representation of a Boolean value.
     */
    public BooleanTerm(String term){
        value = Boolean.valueOf(term);
    }

    @Override
    public Class<Boolean> getTermClass() {
        return Boolean.class;
    }

    @Override
    public Object getTermValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return toString();
    }

    @Override
    public String toString() {
        return ""+ value;
    }
}
