package qiyexue.demo;

import org.apache.zookeeper.AsyncCallback;


/**
 * ZK 异步创建节点回调类
 *
 * @author 七夜雪
 * @create 2018-11-12 9:00
 */
public class CallResult implements AsyncCallback.StringCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("创建节点 : " + path);
        System.out.println(ctx);
    }
}
