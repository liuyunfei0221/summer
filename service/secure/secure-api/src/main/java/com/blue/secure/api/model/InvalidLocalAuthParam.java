package com.blue.secure.api.model;

import java.io.Serializable;

/**
 * local auth invalid key id
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class InvalidLocalAuthParam implements Serializable {

    private static final long serialVersionUID = -4910883019127949636L;

    private String keyId;

    public InvalidLocalAuthParam() {
    }

    public InvalidLocalAuthParam(String keyId) {
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
