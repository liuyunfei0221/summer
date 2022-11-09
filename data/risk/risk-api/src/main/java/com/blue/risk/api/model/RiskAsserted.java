package com.blue.risk.api.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;

/**
 * risk assert result
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class RiskAsserted implements Serializable {

    private static final long serialVersionUID = 2476356777096217786L;

    /**
     * hit risk?
     */
    private volatile boolean hit;

    /**
     * interrupt
     */
    private volatile boolean interrupt;

    /**
     * hits
     */
    private volatile List<RiskHit> hits;

    public RiskAsserted() {
        this.hit = false;
        this.interrupt = false;
        this.hits = new LinkedList<>();
    }

    public RiskAsserted(boolean hit, boolean interrupt, List<RiskHit> hits) {
        this.hit = hit;
        this.interrupt = interrupt;
        this.hits = hits;
    }

    public boolean getHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean getInterrupt() {
        return interrupt;
    }

    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    public List<RiskHit> getHits() {
        return hits;
    }

    public void setHits(List<RiskHit> hits) {
        this.hits = hits;

        if (isNotEmpty(hits))
            if (!this.hit)
                this.hit = true;
    }

    public void addHit(RiskHit riskHit) {
        riskHit.asserts();

        synchronized (this) {
            if (isNull(hits))
                this.hits = new LinkedList<>();

            hits.add(riskHit);

            if (!this.hit)
                this.hit = true;
        }
    }

    @Override
    public String toString() {
        return "RiskAsserted{" +
                "hit=" + hit +
                ", interrupt=" + interrupt +
                ", hits=" + hits +
                '}';
    }

}
