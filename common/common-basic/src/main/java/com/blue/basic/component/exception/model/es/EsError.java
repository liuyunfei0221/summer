package com.blue.basic.component.exception.model.es;

import java.io.Serializable;
import java.util.List;

/**
 * es resp model
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EsError implements Serializable {

    private static final long serialVersionUID = 2104808694482246097L;

    private String type;
    private String reason;
    private String phase;
    private String grouped;
    private CausedBy caused_by;
    private List<Reason> root_cause;
    private List<FailedShard> failed_shards;

    public EsError() {
    }

    public EsError(String type, String reason, String phase, String grouped, CausedBy caused_by, List<Reason> root_cause, List<FailedShard> failed_shards) {
        this.type = type;
        this.reason = reason;
        this.phase = phase;
        this.grouped = grouped;
        this.caused_by = caused_by;
        this.root_cause = root_cause;
        this.failed_shards = failed_shards;
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

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getGrouped() {
        return grouped;
    }

    public void setGrouped(String grouped) {
        this.grouped = grouped;
    }

    public CausedBy getCaused_by() {
        return caused_by;
    }

    public void setCaused_by(CausedBy caused_by) {
        this.caused_by = caused_by;
    }

    public List<Reason> getRoot_cause() {
        return root_cause;
    }

    public void setRoot_cause(List<Reason> root_cause) {
        this.root_cause = root_cause;
    }

    public List<FailedShard> getFailed_shards() {
        return failed_shards;
    }

    public void setFailed_shards(List<FailedShard> failed_shards) {
        this.failed_shards = failed_shards;
    }

    @Override
    public String toString() {
        return "EsError{" +
                "type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", phase='" + phase + '\'' +
                ", grouped='" + grouped + '\'' +
                ", caused_by=" + caused_by +
                ", root_cause=" + root_cause +
                ", failed_shards=" + failed_shards +
                '}';
    }

}
