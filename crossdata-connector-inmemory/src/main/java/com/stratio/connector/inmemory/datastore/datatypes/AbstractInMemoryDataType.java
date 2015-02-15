package com.stratio.connector.inmemory.datastore.datatypes;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.exceptions.ExecutionException;

public abstract class AbstractInMemoryDataType {

    /**
     * Class logger.
     */
    protected static final Logger LOG = Logger.getLogger(AbstractInMemoryDataType.class);

    public abstract Class<?> getClazz();

    public abstract void setClazz(Class<?> clazz);

    public abstract void setParameters(Object... parameters);

    public abstract Object convertStringToInMemoryDataType(String input) throws ExecutionException;

    public abstract String convertInMemoryDataTypeToString(Object input);

    public static AbstractInMemoryDataType castToNativeDataType(String dataType){
        AbstractInMemoryDataType nativeDataType = null;
        if(dataType != null){
            if(dataType.equals("Date")){
                nativeDataType = new Date();
            }
        }
        return nativeDataType;
    }

}
