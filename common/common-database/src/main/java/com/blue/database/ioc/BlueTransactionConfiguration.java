package com.blue.database.ioc;

import com.blue.database.api.conf.TransConf;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.Symbol.ASTERISK;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.context.annotation.AdviceMode.PROXY;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * transaction configuration base on expression
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "DefaultAnnotationParam"})
@EnableTransactionManagement(proxyTargetClass = true, mode = PROXY, order = LOWEST_PRECEDENCE)
@Order(HIGHEST_PRECEDENCE)
public class BlueTransactionConfiguration {

    private static final Logger LOGGER = getLogger(BlueTransactionConfiguration.class);

    @Bean
    @ConditionalOnBean(value = {TransConf.class})
    TransactionInterceptor txAdvice(TransactionManager txManager, TransConf transConf) {
        LOGGER.info("txManager = {}, transactionConf = {}", txManager, transConf);
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
                .map(m -> m + ASTERISK.identity)
                .forEach(m -> source.addTransactionalMethod(m, attributeWithTrans));

        return new TransactionInterceptor(txManager, source);
    }

    @Bean
    @ConditionalOnBean(value = {TransConf.class})
    Advisor advisor(TransactionInterceptor txAdvice, TransConf transConf) {
        LOGGER.info("txAdvice = {}, transactionConf = {}", txAdvice, transConf);
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
        if (isNull(isolation))
            throw new RuntimeException("isolation can't be null");

        Propagation propagation = conf.getPropagation();
        if (isNull(propagation))
            throw new RuntimeException("propagation can't be null");

        Integer transTimeout = conf.getTransTimeout();
        if (isNull(transTimeout) || transTimeout < 1)
            throw new RuntimeException("transTimeout can't be null or less than 1");

        List<String> methodPreWithTrans = conf.getMethodPreWithTrans();
        if (isEmpty(methodPreWithTrans))
            throw new RuntimeException("corePoolSize can't be null or less than 1");

        String pointCutExpression = conf.getPointCutExpression();
        if (isBlank(pointCutExpression))
            throw new RuntimeException("pointCutExpression can't be null or ''");
    }

}
