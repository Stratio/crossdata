/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.connector;

import com.stratio.crossdata.common.data.Name;
import com.stratio.crossdata.common.metadata.UpdatableMetadata;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * Created by lcisneros on 4/05/15.
 */
public class ObservableMapTest {

    @Test
    public void mustPutANewValue(){
        ObservableMap observableMap = new ObservableMap();
        IMetadataListener listener = mock(IMetadataListener.class);
        IMetadataListener listener2 = mock(IMetadataListener.class);

        UpdatableMetadata value = mock(UpdatableMetadata.class);
        Name key = mock(Name.class);

        observableMap.addListener(listener);
        observableMap.addListener(listener2);

        //Experimentation
        UpdatableMetadata oldValue = observableMap.put(key, value);

        //Expectations
        Mockito.verify(listener).updateMetadata(value);
        Mockito.verify(listener2).updateMetadata(value);
        Assert.assertNull(oldValue);
    }


    @Test
    public void shouldRemoveAValue(){
        ObservableMap observableMap = new ObservableMap();
        IMetadataListener listener = mock(IMetadataListener.class);
        UpdatableMetadata value = mock(UpdatableMetadata.class);
        Name key = mock(Name.class);
        observableMap.addListener(listener);
        observableMap.put(key, value);

        //Experimentation
        UpdatableMetadata oldValue = observableMap.remove(key);

        //Expectations
        Mockito.verify(listener).deleteMetadata(key);
        Assert.assertEquals(value, oldValue);
    }


    @SuppressWarnings("PMD.JUnitTestShouldIncludeAssert")
    @Test(expectedExceptions = ClassCastException.class)
    public void shouldThrowsAnExceñtionWhenRemoveAValueWithInvalidKey(){
        ObservableMap observableMap = new ObservableMap();

        //Experimentation
        observableMap.remove("Bad parameter");
    }

    @SuppressWarnings("PMD.JUnitTestShouldIncludeAssert")
    @Test
    public void mustPutALotOfNewValues(){
        ObservableMap observableMap = new ObservableMap();
        IMetadataListener listener = mock(IMetadataListener.class);
        observableMap.addListener(listener);

        Map<Name,UpdatableMetadata > values = new LinkedHashMap<>();

        Name name1 = mock(Name.class);
        Name name2 = mock(Name.class);

        values.put(name1,mock(UpdatableMetadata.class));
        values.put(name2,mock(UpdatableMetadata.class));

        //Experimentation
        observableMap.putAll(values);

        //Expectations
        Mockito.verify(listener).updateMetadata(values.get(name1));
        Mockito.verify(listener).updateMetadata(values.get(name2));
    }

    @SuppressWarnings("PMD.JUnitTestShouldIncludeAssert")
    @Test
    public void shouldClearTheMap(){
        ObservableMap observableMap = new ObservableMap();

        Name key = mock(Name.class), key2=mock(Name.class);
        observableMap.put(key, mock(UpdatableMetadata.class));
        observableMap.put(key2, mock(UpdatableMetadata.class));

        IMetadataListener listener = mock(IMetadataListener.class);
        IMetadataListener listener2 = mock(IMetadataListener.class);
        observableMap.addListener(listener);
        observableMap.addListener(listener2);

        //Experimentation
        observableMap.clear();

        //Expectations
        Mockito.verify(listener).deleteMetadata(key);
        Mockito.verify(listener2).deleteMetadata(key);

        Mockito.verify(listener).deleteMetadata(key2);
        Mockito.verify(listener2).deleteMetadata(key2);
    }
}
