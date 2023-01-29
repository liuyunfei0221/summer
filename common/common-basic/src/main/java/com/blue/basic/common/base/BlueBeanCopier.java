package com.blue.basic.common.base;

import com.blue.basic.constant.common.Symbol;
import com.blue.basic.model.exps.BlueException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import reactor.util.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
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
     * @param target
     * @param useConverter
     * @return
     */
    public static BeanCopier create(Class source, Class target, boolean useConverter) {
        String key = generateKey(source, target, useConverter);

        BeanCopier beanCopier = COPIERS_HOLDER.get(key);
        if (isNull(beanCopier))
            synchronized (BlueBeanCopier.class) {
                beanCopier = COPIERS_HOLDER.get(key);
                if (isNull(beanCopier)) {
                    BeanCopier copier = BeanCopier.create(source, target, useConverter);
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
     * @param from
     * @param to
     * @param converter
     */
    public static void copy(Object from, Object to, Converter converter) {
        create(from.getClass(), to.getClass(), isNotNull(converter))
                .copy(from, to, converter);
    }

}
