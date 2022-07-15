package com.blue.basic.component.message.api.conf;

/**
 * message conf param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public class MessageConfParams implements MessageConf {

    protected String messageLocation;

    protected String elementLocation;

    public MessageConfParams() {
    }

    public MessageConfParams(String messageLocation, String elementLocation) {
        this.messageLocation = messageLocation;
        this.elementLocation = elementLocation;
    }

    @Override
    public String getMessageLocation() {
        return messageLocation;
    }

    @Override
    public String getElementLocation() {
        return elementLocation;
    }

    public void setMessageLocation(String messageLocation) {
        this.messageLocation = messageLocation;
    }

    public void setElementLocation(String elementLocation) {
        this.elementLocation = elementLocation;
    }

    @Override
    public String toString() {
        return "MessageConfParams{" +
                "messageLocation='" + messageLocation + '\'' +
                ", elementLocation='" + elementLocation + '\'' +
                '}';
    }

}
