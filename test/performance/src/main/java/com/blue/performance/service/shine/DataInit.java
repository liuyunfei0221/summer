package com.blue.performance.service.shine;

import com.blue.basic.common.base.CommonFunctions;
import com.blue.basic.component.rest.api.conf.RestConfParams;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.HttpProtocol;

import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.blue.basic.component.rest.api.generator.BlueRestGenerator.generateWebClient;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("DuplicatedCode")
public class DataInit {

    private static final List<String> KEY_PREFIXES = Stream.of("大地-", "光辉岁月-", "海阔天空-", "灰色轨迹-", "冷雨夜-", "情人-", "谁伴我闯荡-", "逝去日子-",
            "Unlimited-", "Princess-", "Bird Set Free-", "Green Light-", "Farmer-", "Fly Away-", "Unstoppable-", "The Great Wall-",
            "我的未来不是梦-", "青花瓷-", "七里香-", "千里之外-", "东风破-", "菊花台-", "晴天-", "星晴-", "那些花儿-", "生如夏花-", "平凡之路-", "绿光-", "世界第一等-", "一起走过的日子-", "北京北京-", "突然的自我-").collect(toList());

    private static final int KEY_MAX_SIZE = KEY_PREFIXES.size();
    private static final Random RANDOM = new Random();

    private static final long CITY_ID = 4816L;
    private static final int PRIORITY = 0;
    private static final int INIT_SIZE = 1000;

    private static final Supplier<String> KEY_PRE_SUP = () ->
            KEY_PREFIXES.get(RANDOM.nextInt(KEY_MAX_SIZE));

    private static final RestConfParams CONF = new RestConfParams(512, 64, false,
            7000, false, Stream.of(HttpProtocol.values()).collect(toList()),
            3000, 3000, 3000, 1048576, emptyMap());

    private static final WebClient WEB_CLIENT = generateWebClient(CONF);

    private static final Gson GSON = CommonFunctions.GSON;

    private static final String URL = "http://127.0.0.1:11000/blue-shine/manager/shine";
    private static final String JWT_AUTH = "eyJraWQiOiJlNDhlOTdkOWQ2M2I2M2I2N2JiY2IwYTM1NDcxY2I0ZDMzZTc5MTRmZTMwN2JmNjE3NDc5MjM0N2MxZjhhMTI0OGIwYTBlNmEwYzYxOWM0M2YzMzE1MzA5MmU3YzUwZmUzMTFmZDY0YzkwZTVmMjRlM2Y5NzdiMmI4ZWQyYmU1ZCIsImN0eSI6ImFwcGxpY2F0aW9uL2p3dCIsInR5cCI6IkpXVCIsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJIZWxsbyIsImciOiJNIiwiaCI6IkJfU046NjgxMTkyNDY0NjY0NDk0MDlfQ0xJX00iLCJpc3MiOiJCbHVlIiwiaSI6IjY4MTE5MjQ2NDY2NDQ5NDA5IiwibiI6IlBWQVIiLCJhdWQiOiJCbHVlciIsInMiOiIxNjgyMjMyNzU5IiwibmJmIjoxNjgyMjMyNzU5LCJ0IjoiaUFXc290V1hUOEwiLCJlYXMiOjE2OTAwMDg3NTk2NTcsImV4cCI6MTY5MDAwODc1OSwiaWF0IjoxNjgyMjMyNzU5LCJqdGkiOiJ2eFd3VE5UV2taSXJaS1FuSmhZVE01MUloSVZwdEdXaiJ9.3TEMh5vuhIunn1CMfZbfIw_nuR-7SnvMQlW5s35VMdT3o0I6Fn7qwefCFmPEY-WgzkHPABsRA2pgmZo0BiGe6w";

    private static final WebClient.RequestBodySpec REQUEST_BODY_SPEC = WEB_CLIENT.post()
            .uri(URI.create(URL))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, JWT_AUTH);

    public static void main(String[] args) {
        long stamp = currentTimeMillis();

        String res = REQUEST_BODY_SPEC.bodyValue(GSON.toJson(new ShineInsertParam(KEY_PRE_SUP.get() + stamp, KEY_PRE_SUP.get() + stamp, KEY_PRE_SUP.get() + stamp, KEY_PRE_SUP.get() + stamp,
                KEY_PRE_SUP.get() + stamp, CITY_ID, KEY_PRE_SUP.get() + stamp, KEY_PRE_SUP.get() + stamp, PRIORITY))).retrieve().bodyToMono(String.class).toFuture().join();
        System.err.println("warm res: " + res);

        for (int i = 0; i < INIT_SIZE; i++) {
            stamp = currentTimeMillis();
            REQUEST_BODY_SPEC.bodyValue(GSON.toJson(new ShineInsertParam(KEY_PRE_SUP.get() + stamp, KEY_PRE_SUP.get() + stamp, KEY_PRE_SUP.get() + stamp, KEY_PRE_SUP.get() + stamp,
                            KEY_PRE_SUP.get() + stamp, CITY_ID, KEY_PRE_SUP.get() + stamp, KEY_PRE_SUP.get() + stamp, PRIORITY))).retrieve()
                    .bodyToMono(String.class)
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe(System.out::println, System.err::println);
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
