package com.blue.database.api.conf;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

/**
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface TransConf {

    Isolation getIsolation();

    Propagation getPropagation();

    Integer getTransTimeout();

    List<String> getMethodPreWithoutTrans();

    List<String> getMethodPreWithTrans();

    String getPointCutExpression();

}
