package com.blue.base.common.base;

import com.blue.base.constant.base.Symbol;
import com.blue.base.model.exps.BlueException;
import org.springframework.cglib.beans.BeanCopier;
import reactor.util.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * bean copier base on cglib
 *
 * @author DarkBlue
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
        if (source == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);
        if (target == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);

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
        if (beanCopier == null)
            synchronized (BlueBeanCopier.class) {
                beanCopier = COPIERS_HOLDER.get(key);
                if (beanCopier == null) {
                    BeanCopier copier = BeanCopier.create(source, target, useConverter);
                    COPIERS_HOLDER.put(key, copier);
                    beanCopier = copier;
                    LOGGER.info("BeanCopier created, key = {}", key);
                }
            }

        return beanCopier;
    }

}
