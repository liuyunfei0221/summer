package com.blue.basic.component.encoder.api.conf;

/**
 * aes encode conf param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public class EncoderConfParams implements EncoderConf {

    protected String salt;

    public EncoderConfParams() {
    }

    public EncoderConfParams(String salt) {
        this.salt = salt;
    }

    @Override
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "EncoderConfParams{" +
                "salt='" + ":)" + '\'' +
                '}';
    }

}