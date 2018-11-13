package qiyexue.config;

import org.omg.CORBA.TIMEOUT;

/**
 * @author 七夜雪
 * @create 2018-11-12 7:57
 */
public interface ZkConfig {
    public static final String ZK_PATH = "192.168.72.135:2181,192.168.72.136:2181,192.168.72.137:2181";
    public static final int TIMEOUT = 30000;

}
