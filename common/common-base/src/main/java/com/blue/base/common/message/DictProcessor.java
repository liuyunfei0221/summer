package com.blue.base.common.message;

import com.blue.base.common.base.PropertiesProcessor;
import com.blue.base.constant.base.DictKey;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.Logger;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

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

    private static volatile Map<String, Map<String, String>> I_18_N;

    private static final Consumer<String> DICT_LOADER = uri -> {
        List<File> files = getFiles(uri, true).stream().filter(Objects::nonNull).collect(toList());
        Integer supportLanguageSize = ofNullable(MessageProcessor.listSupportLanguages()).map(List::size).orElse(0);

        if (files.size() != supportLanguageSize)
            LOGGER.warn("size of dict languages support and size of message languages support are different");

        //noinspection UnnecessaryLocalVariable
        Map<String, Map<String, String>> i18n = files.stream()
                .collect(toMap(f -> lowerCase(PRE_NAME_PARSER.apply(f.getName())),
                        PropertiesProcessor::parseProp, (a, b) -> a));

        I_18_N = i18n;
    };

    private static final Function<List<String>, Map<String, String>> DICT_GETTER = languages -> {
        if (languages != null) {
            Map<String, String> dict;
            for (String language : languages)
                if ((dict = I_18_N.get(lowerCase(language))) != null)
                    return dict;
        }

        return I_18_N.get(DEFAULT_LANGUAGE);
    };

    private static final BiFunction<Map<String, String>, DictKey, String> VALUE_GETTER = (values, key) ->
            ofNullable(values.get(ofNullable(key).map(k -> k.key).orElse(DEFAULT_KEY))).orElse(DEFAULT_VALUE).intern();

    static {
        refresh();
    }

    /**
     * refresh i18n messages
     */
    public static void refresh() {
        DICT_LOADER.accept(DICT_URI);
    }

    /**
     * get dict value by i18n
     *
     * @param key
     * @return
     */
    public static String resolveToValue(DictKey key) {
        return resolveToValue(key, emptyList());
    }

    /**
     * get dict values by i18n
     *
     * @param keys
     * @return
     */
    public static String[] resolveToValues(DictKey[] keys) {
        return resolveToValues(keys, emptyList());
    }

    /**
     * get dict value by i18n
     *
     * @param key
     * @param languages
     * @return
     */
    public static String resolveToValue(DictKey key, List<String> languages) {
        return ofNullable(DICT_GETTER.apply(languages))
                .map(values -> VALUE_GETTER.apply(values, key))
                .orElse(DEFAULT_VALUE).intern();
    }

    /**
     * get dict values by i18n
     *
     * @param keys
     * @param languages
     * @return
     */
    public static String[] resolveToValues(DictKey[] keys, List<String> languages) {
        return ofNullable(DICT_GETTER.apply(languages))
                .map(values ->
                        ofNullable(keys)
                                .filter(ks -> ks.length > 0)
                                .map(ks ->
                                        Stream.of(ks)
                                                .map(key -> VALUE_GETTER.apply(values, key))
                                                .toArray(String[]::new)
                                ).orElse(new String[]{DEFAULT_VALUE.intern()})
                )
                .orElse(new String[]{DEFAULT_VALUE.intern()});
    }

    /**
     * get dict value by i18n
     *
     * @param key
     * @param serverRequest
     * @return
     */
    public static String resolveToValue(DictKey key, ServerRequest serverRequest) {
        return resolveToValue(key, getAcceptLanguages(serverRequest));
    }

    /**
     * get dict values by i18n
     *
     * @param keys
     * @param serverRequest
     * @return
     */
    public static String[] resolveToValues(DictKey[] keys, ServerRequest serverRequest) {
        return resolveToValues(keys, getAcceptLanguages(serverRequest));
    }

}