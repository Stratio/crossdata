package com.stratio.connector.inmemory.datastore.datatypes;

import com.stratio.crossdata.common.metadata.DataType;
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

    public static AbstractInMemoryDataType castToNativeDataType(String dataType)  throws ExecutionException{
        AbstractInMemoryDataType nativeDataType = null;
        if(dataType != null){
            if(dataType.equals("Date")){
                nativeDataType = new Date();
            }else{
                try {
                    DataType.valueOf(dataType);
                }catch (IllegalArgumentException e){
                    throw new ExecutionException("You have an error in your SQL syntax, the type '" + dataType + "' is not supported");
                }
            }
        }
        return nativeDataType;
    }

}
