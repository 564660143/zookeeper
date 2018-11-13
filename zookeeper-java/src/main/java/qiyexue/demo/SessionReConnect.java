package qiyexue.demo;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qiyexue.config.ZkConfig;

import java.util.concurrent.TimeUnit;

/**
 * ZK Session恢复
 *
 * @author 七夜雪
 * @create 2018-11-12 8:18
 */
public class SessionReConnect {
    private final static Logger logger = LoggerFactory.getLogger(SessionReConnect.class);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(ZkConfig.ZK_PATH, ZkConfig.TIMEOUT, new ZkConnect());
        logger.warn("开始连接ZK, 连接状态: {}", zooKeeper.getState());
        TimeUnit.SECONDS.sleep(10);
        Long sessionId = zooKeeper.getSessionId();
        // SessionId转换
        String ssid = "0x" + Long.toHexString(sessionId);
        logger.warn("连接1状态: {}, SessionID: {}, SSID : {}", zooKeeper.getState(), sessionId, ssid);
        zooKeeper.close();
        ZooKeeper zk = new ZooKeeper(ZkConfig.ZK_PATH, ZkConfig.TIMEOUT, new ZkConnect(), sessionId, null);
        TimeUnit.SECONDS.sleep(10);
        logger.warn("连接2状态: {}, SessionID: {}", zk.getState(), zk.getSessionId());

    }

}
