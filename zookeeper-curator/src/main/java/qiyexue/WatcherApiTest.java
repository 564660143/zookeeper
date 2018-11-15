package qiyexue;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryOneTime;
import org.junit.Test;
import qiyexue.config.ZkConfig;
import qiyexue.watcher.MyCuratorWatcher;
import qiyexue.watcher.MyWatcher;

import java.util.concurrent.TimeUnit;

/**
 * Watcher相关API
 *
 * @author 七夜雪
 * @create 2018-11-14 7:51
 */
public class WatcherApiTest {
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
     * 默认Watcher测试
     */
    @Test
    public void testWatcher() throws Exception {
        System.out.println("连接状态 :" + client.getState());
        client.getData().usingWatcher(new MyWatcher()).forPath("/qiyexue");
        TimeUnit.SECONDS.sleep(1000);
    }

    /**
     * curator Watcher测试
     */
    @Test
    public void testCuratorWatcher() throws Exception {
        System.out.println("连接状态 :" + client.getState());
        client.getData().usingWatcher(new MyCuratorWatcher()).forPath("/qiyexue");
        TimeUnit.SECONDS.sleep(1000);
    }

    /**
     * curator Watcher子节点监听测试
     */
    @Test
    public void testCuratorWatcherChildren() throws Exception {
        System.out.println("连接状态 :" + client.getState());
        client.getChildren().usingWatcher(new MyCuratorWatcher()).forPath("/qiyexue");
        TimeUnit.SECONDS.sleep(1000);
    }


    /**
     * curator NodeCache测试
     */
    @Test
    public void testNodeCache() throws Exception {
        System.out.println("连接状态 :" + client.getState());
        String path = "/qiyexue";
        final NodeCache nodeCache = new NodeCache(client, path);
        nodeCache.start(true);
        if (nodeCache.getCurrentData() != null) {
            System.out.println("节点初始化数据为：" + new String(nodeCache.getCurrentData().getData()));
        } else {
            System.out.println("节点初始化数据为空...");
        }
        nodeCache.getListenable().addListener(() -> {
            if (nodeCache.getCurrentData() == null) {
                System.out.println("数据为空");
            }
            ChildData currentData = nodeCache.getCurrentData();
            String data = new String(currentData.getData());
            System.out.println("节点路径: " + nodeCache.getCurrentData().getPath() + " 节点数据 :" + data);
        });

        TimeUnit.SECONDS.sleep(1000);
    }

}
