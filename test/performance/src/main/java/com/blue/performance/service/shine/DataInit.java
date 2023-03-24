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

public class DataInit {

    private static final List<String> KEY_PREFIXES = Stream.of("Unlimited-", "Princess-", "Bird Set Free-", "Green Light-", "Farmer-", "Fly Away-", "Unstoppable-", "The Great Wall-").collect(toList());
    private static final int KEY_MAX_SIZE = KEY_PREFIXES.size();
    private static final Random RANDOM = new Random();

    private static final long CITY_ID = 4816L;
    private static final int PRIORITY = 0;
    private static final int INIT_SIZE = 1000;

    private static final Supplier<String> KEY_PRE_SUP = () ->
            KEY_PREFIXES.get(RANDOM.nextInt(KEY_MAX_SIZE));

    private static final RestConfParams CONF = new RestConfParams(512, 64, false,
            7000, false, Stream.of(HttpProtocol.values()).collect(toList()),
            3000, 3000, 3000,
            1048576, emptyMap());

    private static final WebClient WEB_CLIENT = generateWebClient(CONF);

    private static final Gson GSON = CommonFunctions.GSON;

    private static final String URL = "http://127.0.0.1:11000/blue-shine/manager/shine";
    private static final String JWT_AUTH = "eyJraWQiOiI3NDk1OTYzYjRiZWNlODdiODdmZjVjZWYzMzczZWVlN2JiMDg4NDgwMGQ0YzY5MWY3NWRkNzA1ZGM0N2E1NDE4MTM5NmVjOGRhMmJhYjI1ZGZhOGRiNGE3ZDY0NGYzNmQ3NTZmOWVhMGIyZmE2ODgzN2Y3YTQxNDMzYTYxYTc1ZCIsImN0eSI6ImFwcGxpY2F0aW9uL2p3dCIsInR5cCI6IkpXVCIsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJIZWxsbyIsImciOiJNIiwiaCI6IkJfU046NjUxMzk5NzQ3NTU4NDQwOTdfQ0xJX00iLCJpc3MiOiJCbHVlIiwiaSI6IjY1MTM5OTc0NzU1ODQ0MDk3IiwibiI6IlBWQVIiLCJhdWQiOiJCbHVlciIsInMiOiIxNjc5NDU4MTg4IiwibmJmIjoxNjc5NDU4MTg4LCJ0IjoiOGRiaWlHZUlqcWwiLCJlYXMiOjE2ODcyMzQxODg0MTgsImV4cCI6MTY4NzIzNDE4OCwiaWF0IjoxNjc5NDU4MTg4LCJqdGkiOiJvQmRpdGNZNTN1cENnRlhPWlhIWkdQOTIzRVFCbkt0cSJ9.dr-4o4nJP85VycisBaDPG24s0AxSUUWZXyHDy3KT6FHla7o2mge3okvQnqP1BC54FA5C7KLhUATOOzkJNfjSlA";

    private static final WebClient.RequestBodySpec REQUEST_BODY_SPEC = WEB_CLIENT.post()
            .uri(URI.create(URL))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, JWT_AUTH);

    public static void main(String[] args) {
        long stamp;
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
