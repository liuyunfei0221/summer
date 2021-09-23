package com.blue.base.component.exception.common;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.constant.base.ResponseElement;
import com.blue.base.model.base.BlueResult;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.blue.base.common.base.ClassGetter.getClassesByPackage;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static reactor.util.Loggers.getLogger;

/**
 * 异常处理器
 *
 * @author DarkBlue
 */
@SuppressWarnings({"SameParameterValue", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class ExceptionProcessor {

    private static final Logger LOGGER = getLogger(ExceptionProcessor.class);

    /**
     * 异常处理器实现类路径
     */
    private static final String DIR_NAME = "com.blue.base.component.exception.handler.impl";

    private static final Map<String, ExceptionHandler> MAPPING = generatorMapping(DIR_NAME);

    private static final ResponseElement INTERNAL_SERVER_ERROR_RES = INTERNAL_SERVER_ERROR;

    /**
     * 初始化异常映射器
     *
     * @param dirName
     * @return
     */
    private static Map<String, ExceptionHandler> generatorMapping(String dirName) {
        List<Class<?>> classes = getClassesByPackage(dirName, true);
        LOGGER.info("Map<String, ExceptionHandler> generatorMapping(String dirName), 开始加载异常处理类, dirName = {}", dirName);
        return classes
                .stream()
                .filter(clz ->
                        !clz.isInterface() &&
                                of(clz.getInterfaces()).anyMatch(inter -> ExceptionHandler.class.getName().equals(inter.getName())))
                .map(clz -> {
                    try {
                        LOGGER.info("generatorMapping(String dirName), 加载异常处理类, clz = {}", clz.getName());
                        return (ExceptionHandler) clz.getConstructor().newInstance();
                    } catch (Exception e) {
                        LOGGER.info("generatorMapping(String dirName), 加载异常处理类失败, clz = {}, e = {}", clz.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(toMap(ExceptionHandler::exceptionName, eh -> eh, (a, b) -> a));
    }

    /**
     * 处理
     *
     * @param throwable
     * @return
     */
    public static ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("ExceptionHandleInfo handle(Throwable throwable), throwable = {}", throwable);

        Throwable t = throwable;
        Throwable cause;
        while ((cause = t.getCause()) != null)
            t = cause;

        ExceptionHandler handler = MAPPING.get(t.getClass().getName());
        if (handler != null)
            try {
                return handler.handle(t);
            } catch (Exception e) {
                LOGGER.error("handle(Throwable throwable), 异常处理失败, t = {}, e = {}", t, e);
            }

        LOGGER.error("handle(Throwable throwable), 未能识别的异常, t = {}", t);
        return new ExceptionHandleInfo(INTERNAL_SERVER_ERROR_RES.status,
                new BlueResult<>(INTERNAL_SERVER_ERROR_RES.code, null, INTERNAL_SERVER_ERROR_RES.message));
    }

}
