package qiyexue.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * zk默认Watcher
 *
 * @author 七夜雪
 * @create 2018-11-14 7:55
 */
public class MyWatcher implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        if (Event.EventType.NodeCreated.equals(event.getType())) {
            System.out.println("节点创建 :" + event.getPath());
        } else if (Event.EventType.NodeDeleted.equals(event.getType())) {
            System.out.println("节点删除 :" + event.getPath());
        } else if (Event.EventType.NodeDataChanged.equals(event.getType())){
            System.out.println("节点数据修改" + event.getPath());
        } else if (Event.EventType.NodeChildrenChanged.equals(event.getType())) {
            System.out.println("子节点数据变更" + event.getPath());
        } else {
            System.out.println("其他事件 : " + event);
        }
    }
}
