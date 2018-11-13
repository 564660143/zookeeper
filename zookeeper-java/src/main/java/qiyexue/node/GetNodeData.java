package qiyexue.node;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import qiyexue.cb.MyDataCallBack;
import qiyexue.config.ZkConfig;
import qiyexue.watch.NodeWatcher;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ZK获取节点数据
 *
 * @author 七夜雪
 * @create 2018-11-12 20:45
 */
public class GetNodeData implements Watcher {
    public static CountDownLatch latch = new CountDownLatch(1);
    public static CountDownLatch latch2 = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;
    static {
        try {
            zooKeeper = new ZooKeeper(ZkConfig.ZK_PATH, ZkConfig.TIMEOUT, new GetNodeData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取父节点数据
     * @throws Exception
     */
    @Test
    public void testGetData() throws Exception {
        latch.await();
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/qiyexue", false, stat);
        System.out.println(new String(data));
        System.out.println(stat);
    }

    @Test
    public void testGetData2() throws Exception {
        latch.await();
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/qiyexue", false, stat);
        System.out.println(new String(data));
        System.out.println(stat);
    }

    @Test
    public void testGetData3() throws Exception {
        latch.await();
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/qiyexue", new NodeWatcher(), stat);
        System.out.println(new String(data));
        System.out.println(stat);
        latch2.await();
    }

    @Test
    public void testGetDataAsync() throws Exception {
        latch.await();
        Stat stat = new Stat();
        String ctx = "执行成功";
        zooKeeper.getData("/qiyexue", new NodeWatcher(), new MyDataCallBack(), ctx);
        System.out.println(stat);
        latch2.await();
    }

    @Test
    public void testGetChild() throws Exception {
        latch.await();
        List<String> children = zooKeeper.getChildren("/qiyexue", false);
        for (String child : children) {
            System.out.println(child);
        }
    }

    @Test
    public void testGetChild2() throws Exception {
        latch.await();
        List<String> children = zooKeeper.getChildren("/qiyexue", new NodeWatcher());
        for (String child : children) {
            System.out.println(child);
        }
        latch2.await();
    }

    @Test
    public void testNodeExist() throws Exception {
        latch.await();
        Stat exists = zooKeeper.exists("/qiyexue", true);
        zooKeeper.register(new NodeWatcher());
        System.out.println(exists);
        latch2.await();
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("触发事件" + event);
        latch.countDown();
    }

}
