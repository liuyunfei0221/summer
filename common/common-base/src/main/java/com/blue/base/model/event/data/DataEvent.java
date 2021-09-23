package com.blue.base.model.event.data;

import com.blue.base.constant.base.DataEventType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据上报事件
 *
 * @author DarkBlue
 */
@SuppressWarnings("UnusedReturnValue")
public class DataEvent implements Serializable {

    private static final long serialVersionUID = -7101856515135840564L;

    /**
     * 事件类型
     */
    private DataEventType dataEventType;

    /**
     * 秒级时间戳
     */
    private Long stamp;

    /**
     * 数据
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
