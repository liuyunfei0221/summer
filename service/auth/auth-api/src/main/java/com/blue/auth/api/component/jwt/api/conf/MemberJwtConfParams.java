package com.blue.auth.api.component.jwt.api.conf;

import java.util.List;

/**
 * member jwt params
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class MemberJwtConfParams implements MemberJwtConf {

    private transient Long globalMaxExpireMillis;

    private transient Long globalMinExpireMillis;

    private transient String signKey;

    private transient List<String> gammaSecrets;

    public MemberJwtConfParams(Long globalMaxExpireMillis, Long globalMinExpireMillis,
                               String signKey, List<String> gammaSecrets) {
        this.globalMaxExpireMillis = globalMaxExpireMillis;
        this.globalMinExpireMillis = globalMinExpireMillis;
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
        return "MemberJwtProcessorParam{" +
                "globalMaxExpireMillis=" + globalMaxExpireMillis +
                ", globalMinExpireMillis=" + globalMinExpireMillis +
                ", signKey='" + signKey + '\'' +
                ", gammaSecrets=" + gammaSecrets +
                '}';
    }

}
