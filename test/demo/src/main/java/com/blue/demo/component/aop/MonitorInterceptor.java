package com.blue.demo.component.aop;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MonitorInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();

        System.err.println("name:" + methodName);
        System.err.println("begin stamp:" + System.currentTimeMillis());

        Object result = methodProxy.invokeSuper(o, objects);

        System.err.println("end stamp:" + System.currentTimeMillis());

        return result;
    }

}
