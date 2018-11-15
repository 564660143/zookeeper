package qiyexue.Utils;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;

/**
 * ACL工具类
 *
 * @author 七夜雪
 * @create 2018-11-13 8:13
 */
public class AclUtils {
    public static String getDigestUserPwd(String id) throws Exception {
        return DigestAuthenticationProvider.generateDigest(id);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException, Exception {
        String id = "imooc:imooc";
        String idDigested = getDigestUserPwd(id);
        System.out.println(idDigested);
    }

}
