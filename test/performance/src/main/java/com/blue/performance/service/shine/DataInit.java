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

    //    private static final List<String> KEY_PREFIXES = Stream.of("Unlimited-", "Princess-", "Bird Set Free-", "Green Light-", "Farmer-", "Fly Away-", "Unstoppable-", "The Great Wall-").collect(toList());
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
    private static final String JWT_AUTH = "eyJraWQiOiIxYzFiMGNmYTNhYzQ5MDllYzQ2MTU4ZWY0OWE4YjkyZjdmMTcyMjNmNzlmMjEzNDUxYWVhYzM3NWFkNjZhNDYwZTg0NDdmZjI4ZDAzNmQ5NTViOWRhMWY5ZDNiMDQzMTZhNzJmNmQyMmQ0OWRjMDRkMDdlYzg4MGVlYjQyNWQxMiIsImN0eSI6ImFwcGxpY2F0aW9uL2p3dCIsInR5cCI6IkpXVCIsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJIZWxsbyIsImciOiJNIiwiaCI6IkJfU046NjU3MDkwOTMzNTYwNDQyODlfQ0xJX00iLCJpc3MiOiJCbHVlIiwiaSI6IjY1NzA5MDkzMzU2MDQ0Mjg5IiwibiI6IlBWQVIiLCJhdWQiOiJCbHVlciIsInMiOiIxNjc5OTkxMTY3IiwibmJmIjoxNjc5OTkxMTY3LCJ0Ijoib29RRlFGaVBKQjgiLCJlYXMiOjE2ODc3NjcxNjc3NjYsImV4cCI6MTY4Nzc2NzE2NywiaWF0IjoxNjc5OTkxMTY3LCJqdGkiOiJBNm5oRGFQSWNvY013bEljaDBRTG1xMG95YkFiMklEVSJ9.W2gEwvbl4GIJETI1J_dRjCqK9CDoYFdVhn6fpxWsdOu7w2oN5c4-FxaPaSSlVX4tJV6NTMlxOw_R5pRxkwefWQ";

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
