package com.blue.redis.api.generator;

import com.blue.base.model.exps.BlueException;
import com.blue.redis.common.BlueRedisScript;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * redis script generator
 *
 * @author liuyunfei
 * @date 2021/8/19
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueRedisScriptGenerator {

    /**
     * generate script by file
     *
     * @param location
     * @param clz
     * @return
     */
    public static <T> RedisScript<T> generateScriptByFile(String location, Class<T> clz) {
        if (isBlank(location))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "location can't be null or ''", null);

        Resource resource = new ClassPathResource(location);
        EncodedResource encodedResource = new EncodedResource(resource, UTF_8.name());
        ResourceScriptSource resourceScriptSource = new ResourceScriptSource(encodedResource);
        String scriptStr;
        try {
            scriptStr = resourceScriptSource.getScriptAsString();
        } catch (IOException e) {
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "scriptStr can't be null or ''", null);
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
        if (isBlank(script))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "script can't be null or ''", null);

        return new BlueRedisScript<>(script, clz);
    }

}
