package com.blue.base.common.base;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 对象大小计算器
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class ObjectSizeCalculator {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算大小
     *
     * @param clz
     * @return
     */
    public static long size(Class<?> clz) {
        Field[] fields = clz.getDeclaredFields();
        long len = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            len = len + UNSAFE.objectFieldOffset(field);
        }

        return len;
    }


}
