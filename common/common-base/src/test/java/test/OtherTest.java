package test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class OtherTest {

    public static void main(String[] args) {

        AtomicLong atomicLong = new AtomicLong(Long.MAX_VALUE);

        long l1 = atomicLong.get();
        System.err.println(Long.toBinaryString(l1));

        l1 = atomicLong.incrementAndGet();
        System.err.println(Long.toBinaryString(l1));

        l1 = atomicLong.incrementAndGet();
        System.err.println(Long.toBinaryString(l1));

        l1 = atomicLong.incrementAndGet();
        System.err.println(Long.toBinaryString(l1));

        l1 = atomicLong.incrementAndGet();
        System.err.println(Long.toBinaryString(l1));


        System.err.println();
        System.err.println();
        System.err.println();

        LongAdder longAdder = new LongAdder();
        longAdder.add(Long.MAX_VALUE);
        System.err.println(longAdder.longValue());

        longAdder.increment();
        System.err.println(longAdder.longValue());

        longAdder.increment();
        System.err.println(longAdder.longValue());

        longAdder.add(Long.MAX_VALUE);
        System.err.println(longAdder.longValue());

        longAdder.increment();
        longAdder.increment();
        longAdder.increment();
        System.err.println(longAdder.longValue());

        longAdder.add(Long.MAX_VALUE);
        System.err.println(longAdder.longValue());

        longAdder.increment();
        longAdder.increment();
        longAdder.increment();
        System.err.println(longAdder.longValue());

        longAdder.add(Long.MAX_VALUE);
        System.err.println(longAdder.longValue());

        longAdder.increment();
        longAdder.increment();
        longAdder.increment();
        System.err.println(longAdder.longValue());

        longAdder.add(Long.MAX_VALUE);
        System.err.println(longAdder.longValue());


    }

}
