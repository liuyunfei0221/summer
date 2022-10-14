package com.blue.basic.common.message;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.ElementKey;
import com.blue.basic.model.message.LanguageInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.getAcceptLanguages;
import static com.blue.basic.common.base.FileGetter.getFiles;
import static com.blue.basic.common.base.FileGetter.getResources;
import static com.blue.basic.common.base.PropertiesProcessor.parseProp;
import static com.blue.basic.common.message.ElementProcessor.resolveToValues;
import static com.blue.basic.constant.common.BluePrefix.CLASS_PATH_PREFIX;
import static com.blue.basic.constant.common.BlueSuffix.PROP;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.SummerAttr.LANGUAGE;
import static com.blue.basic.constant.common.Symbol.*;
import static java.lang.Integer.*;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static reactor.util.Loggers.getLogger;

/**
 * i18n message processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
final class MessageProcessor {

    private static final Logger LOGGER = getLogger(MessageProcessor.class);

    static final String DEFAULT_LANGUAGE = lowerCase(replace(LANGUAGE, PAR_CONCATENATION.identity, HYPHEN.identity));
    static final int DEFAULT_CODE = INTERNAL_SERVER_ERROR.code;
    static final String DEFAULT_MESSAGE = INTERNAL_SERVER_ERROR.message;

    private static final String LANGUAGE_NAME_KEY = "NAME";
    private static final String LANGUAGE_PRIORITY_KEY = "PRIORITY";
    private static final String LANGUAGE_ICON_KEY = "ICON";

    private static volatile Map<String, Map<Integer, String>> I_18_N;
    private static volatile List<LanguageInfo> SUPPORT_LANGUAGES;
    private static volatile LanguageInfo DEFAULT_LANGUAGE_INFO;

    private static final UnaryOperator<String> LANGUAGE_IDENTITY_PARSER = n -> {
        int idx = lastIndexOf(n, PERIOD.identity);
        return replace(idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : n) : n, PAR_CONCATENATION.identity, HYPHEN.identity);
    };

    private static final Function<Map<String, String>, Integer> LANGUAGE_PRIORITY_PARSER = map ->
            ofNullable(map.get(LANGUAGE_PRIORITY_KEY))
                    .filter(NumberUtils::isDigits)
                    .map(Integer::parseInt)
                    .orElse(MAX_VALUE);

    private static final Function<Map<String, String>, Map<Integer, String>> MESSAGES_CONVERTER = messages ->
            messages.entrySet().stream()
                    .filter(e -> isDigits(e.getKey()))
                    .collect(toMap(e -> parseInt(e.getKey()), Map.Entry::getValue, (a, b) -> a));


    private static final Function<Map<Integer, LanguageInfo>, List<LanguageInfo>> SUPPORT_LANGUAGES_PARSER = infoMap ->
            BlueChecker.isNotEmpty(infoMap) ? infoMap.entrySet().stream()
                    .sorted((a, b) -> {
                        if (DEFAULT_LANGUAGE.equals(lowerCase(a.getValue().getIdentity())))
                            return MIN_VALUE;

                        if (DEFAULT_LANGUAGE.equals(lowerCase(b.getValue().getIdentity())))
                            return MAX_VALUE;

                        return a.getKey().compareTo(b.getKey());
                    })
                    .map(Map.Entry::getValue).collect(toList())
                    :
                    emptyList();

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

    private static final Consumer<String> CLASS_PATH_MESSAGES_LOADER = location -> {
        List<Resource> resources = getResources(location, PROP.suffix);
        LOGGER.info("resources = {}", resources);

        int size = resources.size();

        Map<Integer, LanguageInfo> infoMap = new HashMap<>(size, 2.0f);
        Map<String, Map<Integer, String>> i18n = new HashMap<>(size << 1, 2.0f);
        LanguageInfo defaultLanguageInfo = null;

        Map<String, String> messages;
        String identity;
        LanguageInfo languageInfo;

        for (Resource r : resources) {
            if (isNull(r))
                continue;

            messages = parseProp(r);
            identity = LANGUAGE_IDENTITY_PARSER.apply(r.getFilename());

            languageInfo = new LanguageInfo(ofNullable(messages.get(LANGUAGE_NAME_KEY)).orElse(EMPTY_VALUE.value),
                    identity, ofNullable(messages.get(LANGUAGE_ICON_KEY)).orElse(EMPTY_VALUE.value));

            infoMap.put(LANGUAGE_PRIORITY_PARSER.apply(messages), languageInfo);

            i18n.put(lowerCase(identity), MESSAGES_CONVERTER.apply(messages));

            if (DEFAULT_LANGUAGE.equals(ofNullable(languageInfo.getIdentity()).map(String::toLowerCase).orElse(EMPTY_VALUE.value)))
                defaultLanguageInfo = languageInfo;
        }

        List<LanguageInfo> supportLanguages = SUPPORT_LANGUAGES_PARSER.apply(infoMap);

        SIMPLE_LANGUAGE_AND_LANGUAGE_MAPPING_PARSER.apply(supportLanguages)
                .forEach((key, value) -> i18n.put(key, i18n.get(value)));

        if (BlueChecker.isEmpty(supportLanguages))
            throw new RuntimeException("supportLanguages can't be empty");

        if (isNull(defaultLanguageInfo))
            defaultLanguageInfo = supportLanguages.get(0);

        I_18_N = i18n;
        SUPPORT_LANGUAGES = supportLanguages;
        DEFAULT_LANGUAGE_INFO = defaultLanguageInfo;

        LOGGER.info("I_18_N = {}", I_18_N);
        LOGGER.info("SUPPORT_LANGUAGES = {}", SUPPORT_LANGUAGES);
        LOGGER.info("DEFAULT_LANGUAGE_INFO = {}", DEFAULT_LANGUAGE_INFO);
    };

    private static final Consumer<String> FILE_MESSAGES_LOADER = location -> {
        List<File> files = getFiles(location, true);
        LOGGER.info("files = {}", files);

        int size = files.size();

        Map<Integer, LanguageInfo> infoMap = new HashMap<>(size, 2.0f);
        Map<String, Map<Integer, String>> i18n = new HashMap<>(size, 2.0f);
        LanguageInfo defaultLanguageInfo = null;

        Map<String, String> messages;
        String identity;
        LanguageInfo languageInfo;

        for (File f : files) {
            if (isNull(f))
                continue;

            messages = parseProp(f);
            identity = LANGUAGE_IDENTITY_PARSER.apply(f.getName());

            languageInfo = new LanguageInfo(ofNullable(messages.get(LANGUAGE_NAME_KEY)).orElse(EMPTY_VALUE.value),
                    identity, ofNullable(messages.get(LANGUAGE_ICON_KEY)).orElse(EMPTY_VALUE.value));

            infoMap.put(LANGUAGE_PRIORITY_PARSER.apply(messages), languageInfo);

            i18n.put(lowerCase(identity), MESSAGES_CONVERTER.apply(messages));

            if (DEFAULT_LANGUAGE.equals(ofNullable(languageInfo.getIdentity()).map(String::toLowerCase).orElse(EMPTY_VALUE.value)))
                defaultLanguageInfo = languageInfo;
        }

        List<LanguageInfo> supportLanguages = SUPPORT_LANGUAGES_PARSER.apply(infoMap);

        SIMPLE_LANGUAGE_AND_LANGUAGE_MAPPING_PARSER.apply(supportLanguages)
                .forEach((key, value) -> i18n.put(key, i18n.get(value)));

        if (BlueChecker.isEmpty(supportLanguages))
            throw new RuntimeException("supportLanguages can't be empty");

        if (isNull(defaultLanguageInfo))
            defaultLanguageInfo = supportLanguages.get(0);

        I_18_N = i18n;
        SUPPORT_LANGUAGES = supportLanguages;
        DEFAULT_LANGUAGE_INFO = defaultLanguageInfo;

        LOGGER.info("I_18_N = {}", I_18_N);
        LOGGER.info("SUPPORT_LANGUAGES = {}", SUPPORT_LANGUAGES);
        LOGGER.info("DEFAULT_LANGUAGE_INFO = {}", DEFAULT_LANGUAGE_INFO);
    };

    private static final Predicate<String> CLASS_PATH_PRE = location ->
            startsWith(location, CLASS_PATH_PREFIX.prefix);

    private static final Consumer<String> MESSAGES_LOADER = location -> {
        if (CLASS_PATH_PRE.test(location)) {
            CLASS_PATH_MESSAGES_LOADER.accept(location);
        } else {
            FILE_MESSAGES_LOADER.accept(location);
        }
    };

    private static final Function<List<String>, Map<Integer, String>> MESSAGES_GETTER = languages -> {
        if (BlueChecker.isNotEmpty(languages)) {
            Map<Integer, String> message;
            for (String language : languages)
                if (isNotNull(message = I_18_N.get(lowerCase(language))))
                    return message;
        }

        return I_18_N.get(DEFAULT_LANGUAGE);
    };

    private static final BiFunction<Integer, List<String>, String> MESSAGE_GETTER = (code, languages) ->
            ofNullable(MESSAGES_GETTER.apply(languages))
                    .map(messages -> ofNullable(messages.get(code)).orElseGet(() -> messages.get(DEFAULT_CODE)))
                    .orElse(DEFAULT_MESSAGE).intern();

    private static final Predicate<ElementKey[]> NON_KEY_REPLACEMENTS_PRE = replacements ->
            isNull(replacements) || replacements.length == 0;

    private static final Predicate<String[]> NON_STR_REPLACEMENTS_PRE = replacements ->
            isNull(replacements) || replacements.length == 0;

    private static final BiFunction<String, String[], String> FILLING_FUNC = (msg, replacements) -> {
        try {
            return format(msg, (Object[]) replacements);
        } catch (Exception e) {
            return msg;
        }
    };

    /**
     * load i18n messages
     */
    static void load(String location) {
        if (isBlank(location))
            throw new RuntimeException("location can't be blank");

        MESSAGES_LOADER.accept(location);
    }

    /**
     * get message by language
     *
     * @param serverRequest
     * @return
     */
    static Map<Integer, String> listMessage(ServerRequest serverRequest) {
        return MESSAGES_GETTER.apply(getAcceptLanguages(serverRequest));
    }

    /**
     * support languages
     *
     * @return
     */
    static List<LanguageInfo> supportLanguages() {
        return SUPPORT_LANGUAGES;
    }

    /**
     * get default language
     *
     * @return
     */
    static LanguageInfo defaultLanguage() {
        return DEFAULT_LANGUAGE_INFO;
    }

    /**
     * get message by default
     *
     * @param code
     * @return
     */
    static String resolveToMessage(Integer code) {
        return MESSAGE_GETTER.apply(code, emptyList()).intern();
    }

    /**
     * get message by default
     *
     * @param code
     * @return
     */
    static String resolveToMessage(Integer code, ElementKey[] replacements) {
        String msg = MESSAGE_GETTER.apply(code, emptyList()).intern();
        return NON_KEY_REPLACEMENTS_PRE.test(replacements) ? msg : FILLING_FUNC.apply(msg, resolveToValues(replacements));
    }

    /**
     * get message by default
     *
     * @param code
     * @return
     */
    static String resolveToMessage(Integer code, String[] replacements) {
        String msg = MESSAGE_GETTER.apply(code, emptyList()).intern();
        return NON_STR_REPLACEMENTS_PRE.test(replacements) ? msg : FILLING_FUNC.apply(msg, replacements);
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param languages
     * @return
     */
    static String resolveToMessage(Integer code, List<String> languages) {
        return MESSAGE_GETTER.apply(code, languages).intern();
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param languages
     * @param replacements
     * @return
     */
    static String resolveToMessage(Integer code, List<String> languages, ElementKey[] replacements) {
        String msg = MESSAGE_GETTER.apply(code, languages).intern();
        return NON_KEY_REPLACEMENTS_PRE.test(replacements) ? msg : FILLING_FUNC.apply(msg, resolveToValues(replacements, languages));
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param languages
     * @param replacements
     * @return
     */
    static String resolveToMessage(Integer code, List<String> languages, String[] replacements) {
        String msg = MESSAGE_GETTER.apply(code, languages).intern();
        return NON_STR_REPLACEMENTS_PRE.test(replacements) ? msg : FILLING_FUNC.apply(msg, replacements);
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param serverRequest
     * @return
     */
    static String resolveToMessage(Integer code, ServerRequest serverRequest) {
        return MESSAGE_GETTER.apply(code, getAcceptLanguages(serverRequest)).intern();
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param serverRequest
     * @param replacements
     * @return
     */
    static String resolveToMessage(Integer code, ServerRequest serverRequest, ElementKey[] replacements) {
        String msg = MESSAGE_GETTER.apply(code, getAcceptLanguages(serverRequest)).intern();
        return NON_KEY_REPLACEMENTS_PRE.test(replacements) ? msg : FILLING_FUNC.apply(msg, resolveToValues(replacements, serverRequest));
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param serverRequest
     * @param replacements
     * @return
     */
    static String resolveToMessage(Integer code, ServerRequest serverRequest, String[] replacements) {
        String msg = MESSAGE_GETTER.apply(code, getAcceptLanguages(serverRequest)).intern();
        return NON_STR_REPLACEMENTS_PRE.test(replacements) ? msg : FILLING_FUNC.apply(msg, replacements);
    }

}