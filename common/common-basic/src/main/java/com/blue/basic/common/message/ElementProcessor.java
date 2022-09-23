package com.blue.basic.common.message;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.common.base.PropertiesProcessor;
import com.blue.basic.constant.common.ElementKey;
import com.blue.basic.model.message.LanguageInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.FileGetter.getFiles;
import static com.blue.basic.common.base.CommonFunctions.getAcceptLanguages;
import static com.blue.basic.common.base.FileGetter.getResources;
import static com.blue.basic.constant.common.BluePrefix.CLASS_PATH_PREFIX;
import static com.blue.basic.constant.common.BlueSuffix.PROP;
import static com.blue.basic.constant.common.ElementKey.DEFAULT;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.SummerAttr.LANGUAGE;
import static com.blue.basic.constant.common.Symbol.*;
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
final class ElementProcessor {

    private static final Logger LOGGER = getLogger(ElementProcessor.class);

    private static final String DEFAULT_LANGUAGE = lowerCase(replace(LANGUAGE, PAR_CONCATENATION.identity, HYPHEN.identity));
    private static final String DEFAULT_KEY = DEFAULT.key;
    private static final String DEFAULT_VALUE = DEFAULT.key;

    private static final UnaryOperator<String> LANGUAGE_IDENTITY_PARSER = n -> {
        int idx = lastIndexOf(n, PERIOD.identity);
        return replace(idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : n) : n, PAR_CONCATENATION.identity, HYPHEN.identity);
    };

    private static volatile Map<String, Map<String, String>> I_18_N;

    private static final Function<List<LanguageInfo>, Map<String, String>> SIMPLE_LANGUAGE_AND_LANGUAGE_MAPPING_PARSER = supportLanguages -> {
        if (BlueChecker.isEmpty(supportLanguages))
            return emptyMap();

        Map<String, String> simpleLanguagesMapping = new HashMap<>(supportLanguages.size());

        String languageIdentity;
        String simpleLanguage;
        String[] languageWithCountry;
        for (LanguageInfo li : supportLanguages) {
            languageIdentity = li.getIdentity();
            languageWithCountry = split(languageIdentity, HYPHEN.identity);
            if (languageWithCountry.length == 1)
                continue;

            simpleLanguage = languageWithCountry[0];
            if (simpleLanguagesMapping.containsKey(simpleLanguage))
                continue;

            simpleLanguagesMapping.put(simpleLanguage, toRootLowerCase(LANGUAGE_IDENTITY_PARSER.apply(languageIdentity)));
        }

        return simpleLanguagesMapping;
    };

    private static final Consumer<String> CLASS_PATH_ELEMENT_LOADER = location -> {
        List<Resource> resources = getResources(location, PROP.suffix);
        LOGGER.info("resources = {}", resources);

        if (resources.size() != ofNullable(MessageProcessor.supportLanguages()).map(List::size).orElse(0))
            LOGGER.warn("size of element languages support and size of message languages support are different");

        Map<String, Map<String, String>> i18n = resources.stream()
                .collect(toMap(f -> lowerCase(LANGUAGE_IDENTITY_PARSER.apply(f.getFilename())),
                        PropertiesProcessor::parseProp, (a, b) -> a));

        List<LanguageInfo> supportLanguages = MessageProcessor.supportLanguages();

        SIMPLE_LANGUAGE_AND_LANGUAGE_MAPPING_PARSER.apply(supportLanguages)
                .forEach((key, value) -> i18n.put(key, i18n.get(value)));

        I_18_N = i18n;
        LOGGER.info("I_18_N = {}", I_18_N);
    };

    private static final Consumer<String> FILE_ELEMENT_LOADER = location -> {
        List<File> files = getFiles(location, true).stream().filter(Objects::nonNull).collect(toList());
        LOGGER.info("files = {}", files);

        if (files.size() != ofNullable(MessageProcessor.supportLanguages()).map(List::size).orElse(0))
            LOGGER.warn("size of element languages support and size of message languages support are different");

        Map<String, Map<String, String>> i18n = files.stream()
                .collect(toMap(f -> lowerCase(LANGUAGE_IDENTITY_PARSER.apply(f.getName())),
                        PropertiesProcessor::parseProp, (a, b) -> a));

        List<LanguageInfo> supportLanguages = MessageProcessor.supportLanguages();

        SIMPLE_LANGUAGE_AND_LANGUAGE_MAPPING_PARSER.apply(supportLanguages)
                .forEach((key, value) -> i18n.put(key, i18n.get(value)));

        I_18_N = i18n;
        LOGGER.info("I_18_N = {}", I_18_N);
    };

    private static final Predicate<String> CLASS_PATH_PRE = location ->
            startsWith(location, CLASS_PATH_PREFIX.prefix);

    private static final Consumer<String> ELEMENT_LOADER = location -> {
        if (CLASS_PATH_PRE.test(location)) {
            CLASS_PATH_ELEMENT_LOADER.accept(location);
        } else {
            FILE_ELEMENT_LOADER.accept(location);
        }
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

    private static final BiFunction<List<String>, Map<String, String>, Map<String, String>> TARGETS_GETTER = (keys, allElement) -> {
        if (BlueChecker.isNotEmpty(allElement) && BlueChecker.isNotEmpty(keys)) {
            Map<String, String> res = new HashMap<>(keys.size(), 2.0f);
            String value;
            for (String key : keys) {
                if (isNotBlank(key)) {
                    value = allElement.get(key);
                    res.put(key, isNotNull(value) ? value : EMPTY_VALUE.value);
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
    static void load(String location) {
        if (isBlank(location))
            throw new RuntimeException("location can't be blank");

        ELEMENT_LOADER.accept(location);
    }

    /**
     * select all elements by default language
     *
     * @return
     */
    static Map<String, String> selectAllElement() {
        return ELEMENT_GETTER.apply(null);
    }

    /**
     * select all elements by languages
     *
     * @param languages
     * @return
     */
    static Map<String, String> selectAllElement(List<String> languages) {
        return ELEMENT_GETTER.apply(languages);
    }

    /**
     * select all elements by request
     *
     * @param serverRequest
     * @return
     */
    static Map<String, String> selectAllElement(ServerRequest serverRequest) {
        return selectAllElement(getAcceptLanguages(serverRequest));
    }

    /**
     * select elements by default language and keys
     *
     * @param keys
     * @return
     */
    static Map<String, String> selectElement(List<String> keys) {
        return BlueChecker.isNotEmpty(keys) ? TARGETS_GETTER.apply(keys, ELEMENT_GETTER.apply(null)) : emptyMap();
    }

    /**
     * select elements by languages and keys
     *
     * @param keys
     * @param languages
     * @return
     */
    static Map<String, String> selectElement(List<String> keys, List<String> languages) {
        return BlueChecker.isNotEmpty(keys) ? TARGETS_GETTER.apply(keys, ELEMENT_GETTER.apply(languages)) : emptyMap();
    }

    /**
     * select elements by request and request
     *
     * @param keys
     * @param serverRequest
     * @return
     */
    static Map<String, String> selectElement(List<String> keys, ServerRequest serverRequest) {
        return selectElement(keys, getAcceptLanguages(serverRequest));
    }

    /**
     * get element value by i18n
     *
     * @param key
     * @return
     */
    static String resolveToValue(ElementKey key) {
        return resolveToValue(key, emptyList());
    }

    /**
     * get element values by i18n
     *
     * @param keys
     * @return
     */
    static String[] resolveToValues(ElementKey[] keys) {
        return resolveToValues(keys, emptyList());
    }

    /**
     * get element value by i18n
     *
     * @param key
     * @param languages
     * @return
     */
    static String resolveToValue(ElementKey key, List<String> languages) {
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
    static String[] resolveToValues(ElementKey[] keys, List<String> languages) {
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
    static String resolveToValue(ElementKey key, ServerRequest serverRequest) {
        return resolveToValue(key, getAcceptLanguages(serverRequest));
    }

    /**
     * get element values by i18n
     *
     * @param keys
     * @param serverRequest
     * @return
     */
    static String[] resolveToValues(ElementKey[] keys, ServerRequest serverRequest) {
        return resolveToValues(keys, getAcceptLanguages(serverRequest));
    }

}