package com.blue.registry.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * websocket deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "websocket")
public class WebSocketDeploy {

    private Boolean direct;

    private Integer bufferSize;

    public WebSocketDeploy() {
    }

    public Boolean getDirect() {
        return direct;
    }

    public void setDirect(Boolean direct) {
        this.direct = direct;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public String toString() {
        return "WebSocketDeploy{" +
                "direct=" + direct +
                ", bufferSize=" + bufferSize +
                '}';
    }

}
