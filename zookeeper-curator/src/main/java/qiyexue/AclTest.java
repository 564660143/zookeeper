package qiyexue;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.Test;
import qiyexue.Utils.AclUtils;
import qiyexue.config.ZkConfig;

import java.security.acl.Acl;
import java.util.ArrayList;
import java.util.List;


/**
 * cutator操作zookeeper权限管理
 *
 * @author 七夜雪
 * @create 2018-11-14 21:47
 */
public class AclTest {

    private static CuratorFramework client;
    static {
        RetryPolicy retryPolicy = new RetryOneTime(30000);
        client = CuratorFrameworkFactory.builder()
                // 构建客户端时认证
                .authorization("digest", "qiyexue:123456".getBytes())
                .connectString(ZkConfig.ZK_PATH)
                .sessionTimeoutMs(ZkConfig.TIMEOUT)
                .namespace("curator")
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    @Test
    public void  testAcl() throws Exception {
//        ACL acl = new ACL(ZooDefs.Perms.ALL, ZooDefs.Ids.ANYONE_ID_UNSAFE);
        Id id1 = new Id("digest", AclUtils.getDigestUserPwd("qiyexue:123456"));
        Id id2 = new Id("digest", AclUtils.getDigestUserPwd("qiye:123456"));
        ACL acl1 = new ACL(ZooDefs.Perms.CREATE, id1);
        ACL acl2 = new ACL(ZooDefs.Perms.WRITE, id1);
        ACL acl3 = new ACL(ZooDefs.Perms.DELETE, id2);
        List<ACL> acls = new ArrayList<>();
        acls.add(acl1);
        acls.add(acl2);
        acls.add(acl3);
        String nodePath = "/qiyexue/acl";
        // 创建节点
        client.create().creatingParentsIfNeeded().withACL(acls).forPath(nodePath, "acltest".getBytes());
        // 更新数据
        client.setData().forPath(nodePath, "acl".getBytes());
        // 删除
        client.delete().forPath(nodePath);
    }

}
