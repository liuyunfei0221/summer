package com.blue.base.common.message;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.message.LanguageInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

import static com.blue.base.common.base.FileGetter.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.parseProp;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getAcceptLanguages;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.SummerAttr.LANGUAGE;
import static com.blue.base.constant.base.Symbol.*;
import static java.lang.Integer.*;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
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
 * @date 2021/12/19
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class MessageProcessor {

    private static final Logger LOGGER = getLogger(MessageProcessor.class);

    private static final String MESSAGES_URI = "classpath:i18n/message";
    private static final String DEFAULT_LANGUAGE = lowerCase(replace(LANGUAGE, PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity));
    private static final int DEFAULT_CODE = INTERNAL_SERVER_ERROR.code;
    private static final String DEFAULT_MESSAGE = INTERNAL_SERVER_ERROR.message;

    private static final String LANGUAGE_NAME_KEY = "NAME";
    private static final String LANGUAGE_PRIORITY_KEY = "PRIORITY";
    private static final String LANGUAGE_ICON_KEY = "ICON";

    private static volatile Map<String, Map<Integer, String>> I_18_N;
    private static volatile List<LanguageInfo> SUPPORT_LANGUAGES;

    private static final UnaryOperator<String> LANGUAGE_IDENTITY_PARSER = n -> {
        int idx = lastIndexOf(n, SCHEME_SEPARATOR.identity);
        return replace(idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : "") : n, PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity);
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

    private static final Consumer<String> MESSAGES_LOADER = uri -> {
        List<File> files = getFiles(uri, true);
        int size = files.size();

        Map<Integer, LanguageInfo> infos = new HashMap<>(size);
        Map<String, Map<Integer, String>> i18n = new HashMap<>(size);

        Map<String, String> messages;
        String identity;
        for (File f : files) {
            if (f == null)
                continue;

            messages = parseProp(f);
            identity = LANGUAGE_IDENTITY_PARSER.apply(f.getName());

            infos.put(LANGUAGE_PRIORITY_PARSER.apply(messages),
                    new LanguageInfo(ofNullable(messages.get(LANGUAGE_NAME_KEY)).orElse(""),
                            identity, ofNullable(messages.get(LANGUAGE_ICON_KEY)).orElse("")));

            i18n.put(lowerCase(identity), MESSAGES_CONVERTER.apply(messages));
        }

        List<LanguageInfo> supportLanguages = infos.entrySet().stream()
                .sorted((a, b) -> {
                    if (DEFAULT_LANGUAGE.equals(lowerCase(a.getValue().getIdentity())))
                        return MIN_VALUE;

                    if (DEFAULT_LANGUAGE.equals(lowerCase(b.getValue().getIdentity())))
                        return MAX_VALUE;

                    return a.getKey().compareTo(b.getKey());
                })
                .map(Map.Entry::getValue).collect(toList());

        I_18_N = i18n;
        SUPPORT_LANGUAGES = supportLanguages;

        LOGGER.info("I_18_N = {}", I_18_N);
        LOGGER.info("SUPPORT_LANGUAGES = {}", SUPPORT_LANGUAGES);
    };

    private static final Function<List<String>, Map<Integer, String>> MESSAGES_GETTER = languages -> {
        if (BlueChecker.isNotEmpty(languages)) {
            Map<Integer, String> message;
            for (String language : languages)
                if ((message = I_18_N.get(language)) != null)
                    return message;
        }

        return I_18_N.get(DEFAULT_LANGUAGE);
    };

    private static final BiFunction<Integer, List<String>, String> MESSAGE_GETTER = (code, languages) ->
            ofNullable(MESSAGES_GETTER.apply(languages))
                    .map(messages -> messages.get(ofNullable(code).orElse(DEFAULT_CODE)))
                    .orElse(DEFAULT_MESSAGE).intern();

    private static final Predicate<String[]> NON_REPLACEMENTS_PRE = replacements ->
            replacements == null || replacements.length == 0;

    private static final BiFunction<String, String[], String> FILLING_FUNC = (msg, replacements) -> {
        try {
            return format(msg, (Object[]) replacements);
        } catch (Exception e) {
            return msg;
        }
    };

    static {
        refresh();
    }

    /**
     * refresh i18n messages
     */
    public static void refresh() {
        MESSAGES_LOADER.accept(MESSAGES_URI);
    }

    /**
     * support languages
     *
     * @return
     */
    public static List<LanguageInfo> listSupportLanguages() {
        return SUPPORT_LANGUAGES;
    }

    /**
     * get message by default
     *
     * @param code
     * @return
     */
    public static String resolveToMessage(Integer code) {
        return MESSAGE_GETTER.apply(code, emptyList()).intern();
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param languages
     * @return
     */
    public static String resolveToMessage(Integer code, List<String> languages) {
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
    public static String resolveToMessage(Integer code, List<String> languages, String[] replacements) {
        String msg = MESSAGE_GETTER.apply(code, languages).intern();
        return NON_REPLACEMENTS_PRE.test(replacements) ? msg : FILLING_FUNC.apply(msg, replacements);
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param serverRequest
     * @return
     */
    public static String resolveToMessage(Integer code, ServerRequest serverRequest) {
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
    public static String resolveToMessage(Integer code, ServerRequest serverRequest, String[] replacements) {
        String msg = MESSAGE_GETTER.apply(code, getAcceptLanguages(serverRequest)).intern();
        return NON_REPLACEMENTS_PRE.test(replacements) ? msg : FILLING_FUNC.apply(msg, replacements);
    }

}