package com.blue.shine.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * highlight config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "highlight")
public class HighlightDeploy {

    private List<String> preTags;

    private List<String> postTags;

    public HighlightDeploy() {
    }

    public List<String> getPreTags() {
        return preTags;
    }

    public void setPreTags(List<String> preTags) {
        this.preTags = preTags;
    }

    public List<String> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<String> postTags) {
        this.postTags = postTags;
    }

    @Override
    public String toString() {
        return "HighlightDeploy{" +
                "preTags=" + preTags +
                ", postTags=" + postTags +
                '}';
    }

}
