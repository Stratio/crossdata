package com.stratio.meta.core.metadata;

import com.stratio.meta.core.structures.IndexType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomIndexMetadataTest {

    @Test
    public void getIndexType(){
        CustomIndexMetadata cim1 = new CustomIndexMetadata(null, IndexType.DEFAULT);
        CustomIndexMetadata cim2 = new CustomIndexMetadata(null, IndexType.LUCENE);
        assertEquals("Invalid index type", IndexType.DEFAULT, cim1.getIndexType());
        assertEquals("Invalid index type", IndexType.LUCENE, cim2.getIndexType());
    }

}
