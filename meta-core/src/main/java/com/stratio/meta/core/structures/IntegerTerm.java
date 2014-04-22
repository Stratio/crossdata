package com.stratio.meta.core.structures;

public class IntegerTerm extends Term{

    private final Integer number;

    public IntegerTerm(String term){
        number = Integer.valueOf(term);
    }

    /** {@inheritDoc} */
    @Override
    public Class<Integer> getTermClass() {
        return Integer.class;
    }

    /** {@inheritDoc} */
    @Override
    public Object getTermValue() {
        return number;
    }

    @Override
    public String getStringValue() {
        return toString();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ""+ number;
    }
}
