package com.stratio.meta.core.structures;


public class BooleanTerm extends Term{

    private final Boolean _value;

    public BooleanTerm(String term){
        _value = Boolean.valueOf(term);
    }

    /** {@inheritDoc} */
    @Override
    public Class getTermClass() {
        return Boolean.class;
    }

    /** {@inheritDoc} */
    @Override
    public Object getTermValue() {
        return _value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ""+_value;
    }
}
