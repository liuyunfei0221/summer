package com.blue.basic.component.lifecycle.conf;

/**
 * BlueLifecycle phase conf param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public class BlueLifecyclePhaseConfParam implements BlueLifecyclePhaseConf {

    protected Integer phase;

    public BlueLifecyclePhaseConfParam() {
    }

    public BlueLifecyclePhaseConfParam(Integer phase) {
        this.phase = phase;
    }

    @Override
    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }

    @Override
    public String toString() {
        return "BlueLifecyclePhaseConfParam{" +
                "phase=" + phase +
                '}';
    }

}
