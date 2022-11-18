package com.blue.auth.api.model;

import java.io.Serializable;

/**
 * local auth invalid key id
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class InvalidLocalAccessEvent implements Serializable {

    private static final long serialVersionUID = -4910883019127949636L;

    private String keyId;

    public InvalidLocalAccessEvent() {
    }

    public InvalidLocalAccessEvent(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    @Override
    public String toString() {
        return "InvalidLocalAuthParam{" +
                "keyId='" + keyId + '\'' +
                '}';
    }
}
