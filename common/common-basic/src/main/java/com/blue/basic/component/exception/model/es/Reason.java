package com.blue.basic.component.exception.model.es;

import java.io.Serializable;

/**
 * es resp model
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Reason implements Serializable {

    private static final long serialVersionUID = -1908779687581621116L;

    private String type;
    private String reason;
    private String index_uuid;
    private String shard;
    private String index;
    private CausedBy caused_by;

    public Reason() {
    }

    public Reason(String type, String reason, String index_uuid, String shard, String index, CausedBy caused_by) {
        this.type = type;
        this.reason = reason;
        this.index_uuid = index_uuid;
        this.shard = shard;
        this.index = index;
        this.caused_by = caused_by;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIndex_uuid() {
        return index_uuid;
    }

    public void setIndex_uuid(String index_uuid) {
        this.index_uuid = index_uuid;
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

    public CausedBy getCaused_by() {
        return caused_by;
    }

    public void setCaused_by(CausedBy caused_by) {
        this.caused_by = caused_by;
    }

    @Override
    public String toString() {
        return "Reason{" +
                "type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", index_uuid='" + index_uuid + '\'' +
                ", shard='" + shard + '\'' +
                ", index='" + index + '\'' +
                ", caused_by=" + caused_by +
                '}';
    }

}
