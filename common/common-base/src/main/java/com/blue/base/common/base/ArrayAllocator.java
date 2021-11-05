package com.blue.base.common.base;

import com.blue.base.model.exps.BlueException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static java.lang.Long.valueOf;
import static java.lang.Math.round;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.groupingBy;

/**
 * list allocator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "WeakerAccess", "unused", "AliControlFlowStatementWithoutBraces"})
public final class ArrayAllocator {

    /**
     * threshold for enable parallel
     */
    private static final int THRESHOLD = 1 << 8;

    private static <T> void checkArgs(List<T> list, int par) {
        if (list == null || list.size() < 1 || par < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);
    }

    /**
     * According to the maximum number of elements in collections set after segmentation
     *
     * @param list list to be divided
     * @param max  The maximum number of elements in each collection after segmentation
     * @param fair enable fair distribution (fair distribution will have additional performance consumption)
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
     * According to the maximum number of collections after segmentation
     *
     * @param list list to be divided
     * @param rows The maximum number of elements in each collection after segmentation
     * @param fair enable fair distribution (fair distribution will have additional performance consumption)
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
            if (size > THRESHOLD) {
                //greater than threshold
                final AtomicInteger pedometer = new AtomicInteger(0);
                final int r = rows;
                return new ArrayList<>(list.parallelStream().collect(groupingBy(e -> pedometer.getAndIncrement() % r)).values());
            } else {
                //less than threshold
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
