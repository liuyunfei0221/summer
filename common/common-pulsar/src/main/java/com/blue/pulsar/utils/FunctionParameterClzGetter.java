package com.blue.pulsar.utils;

import java.lang.reflect.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * generic type getter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode", "unused"})
public final class FunctionParameterClzGetter {

    private static final String OVERRIDE_NAME = "override";
    private static final String GET_CONSTANT_POOL_NAME = "getConstantPool";
    private static final String GET_SIZE_NAME = "getSize";
    private static final String GET_METHOD_AT_NAME = "getMethodAt";

    private static Class<?> getErased(Type type) {
        if (type instanceof ParameterizedType)
            return getErased(((ParameterizedType) type).getRawType());
        if (type instanceof GenericArrayType)
            return Array.newInstance(getErased(((GenericArrayType) type).getGenericComponentType()), 0).getClass();
        if (type instanceof TypeVariable<?>) {
            Type[] bounds = ((TypeVariable<?>) type).getBounds();
            return bounds.length > 0 ? getErased(bounds[0]) : Object.class;
        }
        if (type instanceof WildcardType) {
            Type[] bounds = ((WildcardType) type).getUpperBounds();
            return bounds.length > 0 ? getErased(bounds[0]) : Object.class;
        }
        if (type instanceof Class<?>)
            return (Class<?>) type;
        return Object.class;
    }

    private static Method getMethod(Class<?> objClass, String methodName) {
        for (Method method : objClass.getDeclaredMethods())
            if (methodName.equals(method.getName()))
                return method;
        throw new RuntimeException("getMethod(Class<?> objClass, String methodName), Method not found");
    }

    private static Object invoke(Object obj, String methodName, Object... args) {
        try {
            Field overrideField = AccessibleObject.class.getDeclaredField(OVERRIDE_NAME);
            overrideField.setAccessible(true);
            Method targetMethod = getMethod(obj.getClass(), methodName);
            overrideField.set(targetMethod, true);
            return targetMethod.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException("invoke(Object obj, String methodName, Object... args), e = {}", e);
        }
    }

    private static Class<?> getConsumerLambdaParameterType(Consumer<?> consumer) {
        Class<?> consumerClass = consumer.getClass();
        Object constantPool = invoke(consumerClass, GET_CONSTANT_POOL_NAME);

        Member member;
        for (int i = (int) invoke(constantPool, GET_SIZE_NAME) - 1; i >= 0; --i)
            try {
                member = (Member) invoke(constantPool, GET_METHOD_AT_NAME, i);
                if (member instanceof Method && member.getDeclaringClass() != Object.class)
                    return ((Method) member).getParameterTypes()[0];
            } catch (Exception ignored) {
            }
        throw new RuntimeException("getConsumerLambdaParameterType(Consumer<?> consumer) failed, Class<?> not found");
    }

    private static Class<?> getSupplierLambdaParameterType(Supplier<?> supplier) {
        Class<?> supplierClass = supplier.getClass();
        Object constantPool = invoke(supplierClass, GET_CONSTANT_POOL_NAME);

        Member member;
        for (int i = (int) invoke(constantPool, GET_SIZE_NAME) - 1; i >= 0; --i)
            try {
                member = (Member) invoke(constantPool, GET_METHOD_AT_NAME, i);
                if (member instanceof Method && member.getDeclaringClass() != Object.class)
                    return ((Method) member).getParameterTypes()[0];
            } catch (Exception ignored) {
            }
        throw new RuntimeException("getConsumerLambdaParameterType(Consumer<?> consumer) failed, Class<?> not found");
    }

    public static Class<?> getConsumerParameterType(Consumer<?> consumer) {
        for (Type type : consumer.getClass().getGenericInterfaces()) {
            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Consumer.class)
                return getErased(((ParameterizedType) type).getActualTypeArguments()[0]);
        }
        if (consumer.getClass().isSynthetic())
            return getConsumerLambdaParameterType(consumer);
        throw new RuntimeException("getConsumerParameterType(Consumer<?> consumer) failed, Class<?> not found");
    }

    public static Class<?> getSupplierParameterType(Supplier<?> supplier) {
        for (Type type : supplier.getClass().getGenericInterfaces()) {
            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Consumer.class)
                return getErased(((ParameterizedType) type).getActualTypeArguments()[0]);
        }
        if (supplier.getClass().isSynthetic())
            return getSupplierLambdaParameterType(supplier);
        throw new RuntimeException("getConsumerParameterType(Consumer<?> consumer) failed, Class<?> not found");
    }

}
