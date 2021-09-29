package com.blue.database.api.conf;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

/**
 * transaction conf base on expression
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc"})
public interface TransConf {

    /**
     * isolation
     *
     * @return
     */
    Isolation getIsolation();

    /**
     * propagation
     *
     * @return
     */
    Propagation getPropagation();

    /**
     * transaction timeout
     *
     * @return
     */
    Integer getTransTimeout();

    /**
     * methods by prefix execution in transaction
     *
     * @return
     */
    List<String> getMethodPreWithTrans();

    /**
     * point cut expression
     *
     * @return
     */
    String getPointCutExpression();

}
