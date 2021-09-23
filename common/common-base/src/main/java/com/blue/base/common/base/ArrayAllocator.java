package com.blue.base.common.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Long.valueOf;
import static java.lang.Math.round;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.groupingBy;

/**
 * 资源分配
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "WeakerAccess", "unused", "AliControlFlowStatementWithoutBraces"})
public final class ArrayAllocator {

    /**
     * 是否启用多线程处理的元素阈值
     */
    private static final int THRESHOLD = 1 << 8;

    private static <T> void checkArgs(List<T> list, int par) {
        if (list == null || list.size() < 1 || par < 1)
            throw new IllegalArgumentException("list can't be null or empty, par can't be null or less than 1");
    }

    /**
     * 按切分后每个集合的最大元素数量分配
     *
     * @param list 要切分的集合
     * @param max  切分后每集合的最大元素数量
     * @param fair 是否公平分配(公平分配会有额外性能消耗)
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> allotByMax(List<T> list, int max, boolean fair) {
        checkArgs(list, max);

        int size = list.size();
        if (size < max)
            return singletonList(list);

        return allotByRows(list, (size + max - 1) / max, fair);
    }

    /**
     * 按切分后的最大集合数量分配
     *
     * @param list 要切分的集合
     * @param rows 切分数量
     * @param fair 是否公平分配(公平分配会有额外性能消耗)
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> allotByRows(List<T> list, int rows, boolean fair) {
        checkArgs(list, rows);

        int size = list.size();

        if (1 == size)
            return singletonList(list);
        if (size < rows)
            rows = size;

        if (fair) {
            //公平拆分
            if (size > THRESHOLD) {
                //元素数量高于阈值
                final AtomicInteger pedometer = new AtomicInteger(0);
                final int r = rows;
                return new ArrayList<>(list.parallelStream().collect(groupingBy(e -> pedometer.getAndIncrement() % r)).values());
            } else {
                //元素数量低于阈值
                List<List<T>> result = new ArrayList<>(rows);
                for (int i = 0; i < rows; i++)
                    result.add(new LinkedList<>());

                int idx = 0;
                for (T t : list) {
                    if (idx == rows)
                        idx = 0;
                    result.get(idx++).add(t);
                }
                return result;
            }
        } else {
            //非公平拆分
            List<List<T>> result = new ArrayList<>(rows);

            int seq = 1;
            int start = 0;
            int step = valueOf(round((double) size / (double) rows)).intValue();

            while (seq <= rows)
                result.add(list.subList(start, seq++ < rows ? start = (start + step) : size));
            return result;
        }
    }

}
