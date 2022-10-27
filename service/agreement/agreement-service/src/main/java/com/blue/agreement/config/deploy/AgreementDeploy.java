package com.blue.agreement.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * agreement deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "agreement")
public class AgreementDeploy {

    private int expiresSecond;

    public AgreementDeploy() {
    }

    public int getExpiresSecond() {
        return expiresSecond;
    }

    public void setExpiresSecond(int expiresSecond) {
        this.expiresSecond = expiresSecond;
    }

    @Override
    public String toString() {
        return "AgreementDeploy{" +
                "expiresSecond=" + expiresSecond +
                '}';
    }

}
