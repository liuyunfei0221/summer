package com.blue.marketing.api.model;


import java.io.Serializable;

/**
 * event info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EventRecordInfo implements Serializable {

    private static final long serialVersionUID = 6569268133204313196L;

    private Long id;

    /**
     * event type
     *
     * @see com.blue.base.constant.marketing.MarketingEventType
     */
    private Integer type;

    /**
     * event json
     */
    private String data;

    /**
     * handling status
     *
     * @see com.blue.base.constant.marketing.HandleStatus
     */
    private Integer status;

    /**
     * event time
     */
    private Long createTime;

    /**
     * creator
     */
    private Long creator;


}
