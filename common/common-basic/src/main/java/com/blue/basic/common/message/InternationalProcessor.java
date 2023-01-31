package com.blue.basic.common.message;

import com.blue.basic.constant.common.ElementKey;
import com.blue.basic.model.message.LanguageInfo;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static com.blue.basic.constant.common.Symbol.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.substring;

/**
 * i18n processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "unused"})
public final class InternationalProcessor {

    private static final UnaryOperator<String> LANGUAGE_IDENTITY_PARSER = s -> {
        if (isBlank(s))
            throw new RuntimeException("s can't be blank");

        int idx = lastIndexOf(s, PERIOD.identity);
        return replace(idx >= 0 ? (idx > 0 ? substring(s, 0, idx) : s) : s, PAR_CONCATENATION.identity, HYPHEN.identity);
    };

    /**
     * parse language identity
     *
     * @return
     */
    public static String parseLanguageIdentity(String language) {
        return LANGUAGE_IDENTITY_PARSER.apply(language);
    }

    /**
     * load i18n messages
     */
    public static void loadMessage(String location) {
        MessageProcessor.load(location);
    }

    /**
     * load i18n elements
     */
    public static void loadElement(String location) {
        ElementProcessor.load(location);
    }

    /**
     * get default language identity
     *
     * @return
     */
    public static String defaultLanguageIdentity() {
        return MessageProcessor.DEFAULT_LANGUAGE;
    }

    /**
     * support languages
     *
     * @return
     */
    public static List<LanguageInfo> supportLanguages() {
        return MessageProcessor.supportLanguages();
    }

    /**
     * get default language
     *
     * @return
     */
    public static LanguageInfo defaultLanguage() {
        return MessageProcessor.defaultLanguage();
    }

    /**
     * get message by language
     *
     * @param serverRequest
     * @return
     */
    public static Map<Integer, String> listMessage(ServerRequest serverRequest) {
        return MessageProcessor.listMessage(serverRequest);
    }

    /**
     * get message by default
     *
     * @param code
     * @return
     */
    public static String resolveToMessage(Integer code) {
        return MessageProcessor.resolveToMessage(code);
    }

    /**
     * get message by default
     *
     * @param code
     * @return
     */
    public static String resolveToMessage(Integer code, ElementKey[] replacements) {
        return MessageProcessor.resolveToMessage(code, replacements);
    }

    /**
     * get message by default
     *
     * @param code
     * @return
     */
    public static String resolveToMessage(Integer code, String[] replacements) {
        return MessageProcessor.resolveToMessage(code, replacements);
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param languages
     * @return
     */
    public static String resolveToMessage(Integer code, List<String> languages) {
        return MessageProcessor.resolveToMessage(code, languages);
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param languages
     * @param replacements
     * @return
     */
    public static String resolveToMessage(Integer code, List<String> languages, ElementKey[] replacements) {
        return MessageProcessor.resolveToMessage(code, languages, replacements);
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
        return MessageProcessor.resolveToMessage(code, languages, replacements);
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param serverRequest
     * @return
     */
    public static String resolveToMessage(Integer code, ServerRequest serverRequest) {
        return MessageProcessor.resolveToMessage(code, serverRequest);
    }

    /**
     * get message by i18n
     *
     * @param code
     * @param serverRequest
     * @param replacements
     * @return
     */
    public static String resolveToMessage(Integer code, ServerRequest serverRequest, ElementKey[] replacements) {
        return MessageProcessor.resolveToMessage(code, serverRequest, replacements);
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
        return MessageProcessor.resolveToMessage(code, serverRequest, replacements);
    }

    /**
     * select all elements by default language
     *
     * @return
     */
    public static Map<String, String> selectAllElement() {
        return ElementProcessor.selectAllElement();
    }

    /**
     * select all elements by languages
     *
     * @param languages
     * @return
     */
    public static Map<String, String> selectAllElement(List<String> languages) {
        return ElementProcessor.selectAllElement(languages);
    }

    /**
     * select all elements by request
     *
     * @param serverRequest
     * @return
     */
    public static Map<String, String> selectAllElement(ServerRequest serverRequest) {
        return ElementProcessor.selectAllElement(serverRequest);
    }

    /**
     * select elements by default language and keys
     *
     * @param keys
     * @return
     */
    public static Map<String, String> selectElement(List<String> keys) {
        return ElementProcessor.selectAllElement(keys);
    }

    /**
     * select elements by languages and keys
     *
     * @param keys
     * @param languages
     * @return
     */
    public static Map<String, String> selectElement(List<String> keys, List<String> languages) {
        return ElementProcessor.selectElement(keys, languages);
    }

    /**
     * select elements by request and request
     *
     * @param keys
     * @param serverRequest
     * @return
     */
    public static Map<String, String> selectElement(List<String> keys, ServerRequest serverRequest) {
        return ElementProcessor.selectElement(keys, serverRequest);
    }

    /**
     * get element value by i18n
     *
     * @param key
     * @return
     */
    public static String resolveToValue(ElementKey key) {
        return ElementProcessor.resolveToValue(key);
    }

    /**
     * get element values by i18n
     *
     * @param keys
     * @return
     */
    public static String[] resolveToValues(ElementKey[] keys) {
        return ElementProcessor.resolveToValues(keys);
    }

    /**
     * get element value by i18n
     *
     * @param key
     * @param languages
     * @return
     */
    public static String resolveToValue(ElementKey key, List<String> languages) {
        return ElementProcessor.resolveToValue(key, languages);
    }

    /**
     * get element values by i18n
     *
     * @param keys
     * @param languages
     * @return
     */
    public static String[] resolveToValues(ElementKey[] keys, List<String> languages) {
        return ElementProcessor.resolveToValues(keys, languages);
    }

    /**
     * get element value by i18n
     *
     * @param key
     * @param serverRequest
     * @return
     */
    public static String resolveToValue(ElementKey key, ServerRequest serverRequest) {
        return ElementProcessor.resolveToValue(key, serverRequest);
    }

    /**
     * get element values by i18n
     *
     * @param keys
     * @param serverRequest
     * @return
     */
    public static String[] resolveToValues(ElementKey[] keys, ServerRequest serverRequest) {
        return ElementProcessor.resolveToValues(keys, serverRequest);
    }

}