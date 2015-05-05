package com.stratio.connector.inmemory.datastore.datatypes;

import com.stratio.crossdata.common.metadata.DataType;
import org.apache.log4j.Logger;

import com.stratio.crossdata.common.exceptions.ExecutionException;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractInMemoryDataType {

    /**
     * Class logger.
     */
    protected static final Logger LOG = Logger.getLogger(AbstractInMemoryDataType.class);

    private static final Set<String> SUPPORTED_TYPES = new HashSet();
    static{
        SUPPORTED_TYPES.add(Boolean.class.getSimpleName());
        SUPPORTED_TYPES.add(String.class.getSimpleName());
        SUPPORTED_TYPES.add(Float.class.getSimpleName());
        SUPPORTED_TYPES.add(Integer.class.getSimpleName());
        SUPPORTED_TYPES.add(Double.class.getSimpleName());
        SUPPORTED_TYPES.add(Long.class.getSimpleName());
    }
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
            }else if (!SUPPORTED_TYPES.contains(dataType)) {
                throw new ExecutionException("You have an error in your SQL syntax, the type '" + dataType + "' is not supported");
            }
        }
        return nativeDataType;
    }

}
