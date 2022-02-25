package com.blue.base.common.message;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.message.LanguageInfo;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.FileGetter.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.parseProp;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getAcceptLanguages;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.SummerAttr.LANGUAGE;
import static com.blue.base.constant.base.Symbol.*;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.*;
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

    private static final String MESSAGES_URI = "classpath:i18n";
    private static final String DEFAULT_LANGUAGE = lowerCase(replace(LANGUAGE, PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity));
    private static final int DEFAULT_CODE = INTERNAL_SERVER_ERROR.code;
    private static final String DEFAULT_MESSAGE = INTERNAL_SERVER_ERROR.message;

    private static final String LANGUAGE_NAME_KEY = "NAME";
    private static final String LANGUAGE_ORDER_KEY = "ORDER";
    private static final String LANGUAGE_LINK_KEY = "LINK";

    private static List<LanguageInfo> SUPPORT_LANGUAGES;

    private static final UnaryOperator<String> PRE_NAME_PARSER = n -> {
        int idx = lastIndexOf(n, SCHEME_SEPARATOR.identity);
        return replace(idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : "") : n, PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity);
    };

    private static final Function<Map<String, String>, Integer> LANGUAGE_ORDER_PARSER = map ->
            ofNullable(map.get(LANGUAGE_ORDER_KEY))
                    .map(order -> {
                        try {
                            return parseInt(order);
                        } catch (NumberFormatException e) {
                            return Integer.MAX_VALUE;
                        }
                    }).orElse(Integer.MAX_VALUE);

    private static final Function<Map<String, String>, Map<Integer, String>> MESSAGES_CONVERTER = messages ->
            messages.entrySet().stream()
                    .filter(e -> {
                        try {
                            parseInt(e.getKey());
                            return true;
                        } catch (NumberFormatException ex) {
                            return false;
                        }
                    })
                    .collect(toMap(e -> parseInt(e.getKey()), Map.Entry::getValue, (a, b) -> a));

    private static final Function<String, Map<String, Map<Integer, String>>> MESSAGES_LOADER = uri -> {
        Map<Integer, LanguageInfo> infos = new HashMap<>();

        Map<String, Map<Integer, String>> i18n = getFiles(uri, true).stream()
                .filter(Objects::nonNull)
                .collect(toMap(f -> lowerCase(PRE_NAME_PARSER.apply(f.getName())),
                        f -> {
                            Map<String, String> map = parseProp(f);

                            infos.put(
                                    LANGUAGE_ORDER_PARSER.apply(map),
                                    new LanguageInfo(
                                            ofNullable(map.get(LANGUAGE_NAME_KEY)).orElse(""),
                                            PRE_NAME_PARSER.apply(f.getName()),
                                            ofNullable(map.get(LANGUAGE_LINK_KEY)).orElse("")));

                            return MESSAGES_CONVERTER.apply(map);
                        }, (a, b) -> a));


        SUPPORT_LANGUAGES = infos.entrySet().stream()
                .sorted((a, b) -> {
                    if (DEFAULT_LANGUAGE.equals(lowerCase(a.getValue().getIdentity())))
                        return Integer.MIN_VALUE;

                    if (DEFAULT_LANGUAGE.equals(lowerCase(b.getValue().getIdentity())))
                        return Integer.MAX_VALUE;

                    return a.getKey().compareTo(b.getKey());
                })
                .map(Map.Entry::getValue).collect(toList());

        LOGGER.info("SUPPORT_LANGUAGES = {}");

        return i18n;
    };

    private static final Map<String, Map<Integer, String>> I_18_N = MESSAGES_LOADER.apply(MESSAGES_URI);

    private static final Function<List<String>, Map<Integer, String>> MESSAGES_GETTER = languages -> {
        if (BlueChecker.isNotEmpty(languages)) {
            Map<Integer, String> messages;
            for (String language : languages)
                if ((messages = I_18_N.get(language)) != null)
                    return messages;
        }

        return I_18_N.get(DEFAULT_LANGUAGE);
    };

    private static final BiFunction<String, String[], String> FILLING_FUNC = (msg, replacements) -> {
        try {
            return format(msg, (Object[]) replacements);
        } catch (Exception e) {
            return msg;
        }
    };

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
        return resolveToMessage(code, emptyList());
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param languages
     * @return
     */
    public static String resolveToMessage(Integer code, List<String> languages) {
        return ofNullable(MESSAGES_GETTER.apply(languages))
                .map(messages -> messages.get(ofNullable(code).orElse(DEFAULT_CODE)).intern())
                .orElse(DEFAULT_MESSAGE).intern();
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
        String msg = resolveToMessage(code, languages).intern();
        return replacements == null || replacements.length == 0 ? msg : FILLING_FUNC.apply(msg, replacements);
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param serverRequest
     * @return
     */
    public static String resolveToMessage(Integer code, ServerRequest serverRequest) {
        return resolveToMessage(code, getAcceptLanguages(serverRequest)).intern();
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
        String msg = resolveToMessage(code, serverRequest).intern();
        return replacements == null || replacements.length == 0 ? msg : FILLING_FUNC.apply(msg, replacements);
    }

}