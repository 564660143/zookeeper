package qiyexue.watcher;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * curator watcher
 *
 * @author 七夜雪
 * @create 2018-11-14 8:01
 */
public class MyCuratorWatcher implements CuratorWatcher {

    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        if (Watcher.Event.EventType.NodeCreated.equals(watchedEvent.getType())) {
            System.out.println("节点创建 :" + watchedEvent.getPath());
        } else if (Watcher.Event.EventType.NodeDeleted.equals(watchedEvent.getType())) {
            System.out.println("节点删除 :" + watchedEvent.getPath());
        } else if (Watcher.Event.EventType.NodeDataChanged.equals(watchedEvent.getType())){
            System.out.println("节点数据修改" + watchedEvent.getPath());
        } else if (Watcher.Event.EventType.NodeChildrenChanged.equals(watchedEvent.getType())) {
            System.out.println("子节点数据变更" + watchedEvent.getPath());
        } else {
            System.out.println("其他事件 : " + watchedEvent);
        }
    }
}
