package com.blue.media.model;

/**
 * qr code elements
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class QrCodeElement {

    private String content;

    private String descName;

    private byte[] bytes;

    public QrCodeElement() {
    }

    public QrCodeElement(String content, String descName, byte[] bytes) {
        this.content = content;
        this.descName = descName;
        this.bytes = bytes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescName() {
        return descName;
    }

    public void setDescName(String descName) {
        this.descName = descName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "QrCodeElement{" +
                "content='" + content + '\'' +
                ", descName='" + descName + '\'' +
                '}';
    }
}
