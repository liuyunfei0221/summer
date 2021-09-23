package com.blue.base.common.multitask;

import reactor.util.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

import static io.netty.util.internal.ThreadLocalRandom.current;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static reactor.util.Loggers.getLogger;

/**
 * 任务批处理的聚合器
 *
 * @param <R> 用于聚合的通用响应结果
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class DefaultBlueCollector<R> implements BlueCollector<R> {

    private final Logger LOGGER = getLogger(DefaultBlueCollector.class);

    /**
     * 写超时时间
     */
    private final int WRITE_TIME_OUT;

    /**
     * 读超时时间
     */
    private final long READ_TIME_OUT;

    /**
     * 超时单位
     */
    private final TimeUnit TIME_OUT_UNIT;

    /**
     * 分片容器数组
     */
    private final Segment<R>[] segments;


    private final int MAX_IDX;

    /**
     * 分片上限
     */
    private static final int MAXIMUM_CAPACITY = 1 << 16;

    /**
     * 对应的分片容量
     *
     * @param cap
     * @return
     */
    private int getCap(int cap) {
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * 强制有参
     *
     * @param threads     线程数量
     * @param writeTimeout 写超时
     * @param readTimeOut  读超时
     * @param timeUnit     超时单位
     */
    DefaultBlueCollector(int threads, int writeTimeout, long readTimeOut, TimeUnit timeUnit) {
        this.WRITE_TIME_OUT = writeTimeout;
        this.READ_TIME_OUT = readTimeOut;
        this.TIME_OUT_UNIT = timeUnit;

        int cap = getCap(threads);

        @SuppressWarnings({"unchecked"})
        Segment<R>[] ss = new Segment[cap];

        for (int i = 0; i < cap; i++)
            ss[i] = new Segment<>();

        segments = ss;
        MAX_IDX = cap - 1;
    }


    /**
     * 获取聚合结果
     *
     * @return 聚合结果
     */
    @Override
    public List<R> collect() {
        return stream(segments)
                .map(Segment::segmentCollect)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * 任务消费回调
     *
     * @param r 资源处理结果
     */
    @Override
    public void complete(R r) {
        int ran = current().nextInt();
        this.segments[ran & MAX_IDX].segmentAdd(r);
    }


    /**
     * 分片容器
     *
     * @param <K>
     */
    private final class Segment<K> {

        /**
         * 分片聚合结果
         */
        private final List<K> SEGMENT_COLLECT = new LinkedList<>();

        /**
         * 分片同步控制器
         */
        private final StampedLock SEGMENT_LOCK = new StampedLock();

        Segment() {
        }

        /**
         * 任务消费回调
         *
         * @param k 处理结果
         */
        void segmentAdd(K k) {
            long stamp = 0L;
            try {
                stamp = SEGMENT_LOCK.tryWriteLock(WRITE_TIME_OUT, TIME_OUT_UNIT);
                this.SEGMENT_COLLECT.add(k);
            } catch (InterruptedException e) {
                stamp = SEGMENT_LOCK.writeLock();
                this.SEGMENT_COLLECT.add(k);
                LOGGER.error("segmentAdd(K k) failed, e = {}", e.toString());
            } finally {
                if (0L != stamp) {
                    SEGMENT_LOCK.unlockWrite(stamp);
                }
            }
        }

        /**
         * 获取分片聚合结果
         *
         * @return 分片聚合结果
         */
        @SuppressWarnings("ConstantConditions")
        List<K> segmentCollect() {
            long optimisticReadStamp = SEGMENT_LOCK.tryOptimisticRead();
            List<K> collect = this.SEGMENT_COLLECT;
            if (!SEGMENT_LOCK.validate(optimisticReadStamp)) {
                long stamp = 0L;
                try {
                    SEGMENT_LOCK.tryReadLock(READ_TIME_OUT, TIME_OUT_UNIT);
                    collect = this.SEGMENT_COLLECT;
                } catch (InterruptedException e) {
                    stamp = SEGMENT_LOCK.readLock();
                    collect = this.SEGMENT_COLLECT;
                } catch (Exception e) {
                    LOGGER.error("segmentCollect() failed, e = {}", e.toString());
                } finally {
                    if (0L != stamp) {
                        SEGMENT_LOCK.unlockRead(stamp);
                    }
                }
            }
            return collect;
        }
    }

}
