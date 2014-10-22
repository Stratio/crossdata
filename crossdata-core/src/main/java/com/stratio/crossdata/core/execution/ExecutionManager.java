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

public enum ExecutionManager {
    MANAGER;

    private boolean isInit = false;

    private Map<String, Serializable> executionData;
    private Lock writeLock;
    private TransactionManager tm;

    private void shouldBeInit() {
        if (!isInit) {
            throw new ExecutionManagerException("Execution Data is not initialized yet.");
        }
    }

    public boolean exists(String key) {
        return executionData.containsKey(key);
    }

    private void shouldBeUnique(String key) {
        if (exists(key)) {
            throw new ExecutionManagerException("[" + key + "] already exists");
        }
    }

    private void shouldExist(String key) {
        if (!exists(key)) {
            throw new ExecutionManagerException("[" + key + "] doesn't exist yet");
        }
    }

    private void beginTransaction() throws SystemException, NotSupportedException {
        if (tm != null) {
            tm.begin();
        }
    }

    private void commitTransaction() throws HeuristicRollbackException, RollbackException,
            HeuristicMixedException, SystemException {
        if (tm != null) {
            tm.commit();
        }
    }

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
    public synchronized void clear()
            throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException,
            RollbackException {
        beginTransaction();
        executionData.clear();
        commitTransaction();
    }

    public void createEntry(String key, Serializable value) {
        createEntry(key, value, false);
    }

    public void createEntry(String key, Serializable value, boolean override) {
        shouldBeInit();
        try {
            writeLock.lock();
            if(!override){
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

    public void deleteEntry(String key) {
        shouldBeInit();
        try {
            beginTransaction();
            executionData.remove(key);
            commitTransaction();
        } catch (Exception ex) {
            throw new ExecutionManagerException(ex.getMessage(), ex.getCause());
        } finally {
            writeLock.unlock();
        }
    }

    public Serializable getValue(String key) {
        shouldBeInit();
        shouldExist(key);
        return executionData.get(key);
    }

}
