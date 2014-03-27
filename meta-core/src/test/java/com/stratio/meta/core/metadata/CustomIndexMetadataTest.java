/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.metadata;

import com.stratio.meta.core.structures.IndexType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CustomIndexMetadataTest {

    @Test
    public void getIndexType(){
        CustomIndexMetadata cim1 = new CustomIndexMetadata(null, "index_name", IndexType.DEFAULT);
        CustomIndexMetadata cim2 = new CustomIndexMetadata(null, "stratio_lucene_index", IndexType.LUCENE);
        assertEquals(IndexType.DEFAULT, cim1.getIndexType(), "Invalid index type");
        assertEquals(IndexType.LUCENE, cim2.getIndexType(), "Invalid index type");
    }

}
