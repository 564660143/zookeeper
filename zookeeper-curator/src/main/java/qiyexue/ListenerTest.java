package qiyexue;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.Test;
import qiyexue.config.ZkConfig;

import java.util.concurrent.TimeUnit;

/**
 * 监听端口测试
 *
 * @author 七夜雪
 * @create 2018-11-15 6:48
 */
public class ListenerTest {
    private static CuratorFramework client;
    static {
        RetryPolicy retryPolicy = new RetryOneTime(30000);
        client = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.ZK_PATH)
                .sessionTimeoutMs(ZkConfig.TIMEOUT)
                .namespace("curator")
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    /**
     * curator创建zk节点
     */
    @Test
    public void testClient1() throws Exception {
        System.out.println("连接状态 : " + client.getState());
        String nodePath = "/qiyexue";
        final NodeCache nodeCache = new NodeCache(client, nodePath);
        nodeCache.start();
        nodeCache.getListenable().addListener(() -> {
            System.out.println("客户端1监听到节点数据变更");
        });
        TimeUnit.SECONDS.sleep(10000);
    }

    /**
     * curator创建zk节点
     */
    @Test
    public void testClient2() throws Exception {
        System.out.println("连接状态 : " + client.getState());
        String nodePath = "/qiyexue";
        final NodeCache nodeCache = new NodeCache(client, nodePath);
        nodeCache.start();
        nodeCache.getListenable().addListener(() -> {
            System.out.println("客户端2监听到节点数据变更");
        });
        TimeUnit.SECONDS.sleep(10000);
    }

    /**
     * curator创建zk节点
     */
    @Test
    public void testClient3() throws Exception {
        System.out.println("连接状态 : " + client.getState());
        String nodePath = "/qiyexue";
        final NodeCache nodeCache = new NodeCache(client, nodePath);
        nodeCache.start();
        nodeCache.getListenable().addListener(() -> {
            System.out.println("客户端3监听到节点数据变更");
        });
        TimeUnit.SECONDS.sleep(10000);
    }

}
