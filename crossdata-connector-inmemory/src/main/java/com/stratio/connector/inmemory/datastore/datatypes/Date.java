package com.stratio.connector.inmemory.datastore.datatypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.stratio.crossdata.common.exceptions.ExecutionException;

public class Date extends AbstractInMemoryDataType {

    private Class<?> clazz = Date.class;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setParameters(Object... parameters){
        formatter = new SimpleDateFormat(String.class.cast(parameters[0]));
    }

    public Object convertStringToInMemoryDataType(String input) throws ExecutionException {
        java.util.Date output;
        try {
            output = formatter.parse(input);
        } catch (ParseException e) {
            throw new ExecutionException(e.getMessage());
        }
        return output;
    }

    public String convertInMemoryDataTypeToString(Object input){
        java.util.Date date = java.util.Date.class.cast(input);
        return formatter.format(date);
    }

}
