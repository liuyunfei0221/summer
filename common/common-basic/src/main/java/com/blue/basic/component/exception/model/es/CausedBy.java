package com.blue.basic.component.exception.model.es;

import java.io.Serializable;

/**
 * es resp model
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CausedBy implements Serializable {

    private static final long serialVersionUID = 7738333756305916581L;

    private String type;
    private String reason;
    private CausedBy caused_by;

    public CausedBy() {
    }

    public CausedBy(String type, String reason, CausedBy caused_by) {
        this.type = type;
        this.reason = reason;
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

    public CausedBy getCaused_by() {
        return caused_by;
    }

    public void setCaused_by(CausedBy caused_by) {
        this.caused_by = caused_by;
    }

    @Override
    public String toString() {
        return "CausedBy{" +
                "type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", caused_by=" + caused_by +
                '}';
    }

}
