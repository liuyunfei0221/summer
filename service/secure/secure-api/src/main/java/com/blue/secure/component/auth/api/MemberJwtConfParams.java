package com.blue.secure.component.auth.api;

import java.util.List;

/**
 * member jwt params
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class MemberJwtConfParams implements MemberJwtConf {

    private Long globalMaxExpireMillis;

    private Long globalMinExpireMillis;

    private String signKey;

    private List<String> gammaSecrets;

    private String issuer;

    private String subject;

    private String audience;

    public MemberJwtConfParams(Long globalMaxExpireMillis, Long globalMinExpireMillis,
                               String signKey, List<String> gammaSecrets,
                               String issuer, String subject, String audience) {
        this.globalMaxExpireMillis = globalMaxExpireMillis;
        this.globalMinExpireMillis = globalMinExpireMillis;
        this.signKey = signKey;
        this.gammaSecrets = gammaSecrets;
        this.issuer = issuer;
        this.subject = subject;
        this.audience = audience;
    }

    @Override
    public Long getGlobalMaxExpireMillis() {
        return globalMaxExpireMillis;
    }

    public void setGlobalMaxExpireMillis(Long globalMaxExpireMillis) {
        this.globalMaxExpireMillis = globalMaxExpireMillis;
    }

    @Override
    public Long getGlobalMinExpireMillis() {
        return globalMinExpireMillis;
    }

    public void setGlobalMinExpireMillis(Long globalMinExpireMillis) {
        this.globalMinExpireMillis = globalMinExpireMillis;
    }

    @Override
    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    @Override
    public List<String> getGammaSecrets() {
        return gammaSecrets;
    }

    public void setGammaSecrets(List<String> gammaSecrets) {
        this.gammaSecrets = gammaSecrets;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    @Override
    public String toString() {
        return "MemberJwtProcessorParam{" +
                "globalMaxExpireMillis=" + globalMaxExpireMillis +
                ", globalMinExpireMillis=" + globalMinExpireMillis +
                ", signKey='" + signKey + '\'' +
                ", gammaSecrets=" + gammaSecrets +
                ", issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", audience='" + audience + '\'' +
                '}';
    }

}
