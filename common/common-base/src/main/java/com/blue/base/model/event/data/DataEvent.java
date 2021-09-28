package com.blue.base.model.event.data;

import com.blue.base.constant.base.DataEventType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * unified data
 *
 * @author DarkBlue
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class DataEvent implements Serializable {

    private static final long serialVersionUID = -7101856515135840564L;

    /**
     * event type
     */
    private DataEventType dataEventType;

    /**
     * stamp(second)
     */
    private Long stamp;

    /**
     * data
     */
    private Map<String, String> entries = new HashMap<>();

    public DataEvent() {
    }

    public DataEvent(DataEventType dataEventType, Long stamp, Map<String, String> entries) {
        this.dataEventType = dataEventType;
        this.stamp = stamp;

        this.entries = entries != null ? entries : new HashMap<>();
    }

    public DataEventType getDataEventType() {
        return dataEventType;
    }

    public void setDataEventType(DataEventType dataEventType) {
        this.dataEventType = dataEventType;
    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public Map<String, String> getEntries() {
        return entries;
    }

    public void setEntries(Map<String, String> entries) {
        this.entries = entries != null ? entries : new HashMap<>();
    }

    public String getData(String key) {
        return entries.get(key);
    }

    public String addData(String key, String value) {
        return entries.put(key, value);
    }

    @Override
    public String toString() {
        return "DataEvent{" +
                "dataEventType=" + dataEventType +
                ", stamp=" + stamp +
                ", entries=" + entries +
                '}';
    }

}
