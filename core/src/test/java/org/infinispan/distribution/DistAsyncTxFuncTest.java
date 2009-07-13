package org.infinispan.distribution;

//@Test(groups = "functional", testName = "distribution.DistAsyncTxFuncTest", enabled = false)
public class DistAsyncTxFuncTest extends DistSyncTxFuncTest {

//   ReplListener r1, r2, r3, r4;
//   ReplListener[] r;
//   Map<Cache<?, ?>, ReplListener> listenerLookup;
//
//   public DistAsyncTxFuncTest() {
//      sync = false;
//      tx = true;
//      testRetVals = true;
//      cleanup = CleanupPhase.AFTER_METHOD; // ensure any stale TXs are wiped
//   }
//
//   @Override
//   protected void createCacheManagers() throws Throwable {
//      super.createCacheManagers();
//      r1 = new ReplListener(c1, true, true);
//      r2 = new ReplListener(c2, true, true);
//      r3 = new ReplListener(c3, true, true);
//      r4 = new ReplListener(c4, true, true);
//      r = new ReplListener[]{r1, r2, r3, r4};
//      listenerLookup = new HashMap<Cache<?, ?>, ReplListener>();
//      for (ReplListener rl : r) listenerLookup.put(rl.getCache(), rl);
//   }
//
//   @Override
//   protected void asyncWait(Object key, Class<? extends VisitableCommand> command, Cache<?, ?>... cachesOnWhichKeyShouldInval) {
//      if (cachesOnWhichKeyShouldInval == null) cachesOnWhichKeyShouldInval = new Cache[0];
//      List<Cache<?, ?>> cachesOnWhichKeyShouldInvalList = new ArrayList(Arrays.asList(cachesOnWhichKeyShouldInval));
//      if (key == null) {
//         // test all caches.
//         for (ReplListener rl : r) rl.expect(command);
//         for (ReplListener rl : r) rl.waitForRpc();
//      } else {
//         for (Cache<?, ?> c : getOwners(key)) {
//            if (cachesOnWhichKeyShouldInvalList.remove(c)) {
//               listenerLookup.get(c).expect(command, InvalidateL1Command.class);
//            } else {
//               listenerLookup.get(c).expect(command);
//            }
//            listenerLookup.get(c).waitForRpc();
//         }
//
//         for (Cache<?, ?> c : cachesOnWhichKeyShouldInvalList) {
//            listenerLookup.get(c).expect(InvalidateL1Command.class);
//            listenerLookup.get(c).waitForRpc();
//         }
//      }
//   }
//
//   @Override
//   protected void asyncTxWait(Object... keys) {
//      // Wait for a tx completion event
//      if (keys != null) {
//         Set<Cache<?, ?>> cachesInTx = new HashSet<Cache<?, ?>>();
//         for (Object k : keys) {
//            cachesInTx.addAll(Arrays.asList(getOwners(k)));
//         }
//
//         for (Cache<?, ?> c : cachesInTx) {
//            listenerLookup.get(c).expectAnyWithTx();
//            listenerLookup.get(c).waitForRpc();
//         }
//      }
//   }
}