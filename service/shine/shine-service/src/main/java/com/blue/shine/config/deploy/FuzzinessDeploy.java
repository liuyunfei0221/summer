package com.blue.shine.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * fuzziness config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "fuzziness")
public class FuzzinessDeploy {

    private Integer fuzziness;

    public FuzzinessDeploy() {
    }

    public Integer getFuzziness() {
        return fuzziness;
    }

    public void setFuzziness(Integer fuzziness) {
        this.fuzziness = fuzziness;
    }

    @Override
    public String toString() {
        return "FuzzinessDeploy{" +
                "fuzziness=" + fuzziness +
                '}';
    }

}
