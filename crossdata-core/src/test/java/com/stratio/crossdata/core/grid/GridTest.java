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

package com.stratio.crossdata.core.grid;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests {@link Grid}.
 */
@Test(testName = "GridTest")
public class GridTest {

    private String syncMessage;
    private String asyncMessage;

    /**
     * Starts the common {@link Grid} used by all its tests.
     */
    @BeforeClass
    public void setUp() {
        String path = "/tmp/com.stratio.crossdata-test-" + new Random().nextInt(100000);
        Grid.initializer().withPort(7810).withListenAddress("localhost").withPersistencePath(path).init();
    }

    /**
     * Stops the common {@link Grid} used by all its tests.
     */
    @AfterClass
    public void tearDown() {
        Grid.getInstance().close();
    }

    /**
     * Tests {@link Grid} distributed storing.
     */
    @Test
    public void testGridStore() throws Exception {
        Map<String, String> map = Grid.getInstance().map("testGridStore");
        TransactionManager tm = Grid.getInstance().transactionManager("testGridStore");
        tm.begin();
        Assert.assertNotNull(map);
        map.put("k1", "v1");
        Assert.assertEquals(map.get("k1"), "v1");
        map.remove("k1");
        Assert.assertNull(map.get("k1"));
        tm.commit();
    }

    /**
     * Tests {@link Grid} distributed locking.
     */
    @Test
    public void testGridLock() throws Exception {
        Lock lock = Grid.getInstance().lock("testGridLock");
        lock.lock();
        lock.unlock();
    }

    /**
     * Tests {@link Grid} distributed synchronous channeling.
     */
    @Test
    public void testGridSyncChannel() throws Exception {
        JChannel syncChannel = Grid.getInstance().channel("testGridSyncChannel");
        MessageDispatcher
                dispatcher =
                new MessageDispatcher(syncChannel, null, null, new RequestHandler() {
                    @Override
                    public Object handle(Message msg) throws Exception {
                        syncMessage = msg.getObject().toString();
                        return msg;
                    }
                });
        syncChannel.connect("test");
        dispatcher.castMessage(null, new Message(null, "hello"), RequestOptions.SYNC());
        Assert.assertEquals(syncMessage, "hello");
    }

    /**
     * Tests {@link Grid} distributed asynchronous channeling.
     */
    @Test
    public void testGridAsyncChannel() throws Exception {
        JChannel asyncChannel = Grid.getInstance().channel("testGridAsyncChannel");
        asyncChannel.setReceiver(new ReceiverAdapter() {
            public void receive(Message msg) {
                asyncMessage = msg.getObject().toString();
            }
        });
        asyncChannel.connect("test");
        asyncChannel.send(new Message(null, "hello"));
        Thread.sleep(2000);
        Assert.assertEquals(asyncMessage, "hello");
    }

}
