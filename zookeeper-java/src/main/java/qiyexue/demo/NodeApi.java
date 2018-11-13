package qiyexue.demo;


import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qiyexue.config.ZkConfig;

import java.util.concurrent.TimeUnit;

/**
 * ZK 节点操作API
 *
 * @author 七夜雪
 * @create 2018-11-12 8:27
 */
public class NodeApi {
    public ZooKeeper zooKeeper;
    private final static Logger logger = LoggerFactory.getLogger(NodeApi.class);

    public NodeApi() {
        try {
            this.zooKeeper = new ZooKeeper(ZkConfig.ZK_PATH, ZkConfig.TIMEOUT, new ZkConnect());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 同步创建Zk节点
     * 同步或者异步创建节点，都不支持子节点的递归创建，异步有一个callback函数
     * 参数：
     * path：创建的路径
     * data：存储的数据的byte[]
     * acl：控制权限策略
     * 			Ids.OPEN_ACL_UNSAFE --> world:anyone:cdrwa
     * 			CREATOR_ALL_ACL --> auth:user:password:cdrwa
     * createMode：节点类型, 是一个枚举
     * 			PERSISTENT：持久节点
     * 			PERSISTENT_SEQUENTIAL：持久顺序节点
     * 			EPHEMERAL：临时节点
     * 			EPHEMERAL_SEQUENTIAL：临时顺序节点
     */
    private void createNodeSync(String path, String value, CreateMode createMode) throws Exception {
        logCreate(path, value, createMode);
        zooKeeper.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
    }

    /**
     * 异步创建节点
     * @param path
     * @param value
     * @param createMode
     * @throws Exception
     */
    private void createNodeAsync(String path, String value, CreateMode createMode, AsyncCallback.StringCallback callback, Object ctx) throws Exception {
        logCreate(path, value, createMode);
        zooKeeper.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode, callback, ctx);
    }


    public static void main(String[] args) throws Exception {
        NodeApi nodeApi = new NodeApi();
        // 确保zooKeeper连接上
        TimeUnit.SECONDS.sleep(10);
        // 创建临时有序节点
//        nodeApi.createNodeSync("/testNode1", "testNode1", CreateMode.EPHEMERAL_SEQUENTIAL);
        // 创建临时无序节点
//        nodeApi.createNodeSync("/testNode2", "testNode2", CreateMode.EPHEMERAL);
        // 创建持久有序节点
//        nodeApi.createNodeSync("/testNode3", "testNode3", CreateMode.PERSISTENT_SEQUENTIAL);
        // 创建持久无序节点
//        nodeApi.createNodeSync("/testNode4", "testNode4", CreateMode.PERSISTENT);
        // 异步创建节点
//        CallResult cr = new CallResult();
//        String ctx = "节点创建成功";
//        nodeApi.createNodeAsync("/testNode5", "testNode5", CreateMode.PERSISTENT, cr, ctx);
//        TimeUnit.SECONDS.sleep(2);
        nodeApi.createNodeSync("/testNode5", "testNode4", CreateMode.PERSISTENT);
        // 修改节点
        nodeApi.zooKeeper.setData("/testNode5", "qiyexue".getBytes(),2);
        // 删除节点
        nodeApi.zooKeeper.delete("/testNode5", 3);

    }


    private void logCreate(String path, String value, CreateMode createMode){
        if (createMode.isEphemeral() && createMode.isSequential()){
            logger.warn("创建临时有序节点: {}, value: {}", path, value);
        } else if (createMode.isEphemeral() && !createMode.isSequential()) {
            logger.warn("创建临时无序节点: {}, value: {}", path, value);
        } else if (!createMode.isEphemeral() && createMode.isSequential()) {
            logger.warn("创建持久有序节点: {}, value: {}", path, value);
        } else {
            logger.warn("创建持久无序节点: {}, value: {}", path, value);
        }
    }

}
