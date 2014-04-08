package com.stratio.meta.core.structures;

public class IntegerTerm extends Term{

    private final Integer _number;

    public IntegerTerm(String term){
        _number = Integer.valueOf(term);
    }

    /** {@inheritDoc} */
    @Override
    public Class getTermClass() {
        return Integer.class;
    }

    /** {@inheritDoc} */
    @Override
    public Object getTermValue() {
        return _number;
    }

    @Override
    public String getStringValue() {
        return toString();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ""+_number;
    }
}
