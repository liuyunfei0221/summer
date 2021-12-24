package com.blue.base.common.message;

import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.FileProcessor.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.parseProp;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getAcceptLanguages;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.SummerAttr.LANGUAGE;
import static com.blue.base.constant.base.Symbol.*;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;

/**
 * i18n message processor
 *
 * @author liuyunfei
 * @date 2021/12/19
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class MessageProcessor {

    private static final String MESSAGES_URI = "classpath:i18n";
    private static final String DEFAULT_LANGUAGE = LANGUAGE.replace(PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity).toLowerCase();
    private static final int DEFAULT_KEY = INTERNAL_SERVER_ERROR.code;
    private static final String DEFAULT_MESSAGE = INTERNAL_SERVER_ERROR.message;

    private static final UnaryOperator<String> PRE_NAME_PARSER = n -> {
        int idx = lastIndexOf(n, SCHEME_SEPARATOR.identity);
        String name = idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : "") : n;
        return name.replace(PAR_CONCATENATION.identity, PAR_CONCATENATION_DATABASE_URL.identity);
    };

    private static final Function<Map<String, String>, Map<Integer, String>> MESSAGES_CONVERTER = messages ->
            messages.entrySet().stream().collect(toMap(e -> {
                try {
                    return parseInt(e.getKey());
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("code must be int, entry = " + e);
                }
            }, Map.Entry::getValue, (a, b) -> a));

    private static final Function<String, Map<String, Map<Integer, String>>> MESSAGES_LOADER = uri ->
            getFiles(uri, true).stream()
                    .filter(Objects::nonNull)
                    .collect(toMap(f -> PRE_NAME_PARSER.apply(f.getName()).toLowerCase(),
                            f -> MESSAGES_CONVERTER.apply(parseProp(f)),
                            (a, b) -> a));

    private static final Map<String, Map<Integer, String>> I_18_N = MESSAGES_LOADER.apply(MESSAGES_URI);

    private static final Function<List<String>, Map<Integer, String>> MESSAGES_GETTER = languages -> {
        if (languages != null) {
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
     * get message by i18n
     *
     * @param key
     * @param languages
     * @return
     */
    public static String resolveToMessage(Integer key, List<String> languages) {
        return ofNullable(MESSAGES_GETTER.apply(languages))
                .map(messages -> messages.get(ofNullable(key).orElse(DEFAULT_KEY)))
                .orElse(DEFAULT_MESSAGE).intern();
    }

    /**
     * get message by i18n
     *
     * @param key
     * @param languages
     * @param replacements
     * @return
     */
    public static String resolveToMessage(Integer key, List<String> languages, String[] replacements) {
        String msg = resolveToMessage(key, languages).intern();
        return replacements == null || replacements.length == 0 ? msg : FILLING_FUNC.apply(msg, replacements);
    }

    /**
     * get message by i18n
     *
     * @param key
     * @param serverRequest
     * @return
     */
    public static String resolveToMessage(Integer key, ServerRequest serverRequest) {
        return ofNullable(MESSAGES_GETTER.apply(getAcceptLanguages(serverRequest)))
                .map(messages -> messages.get(ofNullable(key).orElse(DEFAULT_KEY)))
                .orElse(DEFAULT_MESSAGE).intern();
    }

    /**
     * get message by i18n
     *
     * @param key
     * @param serverRequest
     * @param replacements
     * @return
     */
    public static String resolveToMessage(Integer key, ServerRequest serverRequest, String[] replacements) {
        String msg = resolveToMessage(key, serverRequest).intern();
        return replacements == null || replacements.length == 0 ? msg : FILLING_FUNC.apply(msg, replacements);
    }

}
