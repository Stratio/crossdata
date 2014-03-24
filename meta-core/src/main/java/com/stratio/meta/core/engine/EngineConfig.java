package com.stratio.meta.core.engine;

public class EngineConfig {
    private String [] cassandraHosts;
    private int cassandraPort;

    public String[] getCassandraHosts() {
        return cassandraHosts;
    }

    public void setCassandraHosts(String[] cassandraHosts) {
        this.cassandraHosts = cassandraHosts;
    }

    public int getCassandraPort() {
        return cassandraPort;
    }

    public void setCassandraPort(int cassandraPort) {
        this.cassandraPort = cassandraPort;
    }
}
