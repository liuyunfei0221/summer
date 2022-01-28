package com.blue.mail.api.conf;

import java.util.List;

/**
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class MailReaderConfParams implements MailReaderConf {

    protected String imapHost;

    protected Integer imapPort;

    protected Boolean imapSslEnable;

    protected String user;

    protected String password;

    protected String folderName;

    protected List<String> throwableForRetry;

    protected Integer maxWaitingMillisForRefresh;

    protected Boolean debug;

    public MailReaderConfParams() {
    }

    public MailReaderConfParams(String imapHost, Integer imapPort, Boolean imapSslEnable, String user, String password, String folderName, Integer maxWaitingMillisForRefresh, Boolean debug) {
        this.imapHost = imapHost;
        this.imapPort = imapPort;
        this.imapSslEnable = imapSslEnable;
        this.user = user;
        this.password = password;
        this.folderName = folderName;
        this.maxWaitingMillisForRefresh = maxWaitingMillisForRefresh;
        this.debug = debug;
    }

    @Override
    public String getImapHost() {
        return imapHost;
    }

    @Override
    public Integer getImapPort() {
        return imapPort;
    }

    @Override
    public Boolean getImapSslEnable() {
        return imapSslEnable;
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

    @Override
    public Boolean getDebug() {
        return debug;
    }

    public void setImapHost(String imapHost) {
        this.imapHost = imapHost;
    }

    public void setImapPort(Integer imapPort) {
        this.imapPort = imapPort;
    }

    public void setImapSslEnable(Boolean imapSslEnable) {
        this.imapSslEnable = imapSslEnable;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        return "MailReaderConfParams{" +
                "imapHost='" + imapHost + '\'' +
                ", imapPort=" + imapPort +
                ", imapSslEnable=" + imapSslEnable +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", folderName='" + folderName + '\'' +
                ", throwableForRetry=" + throwableForRetry +
                ", maxWaitingMillisForRefresh=" + maxWaitingMillisForRefresh +
                ", debug=" + debug +
                '}';
    }

}
