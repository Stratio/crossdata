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

package com.stratio.meta2.core.grid;

import org.infinispan.AdvancedCache;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

/**
 * Class representing a key-value store with a transaction manager.
 */
public class Store {

  public AdvancedCache<String, String> advancedCache;
  public TransactionManager transactionManager;

  /**
   * Builds a new store using the specified {@link org.infinispan.AdvancedCache} as basis.
   * @param advancedCache the cache to be used as basis.
   */
  Store(AdvancedCache<String, String> advancedCache) {
    this.advancedCache = advancedCache;
    this.transactionManager = advancedCache.getTransactionManager();
  }

  /**
   * Associates the specified value with the specified key in this store.
   *
   * @param key key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return this
   */
  public Store put(String key, String value) {
    advancedCache.put(key, value);
    return this;
  }

  /**
   * Returns the value to which the specified key is mapped,
   * or {@code null} if this map contains no mapping for the key.
   *
   * @param key the key whose associated value is to be returned.
   * @return the value to which the specified key is mapped, or
   *         {@code null} if this map contains no mapping for the key.
   */
  public String get(String key) {
    return advancedCache.get(key);
  }

  /**
   * Returns {@code true} if this store contains a value for the specified
   * key.
   *
   * @param key key whose presence in this store is to be checked
   * @return <tt>true</tt> if this map contains a mapping for the specified
   *         key
   */
  public boolean containsKey(String key) {
    return advancedCache.containsKey(key);
  }

  /**
   * Removes the value for a key from this store.
   *
   * @param key key whose value is to be removed from the store
   * @return this
   */
  public Store remove(String key) {
    advancedCache.remove(key);
    return this;
  }

  /**
   * Returns a {@link Set} view of the mappings contained in this store.
   * @return a {@link Set} view of the mappings contained in this store.
   */
  public Set<Entry<String, String>> entrySet() {
    return advancedCache.entrySet();
  }

  /**
   * Returns a {@link Set} view of the keys contained in this store.
   * @return a {@link Set} view of the keys contained in this store.
   */
  public Set<String> keySet() {
    return advancedCache.keySet();
  }

  /**
   * Returns a {@link Collection} view of the values contained in this store.
   * @return a {@link Collection} view of the values contained in this store.
   */
  public Collection<String> valueSet() {
    return advancedCache.values();
  }

  /**
   * Returns a new {@link javax.transaction.TransactionManager} for this store.
   * @return a new {@link javax.transaction.TransactionManager} for this store.
   * @throws SystemException
   */
  public TransactionManager transaction() {
    return transactionManager;
  }

}
