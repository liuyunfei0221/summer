package com.blue.base.common.base;

import com.blue.base.constant.base.RandomType;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.blue.base.constant.base.RandomType.*;

/**
 * random util base on commons-lang
 *
 * @author liuyunfei
 * @date 2021/8/18
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class BlueRandomGenerator {

    private static final Map<RandomType, Function<Integer, String>> GENERATOR_MAPPING;

    static {
        GENERATOR_MAPPING = new HashMap<>(16, 2.0f);

        GENERATOR_MAPPING.put(NUMERIC, RandomStringUtils::randomNumeric);
        GENERATOR_MAPPING.put(ALPHABETIC, RandomStringUtils::randomAlphabetic);
        GENERATOR_MAPPING.put(ALPHANUMERIC, RandomStringUtils::randomAlphanumeric);
        GENERATOR_MAPPING.put(ASCII, RandomStringUtils::randomAscii);
        GENERATOR_MAPPING.put(GRAPH, RandomStringUtils::randomGraph);
    }

    /**
     * 根据类型及长度生成随机信息
     *
     * @param type
     * @param length
     * @return
     */
    public static String generateRandom(RandomType type, int length) {
        if (type == null || length < 1)
            throw new RuntimeException("type不能为空且length不能小于1");

        Function<Integer, String> generator = GENERATOR_MAPPING.get(type);
        if (generator == null)
            throw new RuntimeException("不支持的type" + type);

        return generator.apply(length);
    }


}
