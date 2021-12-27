package com.blue.database.ioc;

import com.blue.base.model.exps.BlueException;
import com.blue.database.api.conf.TransConf;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.List;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * transaction configuration base on expression
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "SpringJavaInjectionPointsAutowiringInspection", "SpringFacetCodeInspection"})
@ConditionalOnBean(value = {TransConf.class})
@Configuration
public class BlueTransactionConfiguration {

    private static final Logger LOGGER = getLogger(BlueTransactionConfiguration.class);

    @Bean
    TransactionInterceptor txAdvice(TransactionManager txManager, TransConf transConf) {
        LOGGER.info("txAdvice(TransactionManager txManager, TransactionConf transactionConf), txManager = {}, transactionConf = {}", txManager, transConf);
        assertConf(transConf);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        RuleBasedTransactionAttribute attributeWithTrans = new RuleBasedTransactionAttribute();
        attributeWithTrans.setIsolationLevel(transConf.getIsolation().value());
        attributeWithTrans.setPropagationBehavior(transConf.getPropagation().value());
        attributeWithTrans.setTimeout(transConf.getTransTimeout());
        attributeWithTrans.setRollbackRules(of(new RollbackRuleAttribute(Throwable.class)));
        attributeWithTrans.setReadOnly(false);
        ofNullable(transConf.getMethodPreWithTrans())
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(StringUtils::isNotBlank)
                .distinct()
                .map(m -> m + "*")
                .forEach(m -> source.addTransactionalMethod(m, attributeWithTrans));

        return new TransactionInterceptor(txManager, source);
    }

    @Bean
    Advisor advisor(TransactionInterceptor txAdvice, TransConf transConf) {
        LOGGER.info("advisor(TransactionInterceptor txAdvice, TransactionConf transactionConf), txAdvice = {}, transactionConf = {}", txAdvice, transConf);
        assertConf(transConf);

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(transConf.getPointCutExpression());

        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }

    /**
     * assert params
     *
     * @param conf
     */
    private static void assertConf(TransConf conf) {
        Isolation isolation = conf.getIsolation();
        if (isolation == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "isolation can't be null");

        Propagation propagation = conf.getPropagation();
        if (propagation == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "propagation can't be null");

        Integer transTimeout = conf.getTransTimeout();
        if (transTimeout == null || transTimeout < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "transTimeout can't be null or less than 1");

        List<String> methodPreWithTrans = conf.getMethodPreWithTrans();
        if (isEmpty(methodPreWithTrans))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "corePoolSize can't be null or less than 1");

        String pointCutExpression = conf.getPointCutExpression();
        if (isBlank(pointCutExpression))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "pointCutExpression can't be null or ''");
    }

}
