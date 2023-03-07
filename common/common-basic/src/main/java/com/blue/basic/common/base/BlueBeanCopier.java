package com.blue.basic.common.base;

import com.blue.basic.constant.common.Symbol;
import com.blue.basic.model.exps.BlueException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import reactor.util.Logger;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static reactor.util.Loggers.getLogger;

/**
 * bean copier base on cglib
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "rawtypes", "AliControlFlowStatementWithoutBraces", "unused"})
public final class BlueBeanCopier {

    private static final Logger LOGGER = getLogger(BlueBeanCopier.class);

    private static final String PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity;

    private static final Map<String, BeanCopier> COPIERS_HOLDER = new ConcurrentHashMap<>();

    /**
     * generate key
     *
     * @param source
     * @param target
     * @param useConverter
     * @return
     */
    private static String generateKey(Class source, Class target, boolean useConverter) {
        if (isNull(source) || isNull(target))
            throw new BlueException(BAD_REQUEST);

        return source.getName() + PAR_CONCATENATION + target.getName() + PAR_CONCATENATION + useConverter;
    }

    /**
     * create copier
     *
     * @param source
     * @param destination
     * @param useConverter
     * @return
     */
    public static BeanCopier create(Class source, Class destination, boolean useConverter) {
        String key = generateKey(source, destination, useConverter);

        BeanCopier beanCopier = COPIERS_HOLDER.get(key);
        if (isNull(beanCopier))
            synchronized (BlueBeanCopier.class) {
                beanCopier = COPIERS_HOLDER.get(key);
                if (isNull(beanCopier)) {
                    BeanCopier copier = BeanCopier.create(source, destination, useConverter);
                    COPIERS_HOLDER.put(key, copier);
                    beanCopier = copier;
                    LOGGER.info("BeanCopier created, key = {}", key);
                }
            }

        return beanCopier;
    }

    /**
     * copy object
     *
     * @param source
     * @param destination
     * @param converter
     */
    public static <T, R> void copy(T source, R destination, Converter converter) {
        if (isNull(source) || isNull(destination))
            throw new BlueException(EMPTY_PARAM);

        create(source.getClass(), destination.getClass(), isNotNull(converter))
                .copy(source, destination, converter);
    }

    /**
     * copy object
     *
     * @param source
     * @param destination
     */
    public static <T, R> void copy(T source, R destination) {
        if (isNull(source) || isNull(destination))
            throw new BlueException(EMPTY_PARAM);

        create(source.getClass(), destination.getClass(), false)
                .copy(source, destination, null);
    }

    /**
     * generate target and copy attrs
     *
     * @param source
     * @param destType
     * @param converter
     * @param <T>
     * @return
     */
    public static <T, R> R convert(T source, Class<R> destType, Converter converter) {
        if (isNull(source) || isNull(destType))
            throw new BlueException(EMPTY_PARAM);

        try {
            R to = destType.getDeclaredConstructor().newInstance();
            copy(source, to, converter);
            return to;
        } catch (Exception e) {
            throw new BlueException(EMPTY_PARAM);
        }
    }

    /**
     * generate target and copy attrs
     *
     * @param source
     * @param destType
     * @param <T>
     * @return
     */
    public static <T, R> R convert(T source, Class<R> destType) {
        if (isNull(source) || isNull(destType))
            throw new BlueException(EMPTY_PARAM);

        try {
            R to = destType.getDeclaredConstructor().newInstance();
            copy(source, to);
            return to;
        } catch (Exception e) {
            throw new BlueException(EMPTY_PARAM);
        }
    }

    /**
     * generate target and copy attrs
     *
     * @param sources
     * @param destType
     * @param converter
     * @param <T>
     * @return
     */
    public static <T, R> List<R> convertList(List<T> sources, Class<R> destType, Converter converter) {
        if (isEmpty(sources) || isNull(destType))
            throw new BlueException(EMPTY_PARAM);

        int size = sources.size();
        if (size == 0)
            return emptyList();

        try {
            Constructor<R> constructor = destType.getDeclaredConstructor();
            BeanCopier copier = create(sources.stream().filter(BlueChecker::isNotNull).map(e -> e.getClass()).findAny().orElseThrow(() -> new BlueException(EMPTY_PARAM)),
                    destType, isNotNull(converter));

            return sources.stream()
                    .map(e -> {
                        try {
                            R to = constructor.newInstance();
                            copier.copy(e, to, converter);
                            return to;
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }).collect(toList());
        } catch (Exception e) {
            throw new BlueException(EMPTY_PARAM);
        }
    }

    /**
     * generate target and copy attrs
     *
     * @param sources
     * @param destType
     * @param <T>
     * @return
     */
    public static <T, R> List<R> convertList(List<T> sources, Class<R> destType) {
        if (isEmpty(sources) || isNull(destType))
            throw new BlueException(EMPTY_PARAM);

        int size = sources.size();
        if (size == 0)
            return emptyList();

        try {
            Constructor<R> constructor = destType.getDeclaredConstructor();
            BeanCopier copier = create(sources.stream().filter(BlueChecker::isNotNull).map(e -> e.getClass()).findAny().orElseThrow(() -> new BlueException(EMPTY_PARAM)),
                    destType, false);

            return sources.stream()
                    .map(e -> {
                        try {
                            R to = constructor.newInstance();
                            copier.copy(e, to, null);
                            return to;
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }).collect(toList());
        } catch (Exception e) {
            throw new BlueException(EMPTY_PARAM);
        }
    }

}