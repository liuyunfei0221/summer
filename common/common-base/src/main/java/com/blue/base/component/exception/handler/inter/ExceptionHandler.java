package com.blue.base.component.exception.handler.inter;

import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;

/**
 * 异常处理接口 请使用模板方法用来扩展
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "UnnecessaryInterfaceModifier"})
public interface ExceptionHandler {

    /**
     * 获取处理异常类的类名
     *
     * @return
     */
    public String exceptionName();

    /**
     * 处理异常
     *
     * @param throwable
     * @return
     */
    public ExceptionHandleInfo handle(Throwable throwable);

}
