package com.stratio.meta.server.metadata;

import com.stratio.meta.core.metadata.IndexType;
import com.stratio.meta.core.metadata.IndexType;
import org.junit.Test;

public class CustomIndexMetadataTest {

    @Test
    public void getIndexType(){
        IndexType [] types = {
                IndexType.COMPOSITES,
                IndexType.COMPOSITES,
                IndexType.COMPOSITES,
                IndexType.CUSTOM};

        String [] columnNames = {
                "age",
                "bool",
                "gender",
                "lucene"};



    }


}
