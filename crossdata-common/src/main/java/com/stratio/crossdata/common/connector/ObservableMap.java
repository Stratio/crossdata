package com.stratio.crossdata.common.connector;/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stratio.crossdata.common.data.FirstLevelName;
import com.stratio.crossdata.common.metadata.IMetadata;

public class ObservableMap<K extends FirstLevelName, V extends IMetadata>
        implements Map<K, V>, Serializable {

    private Map<FirstLevelName, IMetadata> innerMap = new LinkedHashMap<>();
    private List<IMetadataListener> listeners = new ArrayList<>();

    public void addListener(IMetadataListener listener){
        listeners.add(listener);
    }

    public void removeListener( IMetadataListener listener) {
        listeners.remove(listener);
    }

    @Override
    public int size() {
        return innerMap.size();
    }

    @Override
    public boolean isEmpty() {
        return innerMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return innerMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return innerMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return (V) innerMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        for(IMetadataListener listener: listeners){
            listener.updateMetadata(value);
        }
        return (V) innerMap.put(key, value);
    }

    @Override
    public V remove(Object key) {
        for(IMetadataListener listener: listeners){
            listener.deleteMetadata(innerMap.get(key));
        }
        return (V) innerMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(IMetadataListener listener: listeners){
            for(Entry<? extends K, ? extends V> entry: m.entrySet()){
                listener.updateMetadata(entry.getValue());
            }
        }
        innerMap.putAll(m);
    }

    @Override
    public void clear() {
        for(IMetadataListener listener: listeners){
            for(Entry<FirstLevelName, IMetadata> entry: innerMap.entrySet()){
                listener.deleteMetadata(entry.getValue());
            }
        }
        innerMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return (Set<K>) innerMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return (Collection<V>) innerMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K,V>> entrySet = new LinkedHashSet<>();
        for(Entry<FirstLevelName, IMetadata> entry: innerMap.entrySet()){
            ObservableEntry<FirstLevelName, IMetadata> observableEntry =
                    new ObservableEntry<>(entry.getKey(), entry.getValue());
            entrySet.add((Entry<K, V>) observableEntry);
        }
        return entrySet;
    }

}

class ObservableEntry<FirstLevelName, IMetadata> implements Map.Entry<FirstLevelName, IMetadata>{

    private final FirstLevelName key;
    private IMetadata value;

    ObservableEntry(FirstLevelName key, IMetadata value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public FirstLevelName getKey() {
        return key;
    }

    @Override
    public IMetadata getValue() {
        return value;
    }

    @Override
    public IMetadata setValue(IMetadata value) {
        IMetadata old = this.value;
        this.value = value;
        return old;
    }
}


