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

package com.stratio.crossdata.core.execution;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

/**
 * Execution manager in charge of storing execution related information in Infinispan.
 */
public enum ExecutionManager {

    /**
     * Execution manager Singleton.
     */
    MANAGER;

    /**
     * Whether the execution manager has been initialized.
     */
    private boolean isInit = false;

    /**
     * The map used to store execution related information.
     */
    private Map<String, Serializable> executionData;

    /**
     * Lock used to make write consistent.
     */
    private Lock writeLock;

    /**
     * Transaction manager used to perform writes.
     */
    private TransactionManager tm;

    /**
     * Check that the current execution manager has been initialized. If not an
     * {@link com.stratio.crossdata.core.execution.ExecutionManagerException} is thrown.
     */
    private void shouldBeInit() {
        if (!isInit) {
            throw new ExecutionManagerException("Execution Data is not initialized yet.");
        }
    }

    /**
     * Check whether an entry for a particular key has been stored.
     *
     * @param key The key.
     * @return Whether it exists or not.
     */
    public boolean exists(String key) {
        return executionData.containsKey(key);
    }

    /**
     * Check whether an entry for a particular key already exists on the map. If found,
     * a {@link com.stratio.crossdata.core.execution.ExecutionManagerException} is thrown.
     *
     * @param key They key.
     */
    private void shouldBeUnique(String key) {
        if (exists(key)) {
            throw new ExecutionManagerException("[" + key + "] already exists");
        }
    }

    /**
     * Check whether an entry for a particular key exists on the map. If not found,
     * a {@link com.stratio.crossdata.core.execution.ExecutionManagerException} is thrown.
     *
     * @param key They key.
     */
    private void shouldExist(String key) {
        if (!exists(key)) {
            throw new ExecutionManagerException("[" + key + "] doesn't exist yet");
        }
    }

    /**
     * Start a transaction using the associated transaction manager.
     *
     * @throws SystemException       If the transaction manager founds an error and it becomes unable to answer future
     *                               requests.
     * @throws NotSupportedException If the operation is not supported.
     */
    private void beginTransaction() throws SystemException, NotSupportedException {
        if (tm != null) {
            tm.begin();
        }
    }

    /**
     * Commit a transaction.
     *
     * @throws HeuristicRollbackException If the transaction manager decides to rollback the transaction.
     * @throws RollbackException          If the transaction is marked as rollback only.
     * @throws HeuristicMixedException    If the transaction has been partially commited due to the use of a heuristic.
     * @throws SystemException            If the transaction manager is not available.
     */
    private void commitTransaction() throws HeuristicRollbackException, RollbackException,
            HeuristicMixedException, SystemException {
        if (tm != null) {
            tm.commit();
        }
    }

    /**
     * Initialize the execution manager singleton.
     *
     * @param executionData The map used to persist the elements.
     * @param writeLock     The write lock.
     * @param tm            The transaction manager.
     */
    public synchronized void init(Map<String, Serializable> executionData, Lock writeLock, TransactionManager tm) {
        if (executionData != null && writeLock != null) {
            this.executionData = executionData;
            this.writeLock = writeLock;
            this.tm = tm;
            this.isInit = true;
        } else {
            throw new IllegalArgumentException("Any parameter can't be NULL");
        }
    }

    /**
     * Clear the current map.
     *
     * @throws SystemException            If the transaction manager is not available.
     * @throws NotSupportedException      If the operation is not supported.
     * @throws HeuristicRollbackException If the transaction manager decides to rollback the transaction.
     * @throws HeuristicMixedException    If the transaction has been partially committed due to the use of a heuristic.
     * @throws RollbackException          If the transaction is marked as rollback only.
     */
    public synchronized void clear()
            throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException,
            RollbackException {
        beginTransaction();
        executionData.clear();
        commitTransaction();
    }

    /**
     * Create an entry.
     *
     * @param key   The key.
     * @param value The value associated with a key.
     */
    public void createEntry(String key, Serializable value) {
        createEntry(key, value, false);
    }

    /**
     * Create an entry.
     *
     * @param key      The key.
     * @param value    The value associated with a key.
     * @param override Whether the value should be override.
     */
    public void createEntry(String key, Serializable value, boolean override) {
        shouldBeInit();
        try {
            writeLock.lock();
            if (!override) {
                shouldBeUnique(key);
            }
            beginTransaction();
            executionData.put(key, value);
            commitTransaction();
        } catch (Exception ex) {
            throw new ExecutionManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Delete an entry for the map.
     *
     * @param key The key.
     */
    public void deleteEntry(String key) {
        shouldBeInit();
        try {
            writeLock.lock();
            beginTransaction();
            executionData.remove(key);
            commitTransaction();
        } catch (Exception ex) {
            throw new ExecutionManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Get the value associated with a key.
     *
     * @param key The key.
     * @return The {@link java.io.Serializable} element associated with the key.
     */
    public Serializable getValue(String key) {
        shouldBeInit();
        shouldExist(key);
        return executionData.get(key);
    }

}
