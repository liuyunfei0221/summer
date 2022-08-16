package com.blue.redis.api.generator;

import com.blue.redis.script.BlueRedisScript;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * redis script generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueRedisScriptGenerator {

    /**
     * generate script by location
     *
     * @param location
     * @param clz
     * @return
     */
    public static <T> RedisScript<T> generateScriptByLocation(String location, Class<T> clz) {
        if (isBlank(location) || isNull(clz))
            throw new RuntimeException("location can't be blank, clz can't be null");

        Resource resource = new ClassPathResource(location);
        EncodedResource encodedResource = new EncodedResource(resource, UTF_8.name());
        ResourceScriptSource resourceScriptSource = new ResourceScriptSource(encodedResource);
        String scriptStr;
        try {
            scriptStr = resourceScriptSource.getScriptAsString();
        } catch (IOException e) {
            throw new RuntimeException("scriptStr can't be null or ''");
        }

        return new BlueRedisScript<>(scriptStr, clz);
    }

    /**
     * generate script by str
     *
     * @param script
     * @param clz
     * @return
     */
    public static <T> RedisScript<T> generateScriptByScriptStr(String script, Class<T> clz) {
        if (isBlank(script) || isNull(clz))
            throw new RuntimeException("script can't be blank, clz can't be null");

        return new BlueRedisScript<>(script, clz);
    }

}
