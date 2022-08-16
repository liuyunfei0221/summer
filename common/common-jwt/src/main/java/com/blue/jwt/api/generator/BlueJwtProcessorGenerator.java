package com.blue.jwt.api.generator;

import com.blue.jwt.api.conf.JwtConf;
import com.blue.jwt.component.BlueJwtProcessor;
import com.blue.jwt.component.JwtProcessor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * JwtProcessor generator
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class BlueJwtProcessorGenerator {

    private static final Logger LOGGER = getLogger(BlueJwtProcessorGenerator.class);

    /**
     * generate jwt processor
     *
     * @param jwtConf
     * @return
     */
    public static <T> JwtProcessor<T> generate(JwtConf<T> jwtConf) {
        LOGGER.info("<T> JwtProcessor<T> generate(JwtConf<T> jwtConf), jwtConf = {}", jwtConf);
        return new BlueJwtProcessor<>(jwtConf);
    }

}
