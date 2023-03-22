//package com.blue.demo.component.aop;
//
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.SourceLocation;
//import org.springframework.cglib.proxy.Enhancer;
//
//@Aspect
//public class MonitorProcessor {
//
//    private static final String K = "K";
//
//    private static final String V = "V";
//
//    @Pointcut(value = "@annotation(anno.PerformanceMonitor)")
//    private void aroundMethod() {
//    }
//
//    @Around(value = "aroundMethod()")
//    public Object executeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        MonitorInterceptor monitorInterceptor = new MonitorInterceptor();
//
//
//        SourceLocation sourceLocation = joinPoint.getSourceLocation();
//
//
//        Enhancer enhancer = new Enhancer();
//
//
//
////        enhancer.setSuperclass(UserDao.class);
////
////        enhancer.setCallback(monitorInterceptor);
////        // create方法正式创建代理类
////        UserDao userDao = (UserDao) enhancer.create();
////        // 调用代理类的具体业务方法
////        userDao.findAllUsers();
////        userDao.findUsernameById(1);
////
////
////
////
//
//
//
//        Signature signature = joinPoint.getSignature();
//        String name = signature.getName();
//
//        System.err.println("name:" + name);
//        System.err.println("begin stamp:" + System.currentTimeMillis());
//
//        Object proceed = joinPoint.proceed();
//
//        System.err.println("end stamp:" + System.currentTimeMillis());
//
//        return proceed;
//    }
//
//}
