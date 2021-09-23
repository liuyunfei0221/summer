package com.blue.redis.api.generator;

import com.blue.redis.common.BlueRedisScript;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * redis脚本创建
 *
 * @author liuyunfei
 * @date 2021/8/19
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueRedisScriptGenerator {

    /**
     * 根据文件创建redis脚本
     *
     * @param location
     * @param clz
     * @return
     */
    public static <T> RedisScript<T> generateScriptByFile(String location, Class<T> clz) {
        if (isBlank(location))
            throw new RuntimeException("location can't be null or ''");

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
     * 根据文件创建redis脚本
     *
     * @param script
     * @param clz
     * @return
     */
    public static <T> RedisScript<T> generateScriptByScriptStr(String script, Class<T> clz) {
        if (isBlank(script))
            throw new RuntimeException("script can't be null or ''");

        return new BlueRedisScript<>(script, clz);
    }

}
