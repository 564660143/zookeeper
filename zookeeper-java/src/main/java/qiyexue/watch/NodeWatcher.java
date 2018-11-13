package qiyexue.watch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import qiyexue.node.GetNodeData;

/**
 * 节点Watcher
 *
 * @author 七夜雪
 * @create 2018-11-12 21:00
 */
public class NodeWatcher implements Watcher {

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
        GetNodeData.latch2.countDown();
        System.out.println(event.getState());
    }
}
