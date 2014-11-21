package com.stratio.crossdata.common.metadata;

import com.stratio.crossdata.common.data.NodeName;
import com.stratio.crossdata.common.data.Status;

public class NodeMetadata implements IMetadata {

    /**
     * Connector name.
     */
    private final NodeName name;

    /**
     * The Node status.
     */
    private Status status;

    public NodeMetadata(NodeName name) {
        this(name, Status.OFFLINE);
    }

    public NodeMetadata(NodeName name, Status status) {
        this.name = name;
        this.status = status;
    }

    public NodeName getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
