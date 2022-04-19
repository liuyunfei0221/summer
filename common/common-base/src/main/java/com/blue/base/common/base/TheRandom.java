package com.blue.base.common.base;


import sun.misc.Unsafe;

import java.io.ObjectStreamField;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.BlueChecker.isNull;

/**
 * safe random generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "StatementWithEmptyBody", "unused"})
public class TheRandom extends Random {

    private static long mix64(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
        z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
        return z ^ (z >>> 33);
    }

    private static int mix32(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
        return (int) (((z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L) >>> 32);
    }

    private static final long GAMMA = 0x9e3779b97f4a7c15L;

    private static final int PROBE_INCREMENT = 0x9e3779b9;

    private static final long SEEDER_INCREMENT = 0xbb67ae8584caa73bL;

    private static final double DOUBLE_UNIT = 0x1.0p-53;
    private static final float FLOAT_UNIT = 0x1.0p-24f;

    static final String BAD_BOUND = "bound must be positive";
    static final String BAD_RANGE = "bound must be greater than origin";
    static final String BAD_SIZE = "size must be non-negative";

    private static final Unsafe U;
    private static final long SEED;
    private static final long PROBE;

    static {
        try {
            Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
            unsafe.setAccessible(true);
            U = (Unsafe) unsafe.get(null);

            Class<?> tk = Thread.class;
            SEED = U.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomSeed"));
            PROBE = U.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomProbe"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @SuppressWarnings("AlibabaThreadLocalShouldRemove")
    private static final ThreadLocal<Double> NEXT_LOCAL_GAUSSIAN =
            new ThreadLocal<>();

    private static final AtomicInteger PROBE_GENERATOR = new AtomicInteger();

    static final TheRandom INSTANCE = new TheRandom();

    private static final Random SEED_RANDOM;

    static {
        try {
            SEED_RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("generate random failed, e = {}", e);
        }
    }

    private static final AtomicLong SEEDER = new AtomicLong(SEED_RANDOM.nextLong());

    boolean initialized;

    private TheRandom() {
        initialized = true;
    }

    static void localInit() {
        int p = PROBE_GENERATOR.addAndGet(PROBE_INCREMENT);
        int probe = (p == 0) ? 1 : p;
        long seed = mix64(SEEDER.getAndAdd(SEEDER_INCREMENT));
        Thread t = Thread.currentThread();
        U.putLong(t, SEED, seed);
        U.putInt(t, PROBE, probe);
    }

    private static TheRandom current() {
        if (U.getInt(Thread.currentThread(), PROBE) == 0)
            localInit();
        SEEDER.set(SEED_RANDOM.nextLong());
        return INSTANCE;
    }

    public static Random get() {
        return current();
    }

    @Override
    public void setSeed(long seed) {
        if (isNotNull(SEEDER))
            SEEDER.set(seed);
    }

    final long nextSeed() {
        Thread t;
        long r;
        U.putLong(t = Thread.currentThread(), SEED,
                r = U.getLong(t, SEED) + GAMMA);
        return r;
    }

    @Override
    protected int next(int bits) {
        return nextInt() >>> (32 - bits);
    }

    final long internalNextLong(long origin, long bound) {
        long r = mix64(nextSeed());
        if (origin < bound) {
            long n = bound - origin, m = n - 1;
            if ((n & m) == 0L)
                r = (r & m) + origin;
            else if (n > 0L) {
                for (long u = r >>> 1;
                     u + m - (r = u % n) < 0L;
                     u = mix64(nextSeed()) >>> 1)
                    ;
                r += origin;
            } else {
                while (r < origin || r >= bound)
                    r = mix64(nextSeed());
            }
        }
        return r;
    }

    final int internalNextInt(int origin, int bound) {
        int r = mix32(nextSeed());
        if (origin < bound) {
            int n = bound - origin, m = n - 1;
            if ((n & m) == 0)
                r = (r & m) + origin;
            else if (n > 0) {
                for (int u = r >>> 1;
                     u + m - (r = u % n) < 0;
                     u = mix32(nextSeed()) >>> 1)
                    ;
                r += origin;
            } else {
                while (r < origin || r >= bound)
                    r = mix32(nextSeed());
            }
        }
        return r;
    }

    final double internalNextDouble(double origin, double bound) {
        double r = (nextLong() >>> 11) * DOUBLE_UNIT;
        if (origin < bound) {
            r = r * (bound - origin) + origin;
            if (r >= bound)
                r = Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
        }
        return r;
    }

    @Override
    public int nextInt() {
        return mix32(nextSeed());
    }

    @Override
    public int nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException(BAD_BOUND);
        int r = mix32(nextSeed());
        int m = bound - 1;
        if ((bound & m) == 0)
            r &= m;
        else {
            for (int u = r >>> 1;
                 u + m - (r = u % bound) < 0;
                 u = mix32(nextSeed()) >>> 1)
                ;
        }
        return r;
    }

    public int nextInt(int origin, int bound) {
        if (origin >= bound)
            throw new IllegalArgumentException(BAD_RANGE);
        return internalNextInt(origin, bound);
    }

    @Override
    public long nextLong() {
        return mix64(nextSeed());
    }

    public long nextLong(long bound) {
        if (bound <= 0)
            throw new IllegalArgumentException(BAD_BOUND);
        long r = mix64(nextSeed());
        long m = bound - 1;
        if ((bound & m) == 0L)
            r &= m;
        else {
            for (long u = r >>> 1;
                 u + m - (r = u % bound) < 0L;
                 u = mix64(nextSeed()) >>> 1)
                ;
        }
        return r;
    }

    public long nextLong(long origin, long bound) {
        if (origin >= bound)
            throw new IllegalArgumentException(BAD_RANGE);
        return internalNextLong(origin, bound);
    }

    @Override
    public double nextDouble() {
        return (mix64(nextSeed()) >>> 11) * DOUBLE_UNIT;
    }

    public double nextDouble(double bound) {
        if (bound <= 0.0)
            throw new IllegalArgumentException(BAD_BOUND);
        double result = (mix64(nextSeed()) >>> 11) * DOUBLE_UNIT * bound;
        return (result < bound) ? result :
                Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
    }

    public double nextDouble(double origin, double bound) {
        if (origin >= bound)
            throw new IllegalArgumentException(BAD_RANGE);
        return internalNextDouble(origin, bound);
    }

    @Override
    public boolean nextBoolean() {
        return mix32(nextSeed()) < 0;
    }

    @Override
    public float nextFloat() {
        return (mix32(nextSeed()) >>> 8) * FLOAT_UNIT;
    }

    @Override
    public double nextGaussian() {
        Double d = NEXT_LOCAL_GAUSSIAN.get();
        if (isNotNull(d)) {
            NEXT_LOCAL_GAUSSIAN.set(null);
            return d;
        }
        double v1, v2, s;
        do {
            v1 = 2 * nextDouble() - 1;
            v2 = 2 * nextDouble() - 1;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        NEXT_LOCAL_GAUSSIAN.set(v2 * multiplier);
        return v1 * multiplier;
    }

    @Override
    public IntStream ints(long streamSize) {
        if (streamSize < 0L)
            throw new IllegalArgumentException(BAD_SIZE);
        return StreamSupport.intStream
                (new RandomIntsSpliterator
                                (0L, streamSize, Integer.MAX_VALUE, 0),
                        false);
    }

    @Override
    public IntStream ints() {
        return StreamSupport.intStream
                (new RandomIntsSpliterator
                                (0L, Long.MAX_VALUE, Integer.MAX_VALUE, 0),
                        false);
    }

    @Override
    public IntStream ints(long streamSize, int randomNumberOrigin,
                          int randomNumberBound) {
        if (streamSize < 0L)
            throw new IllegalArgumentException(BAD_SIZE);
        if (randomNumberOrigin >= randomNumberBound)
            throw new IllegalArgumentException(BAD_RANGE);
        return StreamSupport.intStream
                (new RandomIntsSpliterator
                                (0L, streamSize, randomNumberOrigin, randomNumberBound),
                        false);
    }

    @Override
    public IntStream ints(int randomNumberOrigin, int randomNumberBound) {
        if (randomNumberOrigin >= randomNumberBound)
            throw new IllegalArgumentException(BAD_RANGE);
        return StreamSupport.intStream
                (new RandomIntsSpliterator
                                (0L, Long.MAX_VALUE, randomNumberOrigin, randomNumberBound),
                        false);
    }

    @Override
    public LongStream longs(long streamSize) {
        if (streamSize < 0L)
            throw new IllegalArgumentException(BAD_SIZE);
        return StreamSupport.longStream
                (new RandomLongsSpliterator
                                (0L, streamSize, Long.MAX_VALUE, 0L),
                        false);
    }

    @Override
    public LongStream longs() {
        return StreamSupport.longStream
                (new RandomLongsSpliterator
                                (0L, Long.MAX_VALUE, Long.MAX_VALUE, 0L),
                        false);
    }

    @Override
    public LongStream longs(long streamSize, long randomNumberOrigin,
                            long randomNumberBound) {
        if (streamSize < 0L)
            throw new IllegalArgumentException(BAD_SIZE);
        if (randomNumberOrigin >= randomNumberBound)
            throw new IllegalArgumentException(BAD_RANGE);
        return StreamSupport.longStream
                (new RandomLongsSpliterator
                                (0L, streamSize, randomNumberOrigin, randomNumberBound),
                        false);
    }

    @Override
    public LongStream longs(long randomNumberOrigin, long randomNumberBound) {
        if (randomNumberOrigin >= randomNumberBound)
            throw new IllegalArgumentException(BAD_RANGE);
        return StreamSupport.longStream
                (new RandomLongsSpliterator
                                (0L, Long.MAX_VALUE, randomNumberOrigin, randomNumberBound),
                        false);
    }

    @Override
    public DoubleStream doubles(long streamSize) {
        if (streamSize < 0L)
            throw new IllegalArgumentException(BAD_SIZE);
        return StreamSupport.doubleStream
                (new RandomDoublesSpliterator
                                (0L, streamSize, Double.MAX_VALUE, 0.0),
                        false);
    }

    @Override
    public DoubleStream doubles() {
        return StreamSupport.doubleStream
                (new RandomDoublesSpliterator
                                (0L, Long.MAX_VALUE, Double.MAX_VALUE, 0.0),
                        false);
    }

    @Override
    public DoubleStream doubles(long streamSize, double randomNumberOrigin,
                                double randomNumberBound) {
        if (streamSize < 0L)
            throw new IllegalArgumentException(BAD_SIZE);
        if (!(randomNumberOrigin < randomNumberBound))
            throw new IllegalArgumentException(BAD_RANGE);
        return StreamSupport.doubleStream
                (new RandomDoublesSpliterator
                                (0L, streamSize, randomNumberOrigin, randomNumberBound),
                        false);
    }

    @Override
    public DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
        if (!(randomNumberOrigin < randomNumberBound))
            throw new IllegalArgumentException(BAD_RANGE);
        return StreamSupport.doubleStream
                (new RandomDoublesSpliterator
                                (0L, Long.MAX_VALUE, randomNumberOrigin, randomNumberBound),
                        false);
    }

    @SuppressWarnings({"UnnecessaryLocalVariable", "SpellCheckingInspection"})
    private static final class RandomIntsSpliterator
            implements Spliterator.OfInt {
        long index;
        final long fence;
        final int origin;
        final int bound;

        RandomIntsSpliterator(long index, long fence,
                              int origin, int bound) {
            this.index = index;
            this.fence = fence;
            this.origin = origin;
            this.bound = bound;
        }

        @Override
        public RandomIntsSpliterator trySplit() {
            long i = index, m = (i + fence) >>> 1;
            return (m <= i) ? null :
                    new RandomIntsSpliterator(i, index = m, origin, bound);
        }

        @Override
        public long estimateSize() {
            return fence - index;
        }

        @Override
        public int characteristics() {
            return (Spliterator.SIZED | Spliterator.SUBSIZED |
                    Spliterator.NONNULL | Spliterator.IMMUTABLE);
        }

        @Override
        public boolean tryAdvance(IntConsumer consumer) {
            if (isNull(consumer)) throw new NullPointerException();
            long i = index, f = fence;
            if (i < f) {
                consumer.accept(TheRandom.current().internalNextInt(origin, bound));
                index = i + 1;
                return true;
            }
            return false;
        }

        @Override
        public void forEachRemaining(IntConsumer consumer) {
            if (isNull(consumer)) throw new NullPointerException();
            long i = index, f = fence;
            if (i < f) {
                index = f;
                int o = origin, b = bound;
                TheRandom rng = TheRandom.current();
                do {
                    consumer.accept(rng.internalNextInt(o, b));
                } while (++i < f);
            }
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private static final class RandomLongsSpliterator
            implements Spliterator.OfLong {
        long index;
        final long fence;
        final long origin;
        final long bound;

        RandomLongsSpliterator(long index, long fence,
                               long origin, long bound) {
            this.index = index;
            this.fence = fence;
            this.origin = origin;
            this.bound = bound;
        }

        @Override
        public RandomLongsSpliterator trySplit() {
            long i = index, m = (i + fence) >>> 1;
            return (m <= i) ? null :
                    new RandomLongsSpliterator(i, index = m, origin, bound);
        }

        @Override
        public long estimateSize() {
            return fence - index;
        }

        @Override
        public int characteristics() {
            return (Spliterator.SIZED | Spliterator.SUBSIZED |
                    Spliterator.NONNULL | Spliterator.IMMUTABLE);
        }

        @Override
        public boolean tryAdvance(LongConsumer consumer) {
            if (isNull(consumer)) throw new NullPointerException();
            long i = index, f = fence;
            if (i < f) {
                consumer.accept(TheRandom.current().internalNextLong(origin, bound));
                index = i + 1;
                return true;
            }
            return false;
        }

        @Override
        public void forEachRemaining(LongConsumer consumer) {
            if (isNull(consumer)) throw new NullPointerException();
            long i = index, f = fence;
            if (i < f) {
                index = f;
                long o = origin, b = bound;
                TheRandom rng = TheRandom.current();
                do {
                    consumer.accept(rng.internalNextLong(o, b));
                } while (++i < f);
            }
        }

    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private static final class RandomDoublesSpliterator
            implements Spliterator.OfDouble {
        long index;
        final long fence;
        final double origin;
        final double bound;

        RandomDoublesSpliterator(long index, long fence,
                                 double origin, double bound) {
            this.index = index;
            this.fence = fence;
            this.origin = origin;
            this.bound = bound;
        }

        @Override
        public RandomDoublesSpliterator trySplit() {
            long i = index, m = (i + fence) >>> 1;
            return (m <= i) ? null :
                    new RandomDoublesSpliterator(i, index = m, origin, bound);
        }

        @Override
        public long estimateSize() {
            return fence - index;
        }

        @Override
        public int characteristics() {
            return (Spliterator.SIZED | Spliterator.SUBSIZED |
                    Spliterator.NONNULL | Spliterator.IMMUTABLE);
        }

        @Override
        public boolean tryAdvance(DoubleConsumer consumer) {
            if (isNull(consumer)) throw new NullPointerException();
            long i = index, f = fence;
            if (i < f) {
                consumer.accept(TheRandom.current().internalNextDouble(origin, bound));
                index = i + 1;
                return true;
            }
            return false;
        }

        @Override
        public void forEachRemaining(DoubleConsumer consumer) {
            if (isNull(consumer)) throw new NullPointerException();
            long i = index, f = fence;
            if (i < f) {
                index = f;
                double o = origin, b = bound;
                TheRandom rng = TheRandom.current();
                do {
                    consumer.accept(rng.internalNextDouble(o, b));
                } while (++i < f);
            }
        }
    }

    private static final long serialVersionUID = -5851777807851030925L;

    private static final ObjectStreamField[] SERIAL_PERSISTENT_FIELDS = {
            new ObjectStreamField("rnd", long.class),
            new ObjectStreamField("initialized", boolean.class),
    };

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {

        java.io.ObjectOutputStream.PutField fields = s.putFields();
        fields.put("rnd", U.getLong(Thread.currentThread(), SEED));
        fields.put("initialized", true);
        s.writeFields();
    }

    private Object readResolve() {
        return current();
    }

}
