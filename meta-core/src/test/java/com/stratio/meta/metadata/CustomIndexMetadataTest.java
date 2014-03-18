package com.stratio.meta.metadata;

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
