package com.blue.basic.common.base;

import com.blue.basic.model.exps.BlueException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static java.lang.Long.valueOf;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.groupingBy;

/**
 * list allocator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "WeakerAccess", "unused", "AliControlFlowStatementWithoutBraces"})
public final class ArrayAllocator {

    /**
     * threshold for enable parallel
     */
    private static final int THRESHOLD = 1 << 8;

    private static <T> void checkArgs(List<T> list, int par) {
        if (isEmpty(list) || par < 1)
            throw new BlueException(BAD_REQUEST);
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
        if (size <= max)
            return singletonList(list);

        return allotByRows(list, (size + max - 1) / max, fair);
    }

    /**
     * According to the maximum number of collections after segmentation
     *
     * @param list     list to be divided
     * @param segments The maximum number of elements in each collection after segmentation
     * @param fair     enable fair distribution (fair distribution will have additional performance consumption)
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> allotByRows(List<T> list, int segments, boolean fair) {
        checkArgs(list, segments);

        int size = list.size();

        if (size == 1 || segments == 1)
            return singletonList(list);

        final int seg = min(size, segments);

        if (fair) {
            if (size > THRESHOLD) {
                //greater than threshold
                final AtomicInteger pedometer = new AtomicInteger(0);
                return new ArrayList<>(list.parallelStream().collect(groupingBy(e -> pedometer.getAndIncrement() % seg)).values());
            } else {
                //less than threshold
                List<List<T>> result = new ArrayList<>(seg);
                for (int i = 0; i < seg; i++)
                    result.add(new LinkedList<>());

                int idx = 0;
                for (T t : list) {
                    if (idx == seg)
                        idx = 0;
                    result.get(idx++).add(t);
                }

                return result;
            }
        } else {
            List<List<T>> result = new ArrayList<>(seg);

            int seq = 1;
            int start = 0;
            int step = valueOf(round((double) size / (double) seg)).intValue();

            while (seq <= seg)
                result.add(list.subList(start, seq++ < seg ? start = (start + step) : size));

            return result;
        }
    }

}