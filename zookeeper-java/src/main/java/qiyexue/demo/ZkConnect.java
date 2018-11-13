package qiyexue.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qiyexue.config.ZkConfig;

import java.util.concurrent.TimeUnit;


/**
 * Zk客户端连接
 *
 * @author 七夜雪
 * @create 2018-11-12 7:45
 */
public class ZkConnect implements Watcher {
    private final static Logger logger = LoggerFactory.getLogger(ZkConnect.class);

    @Override
    public void process(WatchedEvent event) {
        logger.warn("触发event事件:{}", event);
    }

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(ZkConfig.ZK_PATH, ZkConfig.TIMEOUT, new ZkConnect());
        logger.warn("开始连接ZK, 连接状态: {}", zooKeeper.getState());
        TimeUnit.SECONDS.sleep(10);
        logger.warn("开始连接ZK, 连接状态: {}", zooKeeper.getState());
    }

}
