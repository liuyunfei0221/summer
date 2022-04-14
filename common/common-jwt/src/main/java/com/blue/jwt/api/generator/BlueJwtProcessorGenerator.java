package com.blue.jwt.api.generator;

import com.blue.jwt.api.conf.JwtConf;
import com.blue.jwt.common.BlueJwtProcessor;
import com.blue.jwt.common.JwtProcessor;
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
        LOGGER.info("create(JwtConf<T> jwtConf), jwtConf = {}", jwtConf);
        return new BlueJwtProcessor<>(jwtConf);
    }

}
