package qiyexue.node;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qiyexue.Utils.AclUtils;
import qiyexue.config.ZkConfig;
import qiyexue.watch.NodeWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ACL权限API测试
 *
 * @author 七夜雪
 * @create 2018-11-13 7:06
 */
public class AclTest {
    public static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;
    private final static Logger logger = LoggerFactory.getLogger(AclTest.class);
    static {
        try {
            zooKeeper = new ZooKeeper(ZkConfig.ZK_PATH, ZkConfig.TIMEOUT, new NodeWatcher());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 默认权限
     * @throws Exception
     */
    @Test
    public void testDefaultAcl() throws Exception {
        latch.await();
        Stat stat = new Stat();
        createNodeSync("/acl", "testAcl", ZooDefs.Ids.OPEN_ACL_UNSAFE);
        byte[] data = zooKeeper.getData("/acl", false, stat);
        logger.warn(new String(data));
        logger.warn(stat + "");
    }

    /**
     * 自定义用户权限认证
     */
    @Test
    public void testAclUserAuth() throws Exception {
        TimeUnit.SECONDS.sleep(10);
        Id id1 = new Id("digest", AclUtils.getDigestUserPwd("qiye1:123456"));
        Id id2 = new Id("digest", AclUtils.getDigestUserPwd("qiye2:123456"));
        List<ACL> acls = new ArrayList<>();
        acls.add(new ACL(ZooDefs.Perms.ALL, id1));
        acls.add(new ACL(ZooDefs.Perms.CREATE, id2));
        acls.add(new ACL(ZooDefs.Perms.DELETE, id2));
        Stat stat = new Stat();
        zooKeeper.addAuthInfo("digest", "qiye1:123456".getBytes());
        createNodeSync("/acl/ACL", "testAcl", acls);
        byte[] data = zooKeeper.getData("/acl/ACL", false, stat);
        int version = stat.getVersion();
        zooKeeper.addAuthInfo("digest", "qiye2:123456".getBytes());
        zooKeeper.setData("/acl/ACL", "qiyexue".getBytes(), version);
        System.out.println("------over-------");
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
    private void createNodeSync(String path, String value, List<ACL> acls) throws Exception {
        logCreate(path, value, CreateMode.PERSISTENT);
        zooKeeper.create(path, value.getBytes(), acls, CreateMode.PERSISTENT);
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
