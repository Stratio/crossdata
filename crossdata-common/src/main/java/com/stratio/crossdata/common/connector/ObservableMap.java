/*
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
package com.stratio.crossdata.common.connector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stratio.crossdata.common.data.Name;
import com.stratio.crossdata.common.metadata.UpdatableMetadata;


/**
 * An implementation of Map where multiple listeners can be registered in order to send them automatically any change
 * in the metadata.
 *
 *  @param <K> The Name used as Key
 *  @param <V> The UpdatableMetadata used as value
 *
 */
public class ObservableMap<K extends Name, V extends UpdatableMetadata>
        implements Map<K, V>, Serializable {

    private static final long serialVersionUID = -5379923681871577907L;
    private Map<Name, UpdatableMetadata> innerMap = new LinkedHashMap<>();
    private List<IMetadataListener> listeners = new ArrayList<>();

    /**
     * Add a new {@link com.stratio.crossdata.common.connector.IMetadataListener}.
     * @param listener A @link com.stratio.crossdata.common.connector.IMetadataListener}.
     */
    public void addListener(IMetadataListener listener){
        listeners.add(listener);
    }

    /**
     * Remove the {@link com.stratio.crossdata.common.connector.IMetadataListener} if exists.
     * @param listener A @link com.stratio.crossdata.common.connector.IMetadataListener}.
     */
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
        if(! (key instanceof Name) ){
            throw new ClassCastException();
        }

        for(IMetadataListener listener: listeners){
            listener.deleteMetadata((Name) key);
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
            for(Entry<Name, UpdatableMetadata> entry: innerMap.entrySet()){
                listener.deleteMetadata(entry.getKey());
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
        for(Entry<Name, UpdatableMetadata> entry: innerMap.entrySet()){
            ObservableEntry<Name, UpdatableMetadata> observableEntry =
                    new ObservableEntry<>(entry.getKey(), entry.getValue());
            entrySet.add((Entry<K, V>) observableEntry);
        }
        return entrySet;
    }

}

class ObservableEntry<Name, UpdatableMetadata> implements Map.Entry<Name, UpdatableMetadata>{

    private final Name key;
    private UpdatableMetadata value;

    ObservableEntry(Name key, UpdatableMetadata value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Name getKey() {
        return key;
    }

    @Override
    public UpdatableMetadata getValue() {
        return value;
    }

    @Override
    public UpdatableMetadata setValue(UpdatableMetadata value) {
        UpdatableMetadata old = this.value;
        this.value = value;
        return old;
    }
}


