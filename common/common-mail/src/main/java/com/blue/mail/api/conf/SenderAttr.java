package com.blue.mail.api.conf;

import java.util.Map;

/**
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class SenderAttr {

    private transient String user;

    private transient String password;

    private transient Map<String, String> props;

    public SenderAttr() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    @Override
    public String toString() {
        return "SenderAttr{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", props=" + props +
                '}';
    }

}
