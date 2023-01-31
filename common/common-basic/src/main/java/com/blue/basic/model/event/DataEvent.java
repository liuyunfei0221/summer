package com.blue.basic.model.event;

import com.blue.basic.constant.common.BlueDataAttrKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isNotNull;

/**
 * unified data
 *
 * @author liuyunfei
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class DataEvent implements Serializable {

    private static final long serialVersionUID = -7101856515135840564L;

    private static final int ENTRIES_SIZE = BlueDataAttrKey.values().length;
    private static final float LOAD_FACTOR = 2.0f;

    /**
     * event type
     *
     * @see com.blue.basic.constant.common.DataEventType
     */
    private String dataEventType;

    /**
     * event operate type
     *
     * @see com.blue.basic.constant.common.DataEventOpType
     */
    private String dataEventOpType;

    /**
     * stamp(second)
     */
    private Long stamp;

    /**
     * data
     */
    private Map<String, String> entries = new HashMap<>(ENTRIES_SIZE, LOAD_FACTOR);

    public DataEvent() {
    }

    public DataEvent(String dataEventType, String dataEventOpType, Long stamp, Map<String, String> entries) {
        this.dataEventType = dataEventType;
        this.dataEventOpType = dataEventOpType;
        this.stamp = stamp;

        this.entries = isNotNull(entries) ? entries : new HashMap<>();
    }

    public String getDataEventType() {
        return dataEventType;
    }

    public void setDataEventType(String dataEventType) {
        this.dataEventType = dataEventType;
    }

    public String getDataEventOpType() {
        return dataEventOpType;
    }

    public void setDataEventOpType(String dataEventOpType) {
        this.dataEventOpType = dataEventOpType;
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
                ", dataEventOpType=" + dataEventOpType +
                ", stamp=" + stamp +
                ", entries=" + entries +
                '}';
    }
    
}