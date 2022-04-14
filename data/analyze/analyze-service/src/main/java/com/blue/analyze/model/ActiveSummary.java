package com.blue.analyze.model;

import java.io.Serializable;
import java.util.Map;

/**
 * summary for active
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ActiveSummary<T> implements Serializable {

    private static final long serialVersionUID = 4933618768410864562L;

    private Map<String, T> summary;

    private String disc;

    public ActiveSummary() {
    }

    public ActiveSummary(Map<String, T> summary, String disc) {
        this.summary = summary;
        this.disc = disc;
    }

    public Map<String, T> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, T> summary) {
        this.summary = summary;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    @Override
    public String toString() {
        return "ActiveSummary{" +
                "summary=" + summary +
                ", disc='" + disc + '\'' +
                '}';
    }

}
