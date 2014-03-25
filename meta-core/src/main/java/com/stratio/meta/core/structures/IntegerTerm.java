package com.stratio.meta.core.structures;

/**
 * Created by dhiguero on 25/03/14.
 */
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

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ""+_number;
    }
}
