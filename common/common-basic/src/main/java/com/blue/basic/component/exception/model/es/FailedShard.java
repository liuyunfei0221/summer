package com.blue.basic.component.exception.model.es;

import java.io.Serializable;

/**
 * es resp model
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class FailedShard implements Serializable {

    private static final long serialVersionUID = -2841727077988146379L;

    private String shard;
    private String index;
    private String node;
    private Reason reason;

    public FailedShard() {
    }

    public FailedShard(String shard, String index, String node, Reason reason) {
        this.shard = shard;
        this.index = index;
        this.node = node;
        this.reason = reason;
    }

    public String getShard() {
        return shard;
    }

    public void setShard(String shard) {
        this.shard = shard;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "FailedShard{" +
                "shard='" + shard + '\'' +
                ", index='" + index + '\'' +
                ", node='" + node + '\'' +
                ", reason=" + reason +
                '}';
    }
}