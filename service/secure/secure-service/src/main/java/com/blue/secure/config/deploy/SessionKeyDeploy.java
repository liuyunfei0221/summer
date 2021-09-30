package com.blue.secure.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SessionKey config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "session")
public class SessionKeyDeploy {

    private int ranLen;

    public SessionKeyDeploy() {
    }

    public int getRanLen() {
        return ranLen;
    }

    public void setRanLen(int ranLen) {
        this.ranLen = ranLen;
    }
}
