package com.stratio.meta2.core.grid;

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

import java.util.Random;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

/**
 * Tests {@link Grid}.
 */

@Test(testName = "GridTest")
public class GridTest {

  private Grid grid;

  private String syncMessage;
  private String asyncMessage;

  /**
   * Starts the common {@link Grid} used by all its tests.
   */
  @BeforeClass
  public void setUp() {
    String path = "/tmp/meta-test-" + new Random().nextInt(100000);
    grid = new GridBuilder().withListenAddress("localhost", 7810).withPersistencePath(path).build();
  }

  /**
   * Stops the common {@link Grid} used by all its tests.
   */
  @AfterClass
  public void tearDown() {
    grid.close();
  }

  /**
   * Tests {@link Grid} distributed storing.
   */
  @Test(testName = "testGridStore")
  public void testGridStore() throws Exception {
    Store store = grid.store("test");
    TransactionManager tm = store.transactionManager();
    tm.begin();
    Assert.assertNotNull(store);
    store.put("k1", "v1");
    Assert.assertEquals(store.get("k1"), "v1");
    store.remove("k1");
    Assert.assertNull(store.get("k1"));
    tm.commit();
  }

  /**
   * Tests {@link Grid} distributed locking.
   */
  @Test(testName = "testGridLock")
  public void testGridLock() throws Exception {
    Lock lock = grid.lock("test");
    lock.lock();
    lock.unlock();
  }

  /**
   * Tests {@link Grid} distributed synchronous channeling.
   */
  @Test(testName = "testGridSyncChannel")
  public void testGridSyncChannel() throws Exception {
    JChannel syncChannel = grid.channel("test");
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
    dispatcher.castMessage(null, new Message(null, new String("hello")), RequestOptions.SYNC());
    Assert.assertEquals(syncMessage, "hello");
  }

  /**
   * Tests {@link Grid} distributed asynchronous channeling.
   */
  @Test(testName = "testGridAsyncChannel")
  public void testGridAsyncChannel() throws Exception {
    JChannel asyncChannel = grid.channel("test_async");
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
