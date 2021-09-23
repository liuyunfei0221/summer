package com.blue.database.ioc;

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

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 事务配置
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

        RuleBasedTransactionAttribute attributeWithoutTrans = new RuleBasedTransactionAttribute();
        attributeWithoutTrans.setIsolationLevel(transConf.getIsolation().value());
        attributeWithoutTrans.setPropagationBehavior(transConf.getPropagation().value());
        attributeWithoutTrans.setTimeout(transConf.getTransTimeout());
        attributeWithoutTrans.setReadOnly(true);
        ofNullable(transConf.getMethodPreWithoutTrans())
                .orElse(emptyList())
                .stream()
                .filter(StringUtils::isNotBlank)
                .distinct()
                .map(m -> m + "*")
                .forEach(m -> source.addTransactionalMethod(m, attributeWithoutTrans));


        RuleBasedTransactionAttribute attributeWithTrans = new RuleBasedTransactionAttribute();
        attributeWithTrans.setIsolationLevel(transConf.getIsolation().value());
        attributeWithTrans.setPropagationBehavior(transConf.getPropagation().value());
        attributeWithTrans.setTimeout(transConf.getTransTimeout());
        attributeWithTrans.setRollbackRules(List.of(new RollbackRuleAttribute(Throwable.class)));
        attributeWithTrans.setReadOnly(false);
        ofNullable(transConf.getMethodPreWithTrans())
                .orElse(emptyList())
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
     * 校验
     *
     * @param transConf
     */
    private static void assertConf(TransConf transConf) {
        Isolation isolation = transConf.getIsolation();
        if (isolation == null)
            throw new RuntimeException("isolation can't be null");

        Propagation propagation = transConf.getPropagation();
        if (propagation == null)
            throw new RuntimeException("propagation can't be null");

        Integer transTimeout = transConf.getTransTimeout();
        if (transTimeout == null || transTimeout < 1)
            throw new RuntimeException("transTimeout can't be null or less than 1");

        List<String> methodPreWithTrans = transConf.getMethodPreWithTrans();
        if (isEmpty(methodPreWithTrans))
            throw new RuntimeException("corePoolSize can't be null or less than 1");

        String pointCutExpression = transConf.getPointCutExpression();
        if (isBlank(pointCutExpression))
            throw new RuntimeException("pointCutExpression can't be null or ''");
    }

}
