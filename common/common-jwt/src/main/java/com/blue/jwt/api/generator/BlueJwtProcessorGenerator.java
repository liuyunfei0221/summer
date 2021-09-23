package com.blue.jwt.api.generator;

import com.blue.jwt.api.conf.JwtConf;
import com.blue.jwt.common.BlueJwtProcessor;
import com.blue.jwt.common.JwtProcessor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * JwtProcessor创建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public final class BlueJwtProcessorGenerator {

    private static final Logger LOGGER = getLogger(BlueJwtProcessorGenerator.class);

    /**
     * 创建方法
     *
     * @param jwtConf
     * @return
     */
    public static <T> JwtProcessor<T> create(JwtConf<T> jwtConf) {
        LOGGER.info("create(JwtConf<T> jwtConf), jwtConf = {}", jwtConf);
        return new BlueJwtProcessor<>(jwtConf);
    }

}
