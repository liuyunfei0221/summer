import com.blue.identity.core.BlueIdentityGenerator;
import com.blue.identity.core.param.IdGenParam;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static com.blue.identity.constant.SnowflakeBits.*;

/**
 * @author DarkBlue
 */
@SuppressWarnings("ALL")
public class IdentityPerfomanceTest {

    public static void main(String[] args) {

        System.err.println(((1L << TIME_STAMP.len) - Instant.now().getEpochSecond()) / (60L * 60L * 24L * 365L));
        System.err.println(1L << DATA_CENTER.len);
        System.err.println(1L << WORKER.len);
        System.err.println(1L << SEQUENCE.len);

        ExecutorService executorService = new ThreadPoolExecutor(4, 64,
                64, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),
                Thread::new, (r, executor) -> {
        });

//        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
//                new ScheduledThreadPoolExecutor(1, Thread::new, (r, executor) -> System.err.println("scheduled padding thread rejected"));

        IdGenParam idGenParam = new IdGenParam(0, 0,
                Instant.now().getEpochSecond(), 1618790400L, s -> System.err.println(s), null, 3, 50,
                executorService, false, null, 3L, 3L);

        BlueIdentityGenerator blueIdentityGenerator = new BlueIdentityGenerator(idGenParam);

        int times = 20;
        List<Long> durations = new LinkedList<>();
        for (int i = 0; i < times; i++) {
//            durations.add(test(idGenParam, executorService));
            durations.add(testPerformance(idGenParam, executorService));
        }
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println("avg = " + (durations.stream().reduce(Long::sum).orElse(0L)) / times);
        System.err.println("max = " + durations.stream().max(Long::compareTo).orElse(0L));
        System.err.println("min = " + durations.stream().min(Long::compareTo).orElse(0L));

    }

    private static long test(IdGenParam idGenParam, ExecutorService executorService) {
        BlueIdentityGenerator blueIdentityGenerator = new BlueIdentityGenerator(idGenParam);

//        int count = 10000000;
        int count = 1000000;
        Set<Long> set = new HashSet<>();

        CountDownLatch countDownLatch = new CountDownLatch(count);

        long s = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            executorService.submit(() -> {
                long id = blueIdentityGenerator.generate();
                synchronized (blueIdentityGenerator) {
                    boolean add = set.add(id);
                    if (!add) {
                        System.err.println("duplicate id = " + id);
                        System.err.println(blueIdentityGenerator.parse(id));
                    }
//                    System.err.println("id = " + id);
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long d = System.currentTimeMillis() - s;
        System.err.println();
        System.err.println("cost = " + d);
        System.err.println(set.size());
//        System.err.println(identityGenerator.parse(set.stream().findFirst().get()));

        return d;
    }

    private static long testPerformance(IdGenParam idGenParam, ExecutorService executorService) {
        BlueIdentityGenerator blueIdentityGenerator = new BlueIdentityGenerator(idGenParam);
//        SnowflakeIdentityGenerator identityGenerator = new SnowflakeIdentityGenerator(new SnowIdGenParam(idGenParam.getDataCenter(), idGenParam.getWorker(), idGenParam.getLastSeconds(),
//                idGenParam.getBootSeconds(), idGenParam.getMaximumTimeAlarm(), idGenParam.getSecondsRecorder(), executorService));

//        int count = 10000000;
        int count = 1000000;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        long s = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            executorService.submit(() -> {
                blueIdentityGenerator.generate();
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long d = System.currentTimeMillis() - s;
        System.err.println();
        System.err.println("cost = " + d);

        return d;
    }

}
