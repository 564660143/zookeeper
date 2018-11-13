package qiyexue;

import com.alibaba.fastjson.JSON;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import qiyexue.config.ZkConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * curator客户端操作zookeeper节点
 *
 * @author 七夜雪
 * @create 2018-11-13 21:16
 */
public class NodeApiTest {
    private static CuratorFramework client;

    static {
        /**
         * 同步创建zk示例，原生api是异步的
         *
         * curator链接zookeeper的策略:ExponentialBackoffRetry
         * baseSleepTimeMs：初始sleep的时间
         * maxRetries：最大重试次数
         * maxSleepMs：最大重试时间
         */
//		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        /**
         * curator链接zookeeper的策略:RetryNTimes
         * n：重试的次数
         * sleepMsBetweenRetries：每次重试间隔的时间
         */
//        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);

        /**
         * curator链接zookeeper的策略:RetryOneTime
         * sleepMsBetweenRetry:每次重试间隔的时间
         */
        RetryPolicy retryPolicy2 = new RetryOneTime(30000);

        /**
         * 永远重试，不推荐使用
         */
//		RetryPolicy retryPolicy3 = new RetryForever(retryIntervalMs)

        /**
         * curator链接zookeeper的策略:RetryUntilElapsed
         * maxElapsedTimeMs:最大重试时间
         * sleepMsBetweenRetries:每次重试间隔
         * 重试时间超过maxElapsedTimeMs后，就不再重试
         */
//		RetryPolicy retryPolicy4 = new RetryUntilElapsed(2000, 3000);
        // 创建客户端连接, 链式编程
        client = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.ZK_PATH)
                .sessionTimeoutMs(ZkConfig.TIMEOUT)
                .namespace("curator")
                .retryPolicy(retryPolicy2)
                .build();
        client.start();
    }

    /**
     * curator创建zk节点
     */
    @Test
    public void testCreateNode() throws Exception {
        System.out.println("连接状态 : " + client.getState());
        String nodePath = "/qiyexue/biluo";
        byte[] data = "qiyexue".getBytes();
        // 这个表示递归创建, 如果没有父节点就创建
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(nodePath, data);
    }

    /**
     * curator删除zk节点
     */
    @Test
    public void testDeleteNode() throws Exception {
        System.out.println("连接状态 : " + client.getState());
        String nodePath = "/qiyexue/biluo";
        byte[] data = "qiyexue".getBytes();
        // deletingChildrenIfNeeded : 如果会将该节点下的子节点都删除
        // guaranteed : 如果删除失败，那么在后端还是继续会删除，直到成功
        client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(0).forPath(nodePath);
    }

    /**
     * 获取数据
     * @throws Exception
     */
    @Test
    public void testGetData() throws Exception {
        String nodePath = "/qiyexue/biluo";
        byte[] data = "qiyexue".getBytes();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(nodePath, data);
        byte[] bytes = client.getData().forPath(nodePath);
        System.out.println("data : " + new String(bytes));
        Stat stat = new Stat();
        // 获取节点信息, 同时获取stat
        byte[] bytes1 = client.getData().storingStatIn(stat).forPath(nodePath);
        System.out.println("data : " + new String(bytes1) + ", stat :" + JSON.toJSONString(stat, true));
    }


    /**
     * 获取子节点列表
     * @throws Exception
     */
    @Test
    public void testGetChildren() throws Exception {
        String nodePath = "/qiyexue";
        List<String> childList = client.getChildren().forPath(nodePath);
        for (String s : childList) {
            System.out.println(s);
        }
    }

    /**
     * 判断节点是否存在
     * @throws Exception
     */
    @Test
    public void testExist() throws Exception {
        String nodePath = "/qiyexue";
        Stat stat = client.checkExists().forPath(nodePath);
        System.out.println(stat == null ? "节点不存在" : JSON.toJSONString(stat, true));
        nodePath = "/qiyexue11";
        stat = client.checkExists().forPath(nodePath);
        System.out.println(stat == null ? "节点不存在" : JSON.toJSONString(stat, true));
    }

}
