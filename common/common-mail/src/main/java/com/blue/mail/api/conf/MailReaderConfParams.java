package com.blue.mail.api.conf;

import java.util.List;
import java.util.Map;

/**
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class MailReaderConfParams implements MailReaderConf {

    protected transient String user;

    protected transient String password;

    protected transient Map<String, String> props;

    protected transient String folderName;

    protected transient List<String> throwableForRetry;

    protected Integer maxWaitingMillisForRefresh;

    public MailReaderConfParams() {
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Map<String, String> getProps() {
        return props;
    }

    @Override
    public String getFolderName() {
        return folderName;
    }

    @Override
    public List<String> getThrowableForRetry() {
        return throwableForRetry;
    }

    @Override
    public Integer getMaxWaitingMillisForRefresh() {
        return maxWaitingMillisForRefresh;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setThrowableForRetry(List<String> throwableForRetry) {
        this.throwableForRetry = throwableForRetry;
    }

    public void setMaxWaitingMillisForRefresh(Integer maxWaitingMillisForRefresh) {
        this.maxWaitingMillisForRefresh = maxWaitingMillisForRefresh;
    }

    @Override
    public String toString() {
        return "MailReaderConfParams{" +
                "user='" + ":)" + '\'' +
                ", password='" + "(:" + '\'' +
                ", props=" + props +
                ", folderName='" + folderName + '\'' +
                ", throwableForRetry=" + throwableForRetry +
                ", maxWaitingMillisForRefresh=" + maxWaitingMillisForRefresh +
                '}';
    }

}
