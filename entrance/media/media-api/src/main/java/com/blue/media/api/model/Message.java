package com.blue.media.api.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * message
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class Message implements Serializable {

    private static final long serialVersionUID = 843848209301560109L;

    /**
     * @see com.blue.basic.constant.media.MessageType
     */
    private Integer type;

    /**
     * @see com.blue.basic.constant.media.MessageBusinessType
     */
    private Integer businessType;

    private String source;

    private String destination;

    private String[] titlePlaceholders;

    private String[] contentPlaceholders;

    public Message() {
    }

    public Message(Integer type, Integer businessType, String source, String destination, String[] titlePlaceholders, String[] contentPlaceholders) {
        this.type = type;
        this.businessType = businessType;
        this.source = source;
        this.destination = destination;
        this.titlePlaceholders = titlePlaceholders;
        this.contentPlaceholders = contentPlaceholders;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String[] getTitlePlaceholders() {
        return titlePlaceholders;
    }

    public void setTitlePlaceholders(String[] titlePlaceholders) {
        this.titlePlaceholders = titlePlaceholders;
    }

    public String[] getContentPlaceholders() {
        return contentPlaceholders;
    }

    public void setContentPlaceholders(String[] contentPlaceholders) {
        this.contentPlaceholders = contentPlaceholders;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", businessType=" + businessType +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", titlePlaceholders=" + Arrays.toString(titlePlaceholders) +
                ", contentPlaceholders=" + Arrays.toString(contentPlaceholders) +
                '}';
    }

}
