package com.blue.base.common.base;

import com.blue.base.constant.base.RandomType;
import com.blue.base.model.exps.BlueException;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.RandomType.*;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;

/**
 * random util base on commons-lang
 *
 * @author liuyunfei
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
     * generate random data by type and length
     *
     * @param type
     * @param length
     * @return
     */
    public static String generateRandom(RandomType type, int length) {
        if (isNull(type) || length < 1)
            throw new BlueException(BAD_REQUEST);

        return ofNullable(GENERATOR_MAPPING.get(type))
                .map(generator -> generator.apply(length))
                .orElseThrow(() -> new BlueException(INVALID_IDENTITY));
    }


}
