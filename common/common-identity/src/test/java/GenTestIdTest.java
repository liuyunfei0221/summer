import com.blue.identity.core.BlueIdentityGenerator;
import com.blue.identity.core.param.IdGenParam;

import java.time.Instant;
import java.util.concurrent.*;

import static com.blue.identity.constant.SnowflakeBits.*;

/**
 * @author liuyunfei
 */
public class GenTestIdTest {

    public static void main(String[] args) {

        System.err.println(((1L << TIME_STAMP.len) - Instant.now().getEpochSecond()) / (60L * 60L * 24L * 365L));
        System.err.println(1L << DATA_CENTER.len);
        System.err.println(1L << WORKER.len);
        System.err.println(1L << SEQUENCE.len);

        ExecutorService executorService = new ThreadPoolExecutor(4, 64,
                64, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),
                Thread::new, (r, executor) -> {
        });

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(1, Thread::new, (r, executor) -> System.err.println("scheduled padding thread rejected"));

        IdGenParam idGenParam = new IdGenParam(1, 1,
                Instant.now().getEpochSecond(), 1618790400L, System.err::println, null, 3, 50,
                executorService, false, scheduledThreadPoolExecutor, 3L, 3L);

        BlueIdentityGenerator blueIdentityGenerator = new BlueIdentityGenerator(idGenParam);

        int times = 10;

        long id;
        for (int i = 0; i < times; i++) {
            id = blueIdentityGenerator.generate();
            System.err.println(id);
        }

    }

}
