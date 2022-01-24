package com.blue.mail.api.conf;

/**
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class MailReaderConfParams implements MailReaderConf {

    private String imapHost;

    private Integer imapPort;

    private Boolean imapSslEnable;

    private String user;

    private String password;

    private String folderName;

    private Integer maxWaitingMillisForRefresh;

    private Boolean debug;

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
                ", maxWaitingMillisForRefresh=" + maxWaitingMillisForRefresh +
                ", debug=" + debug +
                '}';
    }

}
