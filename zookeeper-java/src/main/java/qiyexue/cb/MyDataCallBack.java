package qiyexue.cb;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

/**
 * 回调接口
 *
 * @author 七夜雪
 * @create 2018-11-12 21:29
 */
public class MyDataCallBack implements AsyncCallback.DataCallback {

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println("rc : " + rc);
        System.out.println("path : " + path);
        System.out.println("ctx : " + ctx);
        System.out.println("data : " + new String(data));
        System.out.println("stat : " + stat);
    }
}
