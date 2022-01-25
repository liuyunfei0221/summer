package com.blue.base.component.exception.common;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionInfo;
import com.blue.base.model.base.ExceptionResponse;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import static com.blue.base.common.base.ClassGetter.getClassesByPackage;
import static com.blue.base.common.base.OriginalThrowableGetter.getOriginalThrowable;
import static com.blue.base.common.message.MessageProcessor.resolveToMessage;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
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

    private static final ExceptionResponse DEFAULT_EXP_RESP = new ExceptionResponse();

    private static final BiFunction<List<String>, ExceptionInfo, ExceptionResponse> EXP_RES_GETTER = (languages, info) ->
            ofNullable(info)
                    .map(i -> {
                        Integer code = i.getCode();
                        return new ExceptionResponse(i.getStatus(), code,
                                resolveToMessage(code, languages, info.getReplacements()));
                    }).orElse(DEFAULT_EXP_RESP);

    /**
     * process
     *
     * @param throwable
     * @return
     */
    public static ExceptionResponse handle(Throwable throwable, List<String> languages) {
        LOGGER.info("ExceptionHandleInfo handle(Throwable throwable), throwable = {}", throwable);

        Throwable original = getOriginalThrowable(throwable);

        ExceptionHandler handler = MAPPING.get(original.getClass().getName());
        if (handler != null)
            try {
                return EXP_RES_GETTER.apply(languages, handler.handle(original));
            } catch (Exception e) {
                LOGGER.error("handle(Throwable throwable), Exception handling failed, throwable = {}, e = {}", throwable, e);
            }

        LOGGER.error("handle(Throwable throwable), unknown exception, throwable = {}", throwable);
        return EXP_RES_GETTER.apply(languages, UNKNOWN_EXP_HANDLE_INFO);
    }

}
