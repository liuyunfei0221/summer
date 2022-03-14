package com.blue.base.common.message;

import com.blue.base.common.base.PropertiesProcessor;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.Logger;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.FileGetter.getFiles;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getAcceptLanguages;
import static com.blue.base.constant.base.DictKey.DEFAULT;
import static com.blue.base.constant.base.SummerAttr.LANGUAGE;
import static com.blue.base.constant.base.Symbol.*;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.*;
import static reactor.util.Loggers.getLogger;

/**
 * i18n dict processor
 *
 * @author liuyunfei
 * @date 2021/12/19
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "unused"})
public final class DictProcessor {

    private static final Logger LOGGER = getLogger(DictProcessor.class);

    private static final String DICT_URI = "classpath:i18n/dict";
    private static final String DEFAULT_LANGUAGE = lowerCase(replace(LANGUAGE, PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity));
    private static final String DEFAULT_KEY = DEFAULT.key;
    private static final String DEFAULT_VALUE = DEFAULT.key;

    private static final UnaryOperator<String> PRE_NAME_PARSER = n -> {
        int idx = lastIndexOf(n, SCHEME_SEPARATOR.identity);
        String name = idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : "") : n;
        return replace(name, PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity);
    };

    private static final Function<String, Map<String, Map<String, String>>> DICT_LOADER = uri -> {
        List<File> files = getFiles(uri, true).stream().filter(Objects::nonNull).collect(toList());
        Integer supportLanguageSize = ofNullable(MessageProcessor.listSupportLanguages()).map(List::size).orElse(0);

        if (files.size() != supportLanguageSize)
            LOGGER.warn("size of dict languages support and size of message languages support are different");

        return files.stream()
                .collect(toMap(f -> lowerCase(PRE_NAME_PARSER.apply(f.getName())),
                        PropertiesProcessor::parseProp,
                        (a, b) -> a));
    };

    private static final Map<String, Map<String, String>> I_18_N = DICT_LOADER.apply(DICT_URI);

    private static final Function<List<String>, Map<String, String>> DICT_GETTER = languages -> {
        if (languages != null) {
            Map<String, String> dict;
            for (String language : languages)
                if ((dict = I_18_N.get(language)) != null)
                    return dict;
        }

        return I_18_N.get(DEFAULT_LANGUAGE);
    };

    /**
     * get dict value by i18n
     *
     * @param key
     * @return
     */
    public static String resolveToValue(String key) {
        return resolveToValue(key, emptyList());
    }

    /**
     * get dict value by i18n
     *
     * @param key
     * @param languages
     * @return
     */
    public static String resolveToValue(String key, List<String> languages) {
        return ofNullable(DICT_GETTER.apply(languages))
                .map(messages -> ofNullable(messages.get(ofNullable(key).orElse(DEFAULT_KEY))).orElse(DEFAULT_VALUE).intern())
                .orElse(DEFAULT_VALUE).intern();
    }

    /**
     * get dict value by i18n
     *
     * @param key
     * @param serverRequest
     * @return
     */
    public static String resolveToValue(String key, ServerRequest serverRequest) {
        return resolveToValue(key, getAcceptLanguages(serverRequest));
    }

}