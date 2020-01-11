package org.swiftboot.service.config;

/**
 * swiftboot.service.redis
 *
 * @author swiftech 2019-06-06
 **/
public class RedisConfigBean {

    private String host = "localhost";

    private int port = 6379;

    private String cluster = "localhost:6379";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
}
