package com.blue.auth.api.component.jwt.api.conf;

import java.util.List;

/**
 * member jwt params
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberJwtConfParams implements MemberJwtConf {

    private transient Long globalMaxExpiresMillis;

    private transient Long globalMinExpiresMillis;

    private transient Long globalRefreshExpiresMillis;

    private transient String signKey;

    private transient List<String> gammaSecrets;

    public MemberJwtConfParams(Long globalMaxExpiresMillis, Long globalMinExpiresMillis, Long globalRefreshExpiresMillis,
                               String signKey, List<String> gammaSecrets) {
        this.globalMaxExpiresMillis = globalMaxExpiresMillis;
        this.globalMinExpiresMillis = globalMinExpiresMillis;
        this.globalRefreshExpiresMillis = globalRefreshExpiresMillis;
        this.signKey = signKey;
        this.gammaSecrets = gammaSecrets;
    }

    @Override
    public Long getGlobalMaxExpiresMillis() {
        return globalMaxExpiresMillis;
    }

    public void setGlobalMaxExpiresMillis(Long globalMaxExpiresMillis) {
        this.globalMaxExpiresMillis = globalMaxExpiresMillis;
    }

    @Override
    public Long getGlobalMinExpiresMillis() {
        return globalMinExpiresMillis;
    }

    public void setGlobalMinExpiresMillis(Long globalMinExpiresMillis) {
        this.globalMinExpiresMillis = globalMinExpiresMillis;
    }

    @Override
    public Long getGlobalRefreshExpiresMillis() {
        return globalRefreshExpiresMillis;
    }

    public void setGlobalRefreshExpiresMillis(Long globalRefreshExpiresMillis) {
        this.globalRefreshExpiresMillis = globalRefreshExpiresMillis;
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
                "globalMaxExpiresMillis=" + globalMaxExpiresMillis +
                ", globalMinExpiresMillis=" + globalMinExpiresMillis +
                ", globalRefreshExpiresMillis=" + globalRefreshExpiresMillis +
                ", signKey='" + signKey + '\'' +
                ", gammaSecrets=" + gammaSecrets +
                '}';
    }

}
