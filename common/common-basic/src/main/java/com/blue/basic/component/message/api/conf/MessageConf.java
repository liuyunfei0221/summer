package com.blue.basic.component.message.api.conf;

/**
 * message conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavadocDeclaration"})
public interface MessageConf {

    /**
     * i18n messages location
     *
     * @return
     */
    String getMessageLocation();

    /**
     * i18n elements location
     *
     * @return
     */
    String getElementLocation();

}
