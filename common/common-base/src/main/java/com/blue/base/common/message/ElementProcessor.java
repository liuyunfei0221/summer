package com.blue.base.common.message;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.common.base.PropertiesProcessor;
import com.blue.base.constant.common.ElementKey;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.FileGetter.getFiles;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getAcceptLanguages;
import static com.blue.base.constant.common.ElementKey.DEFAULT;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.common.SummerAttr.LANGUAGE;
import static com.blue.base.constant.common.Symbol.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.*;
import static reactor.util.Loggers.getLogger;

/**
 * i18n element processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "unused"})
public final class ElementProcessor {

    private static final Logger LOGGER = getLogger(ElementProcessor.class);

    private static final String DEFAULT_LANGUAGE = lowerCase(replace(LANGUAGE, PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity));
    private static final String DEFAULT_KEY = DEFAULT.key;
    private static final String DEFAULT_VALUE = DEFAULT.key;

    private static final UnaryOperator<String> PRE_NAME_PARSER = n -> {
        int idx = lastIndexOf(n, SCHEME_SEPARATOR.identity);
        String name = idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : EMPTY_DATA.value) : n;
        return replace(name, PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity);
    };

    private static volatile Map<String, Map<String, String>> I_18_N;

    private static final Consumer<String> ELEMENT_LOADER = uri -> {
        List<File> files = getFiles(uri, true).stream().filter(Objects::nonNull).collect(toList());

        LOGGER.info("files = {}", files);

        if (files.size() != ofNullable(MessageProcessor.supportLanguages()).map(List::size).orElse(0))
            LOGGER.warn("size of element languages support and size of message languages support are different");

        //noinspection UnnecessaryLocalVariable
        Map<String, Map<String, String>> i18n = files.stream()
                .collect(toMap(f -> lowerCase(PRE_NAME_PARSER.apply(f.getName())),
                        PropertiesProcessor::parseProp, (a, b) -> a));

        I_18_N = i18n;
    };

    private static final Function<List<String>, Map<String, String>> ELEMENT_GETTER = languages -> {
        if (BlueChecker.isNotEmpty(languages)) {
            Map<String, String> element;
            for (String language : languages)
                if (isNotNull(element = I_18_N.get(lowerCase(language))))
                    return element;
        }

        return I_18_N.get(DEFAULT_LANGUAGE);
    };

    private static final BiFunction<Map<String, String>, List<String>, Map<String, String>> TARGETS_GETTER = (allElement, keys) -> {
        if (BlueChecker.isNotEmpty(allElement) && BlueChecker.isNotEmpty(keys)) {
            Map<String, String> res = new HashMap<>(keys.size(), 1.0f);
            String value;
            for (String key : keys) {
                if (isNotBlank(key)) {
                    value = allElement.get(key);
                    res.put(key, isNotNull(value) ? value : EMPTY_DATA.value);
                }
            }

            return res;
        }

        return emptyMap();
    };

    private static final BiFunction<Map<String, String>, ElementKey, String> VALUE_GETTER = (values, key) ->
            ofNullable(values.get(ofNullable(key).map(k -> k.key).orElse(DEFAULT_KEY))).orElse(DEFAULT_VALUE).intern();

    /**
     * load i18n elements
     */
    public static void load(String location) {
        if (isBlank(location))
            throw new RuntimeException("location can't be blank");

        ELEMENT_LOADER.accept(location);
    }

    /**
     * select all elements by default language
     *
     * @return
     */
    public static Map<String, String> selectAllElement() {
        return ELEMENT_GETTER.apply(null);
    }

    /**
     * select all elements by languages
     *
     * @param languages
     * @return
     */
    public static Map<String, String> selectAllElement(List<String> languages) {
        return ELEMENT_GETTER.apply(languages);
    }

    /**
     * select all elements by request
     *
     * @param serverRequest
     * @return
     */
    public static Map<String, String> selectAllElement(ServerRequest serverRequest) {
        return selectAllElement(getAcceptLanguages(serverRequest));
    }

    /**
     * select elements by default language and keys
     *
     * @param keys
     * @return
     */
    public static Map<String, String> selectElement(List<String> keys) {
        return BlueChecker.isNotEmpty(keys) ? TARGETS_GETTER.apply(ELEMENT_GETTER.apply(null), keys) : emptyMap();
    }

    /**
     * select elements by languages and keys
     *
     * @param languages
     * @param keys
     * @return
     */
    public static Map<String, String> selectElement(List<String> languages, List<String> keys) {
        return BlueChecker.isNotEmpty(keys) ? TARGETS_GETTER.apply(ELEMENT_GETTER.apply(languages), keys) : emptyMap();
    }

    /**
     * select elements by request and request
     *
     * @param serverRequest
     * @param keys
     * @return
     */
    public static Map<String, String> selectElement(ServerRequest serverRequest, List<String> keys) {
        return selectElement(getAcceptLanguages(serverRequest), keys);
    }

    /**
     * get element value by i18n
     *
     * @param key
     * @return
     */
    public static String resolveToValue(ElementKey key) {
        return resolveToValue(key, emptyList());
    }

    /**
     * get element values by i18n
     *
     * @param keys
     * @return
     */
    public static String[] resolveToValues(ElementKey[] keys) {
        return resolveToValues(keys, emptyList());
    }

    /**
     * get element value by i18n
     *
     * @param key
     * @param languages
     * @return
     */
    public static String resolveToValue(ElementKey key, List<String> languages) {
        return ofNullable(ELEMENT_GETTER.apply(languages))
                .map(values -> VALUE_GETTER.apply(values, key))
                .orElse(DEFAULT_VALUE).intern();
    }

    /**
     * get element values by i18n
     *
     * @param keys
     * @param languages
     * @return
     */
    public static String[] resolveToValues(ElementKey[] keys, List<String> languages) {
        return ofNullable(ELEMENT_GETTER.apply(languages))
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
     * get element value by i18n
     *
     * @param key
     * @param serverRequest
     * @return
     */
    public static String resolveToValue(ElementKey key, ServerRequest serverRequest) {
        return resolveToValue(key, getAcceptLanguages(serverRequest));
    }

    /**
     * get element values by i18n
     *
     * @param keys
     * @param serverRequest
     * @return
     */
    public static String[] resolveToValues(ElementKey[] keys, ServerRequest serverRequest) {
        return resolveToValues(keys, getAcceptLanguages(serverRequest));
    }

}