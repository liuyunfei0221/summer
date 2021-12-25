package com.blue.verify.api.conf;

/**
 * @author liuyunfei
 * @date 2021/12/22
 * @apiNote
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface CaptchaConf {

    Boolean getBorder();

    String getBorderColor();

    Integer getBorderThickness();

    String getProducerImpl();

    String getTextProducerImpl();

    String getTextProducerCharString();

    Integer getTextProducerCharLength();

    String getTextProducerFontNames();

    Integer getTextProducerFontSize();

    String getTextProducerFontColor();

    Integer getTextProducerCharSpace();

    String getNoiseImpl();

    String getNoiseColor();

    String getObscurificatorImpl();

    String getWordImpl();

    String getBackgroundImpl();

    String getBackgroundClearFrom();

    String getBackgroundClearTo();

    Integer getImageWidth();

    Integer getImageHeight();

}
