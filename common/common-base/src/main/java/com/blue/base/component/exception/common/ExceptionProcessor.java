package com.blue.base.component.exception.common;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionInfo;
import com.blue.base.model.base.ExceptionResponse;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.blue.base.common.base.ClassGetter.getClassesByPackage;
import static com.blue.base.common.base.FileProcessor.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.loadProp;
import static com.blue.base.common.base.PropertiesProcessor.parseProp;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.SCHEME_SEPARATOR;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;
import static reactor.util.Loggers.getLogger;

/**
 * global exp processor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"SameParameterValue", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class ExceptionProcessor {

    private static final Logger LOGGER = getLogger(ExceptionProcessor.class);

    /**
     * handlers package
     */
    private static final String DIR_NAME = "com.blue.base.component.exception.handler.impl";

    private static final Map<String, ExceptionHandler> MAPPING = generatorMapping(DIR_NAME);

    private static final ExceptionInfo UNKNOWN_EXP_HANDLE_INFO = new ExceptionInfo(INTERNAL_SERVER_ERROR.status,
            INTERNAL_SERVER_ERROR.code, null);

    /**
     * init mapping
     *
     * @param dirName
     * @return
     */
    private static Map<String, ExceptionHandler> generatorMapping(String dirName) {
        List<Class<?>> classes = getClassesByPackage(dirName, true);
        LOGGER.info("Map<String, ExceptionHandler> generatorMapping(String dirName), dirName = {}", dirName);
        String expHandlerName = ExceptionHandler.class.getName();
        return classes
                .stream()
                .filter(clz ->
                        !clz.isInterface() &&
                                of(clz.getInterfaces()).anyMatch(inter -> expHandlerName.equals(inter.getName())))
                .map(clz -> {
                    try {
                        LOGGER.info("generatorMapping(String dirName), Load exception handler class, clz = {}", clz.getName());
                        return (ExceptionHandler) clz.getConstructor().newInstance();
                    } catch (Exception e) {
                        LOGGER.info("generatorMapping(String dirName), Load exception handler class failed, clz = {}, e = {}", clz.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(toMap(ExceptionHandler::exceptionName, eh -> eh, (a, b) -> a));
    }

    private static final String MESSAGES_URI = "classpath:i18n";
    private static final String DEFAULT_MESSAGES = "en_us";
    private static final int DEFAULT_KEY = INTERNAL_SERVER_ERROR.code;
    private static final String DEFAULT_MESSAGE = INTERNAL_SERVER_ERROR.message;

    private static final ExceptionResponse DEFAULT_EXP_RESP = new ExceptionResponse();

    private static final UnaryOperator<String> PRE_NAME_PARSER = n -> {
        int idx = lastIndexOf(n, SCHEME_SEPARATOR.identity);
        return idx >= 0 ? (idx > 0 ? substring(n, 0, idx) : "") : n;
    };

    private static final Function<Map<String, String>, Map<Integer, String>> MESSAGES_CONVERTER = messages ->
            messages.entrySet().stream().collect(Collectors.toMap(e -> {
                try {
                    return parseInt(e.getKey());
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("code must be int");
                }
            }, Map.Entry::getValue, (a, b) -> a));

    private static final Function<String, Map<String, Map<Integer, String>>> MESSAGES_LOADER = uri ->
            getFiles(uri, true).stream()
                    .collect(toMap(f -> requireNonNull(PRE_NAME_PARSER).apply(f.getName()).toLowerCase(),
                            f -> MESSAGES_CONVERTER.apply(parseProp(loadProp(f))),
                            (a, b) -> a));

    private static final Map<String, Map<Integer, String>> I_18_N = MESSAGES_LOADER.apply(MESSAGES_URI);

    private static final Function<List<String>, Map<Integer, String>> MESSAGES_GETTER = languages -> {
        if (languages != null) {
            Map<Integer, String> messages;
            for (String language : languages)
                if ((messages = I_18_N.get(language)) != null)
                    return messages;
        }

        return I_18_N.get(DEFAULT_MESSAGES);
    };

    private static final BiFunction<List<String>, Integer, String> MESSAGE_GETTER = (languages, key) ->
            ofNullable(MESSAGES_GETTER.apply(languages))
                    .map(messages -> messages.get(ofNullable(key).orElse(DEFAULT_KEY)))
                    .orElse(DEFAULT_MESSAGE);

    private static final BiFunction<String, String[], String> FILLING_FUNC = (msg, fillings) -> {
        try {
            return format(msg, (Object[]) fillings);
        } catch (Exception e) {
            return msg;
        }
    };

    private static final BiFunction<List<String>, ExceptionInfo, ExceptionResponse> EXP_RES_GETTER = (languages, info) ->
            ofNullable(info)
                    .map(i -> {
                        Integer code = i.getCode();
                        String[] fillings = info.getFillings();
                        String msg = MESSAGE_GETTER.apply(languages, code);
                        return new ExceptionResponse(i.getStatus(), code,
                                fillings == null ? msg : FILLING_FUNC.apply(msg, fillings));
                    }).orElse(DEFAULT_EXP_RESP);

    /**
     * process
     *
     * @param throwable
     * @return
     */
    public static ExceptionResponse handle(Throwable throwable, List<String> languages) {
        LOGGER.info("ExceptionHandleInfo handle(Throwable throwable), throwable = {}", throwable);

        Throwable t = throwable;
        Throwable cause;
        while ((cause = t.getCause()) != null)
            t = cause;

        ExceptionHandler handler = MAPPING.get(t.getClass().getName());
        if (handler != null)
            try {
                return EXP_RES_GETTER.apply(languages, handler.handle(t));
            } catch (Exception e) {
                LOGGER.error("handle(Throwable throwable), Exception handling failed, t = {}, e = {}", t, e);
            }

        LOGGER.error("handle(Throwable throwable), unknown exception, t = {}", t);
        return EXP_RES_GETTER.apply(languages, UNKNOWN_EXP_HANDLE_INFO);
    }

}
