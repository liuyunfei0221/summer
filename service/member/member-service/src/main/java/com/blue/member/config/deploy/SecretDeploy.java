package com.blue.member.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * secret deploy
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "secret")
public class SecretDeploy {

    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "SecretDeploy{" +
                "salt='" + "" + '\'' +
                '}';
    }
}
