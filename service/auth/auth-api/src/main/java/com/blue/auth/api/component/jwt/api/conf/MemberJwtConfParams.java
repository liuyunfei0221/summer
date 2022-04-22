package com.blue.auth.api.component.jwt.api.conf;

import java.util.List;

/**
 * member jwt params
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberJwtConfParams implements MemberJwtConf {

    private transient Long globalMaxExpireMillis;

    private transient Long globalMinExpireMillis;

    private transient Long globalRefreshExpireMillis;

    private transient String signKey;

    private transient List<String> gammaSecrets;

    public MemberJwtConfParams(Long globalMaxExpireMillis, Long globalMinExpireMillis, Long globalRefreshExpireMillis,
                               String signKey, List<String> gammaSecrets) {
        this.globalMaxExpireMillis = globalMaxExpireMillis;
        this.globalMinExpireMillis = globalMinExpireMillis;
        this.globalRefreshExpireMillis = globalRefreshExpireMillis;
        this.signKey = signKey;
        this.gammaSecrets = gammaSecrets;
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
    public Long getGlobalRefreshExpireMillis() {
        return globalRefreshExpireMillis;
    }

    public void setGlobalRefreshExpireMillis(Long globalRefreshExpireMillis) {
        this.globalRefreshExpireMillis = globalRefreshExpireMillis;
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
    public String toString() {
        return "MemberJwtConfParams{" +
                "globalMaxExpireMillis=" + globalMaxExpireMillis +
                ", globalMinExpireMillis=" + globalMinExpireMillis +
                ", globalRefreshExpireMillis=" + globalRefreshExpireMillis +
                ", signKey='" + signKey + '\'' +
                ", gammaSecrets=" + gammaSecrets +
                '}';
    }

}
