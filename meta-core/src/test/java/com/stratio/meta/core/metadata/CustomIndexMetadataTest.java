package com.stratio.meta.core.metadata;

import com.stratio.meta.core.structures.IndexType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CustomIndexMetadataTest {

    @Test
    public void getIndexType(){
        CustomIndexMetadata cim1 = new CustomIndexMetadata(null, IndexType.DEFAULT);
        CustomIndexMetadata cim2 = new CustomIndexMetadata(null, IndexType.LUCENE);
        assertEquals(IndexType.DEFAULT, cim1.getIndexType(), "Invalid index type");
        assertEquals(IndexType.LUCENE, cim2.getIndexType(), "Invalid index type");
    }

}
