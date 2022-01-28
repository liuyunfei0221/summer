package com.blue.mail.api.conf;

/**
 * @author DarkBlue
 */
public final class SmtpAttr {

    private String smtpServerHost;

    private Integer smtpServerPort;

    private String smtpUsername;

    private String smtpPassword;

    private Boolean mailSmtpSsl;

    private Boolean mailSmtpStarttlsEnable;

    public SmtpAttr() {
    }

    public String getSmtpServerHost() {
        return smtpServerHost;
    }

    public void setSmtpServerHost(String smtpServerHost) {
        this.smtpServerHost = smtpServerHost;
    }

    public Integer getSmtpServerPort() {
        return smtpServerPort;
    }

    public void setSmtpServerPort(Integer smtpServerPort) {
        this.smtpServerPort = smtpServerPort;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public Boolean getMailSmtpSsl() {
        return mailSmtpSsl;
    }

    public void setMailSmtpSsl(Boolean mailSmtpSsl) {
        this.mailSmtpSsl = mailSmtpSsl;
    }

    public Boolean getMailSmtpStarttlsEnable() {
        return mailSmtpStarttlsEnable;
    }

    public void setMailSmtpStarttlsEnable(Boolean mailSmtpStarttlsEnable) {
        this.mailSmtpStarttlsEnable = mailSmtpStarttlsEnable;
    }

    @Override
    public String toString() {
        return "SenderAttr{" +
                "smtpServerHost='" + smtpServerHost + '\'' +
                ", smtpServerPort=" + smtpServerPort +
                ", smtpUsername='" + smtpUsername + '\'' +
                ", smtpPassword='" + smtpPassword + '\'' +
                ", mailSmtpSsl=" + mailSmtpSsl +
                ", mailSmtpStarttlsEnable=" + mailSmtpStarttlsEnable +
                '}';
    }

}
