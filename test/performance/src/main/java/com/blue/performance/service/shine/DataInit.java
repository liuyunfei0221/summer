package com.blue.performance.service.shine;

import com.blue.basic.common.base.CommonFunctions;
import com.blue.basic.component.rest.api.conf.RestConfParams;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.stream.Stream;

import static com.blue.basic.component.rest.api.generator.BlueRestGenerator.generateWebClient;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static reactor.netty.http.HttpProtocol.*;

public class DataInit {

    private static final String TITLE_PREFIX = "标题-";
    private static final String CONTENT_PREFIX = "内容-";
    private static final String DETAIL_PREFIX = "详情-";
    private static final String CONTACT_PREFIX = "联系人-";
    private static final String CONTACT_DETAIL_PREFIX = "联系方式详情-";
    private static final long CITY_ID = 4816L;
    private static final String ADDRESS_DETAIL_PREFIX = "海滩-";
    private static final String EXTRA_PREFIX = "附加信息-";
    private static final int PRIORITY = 0;

    private static final int INIT_SIZE = 1000;

    private static final RestConfParams CONF = new RestConfParams(512, 64, false,
            7000, false, Stream.of(HTTP11, H2, H2C).collect(toList()),
            3000, 3000, 3000,
            1048576, emptyMap());

    private static final WebClient WEB_CLIENT = generateWebClient(CONF);

    private static final Gson GSON = CommonFunctions.GSON;

    private static final String URL = "http://127.0.0.1:11000/blue-shine/manager/shine";
    private static final String JWT_AUTH = "eyJraWQiOiJjYTRiNmY3ZTI0YjY1ZWMwYjRkYWJiMWIxZDEwYmI5ZWMzNWU0MzdmYWQ1M2QyMzE5OWJkNzE1ZDY2ZDVlNDZjMmZlMTFhNjhlNmIxNDA4NDc0OThjOGQ2MmE0Mjg4NWQ4ZjZlMzIxMWZlZjM3MWY4ZDZkNWUwMWExZTM2MzA0YSIsImN0eSI6ImFwcGxpY2F0aW9uL2p3dCIsInR5cCI6IkpXVCIsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJIZWxsbyIsImciOiJNIiwiaCI6IkJfU046NTczNzY4Njg2MTI5NjQzNTNfQ0xJX00iLCJpc3MiOiJCbHVlIiwiaSI6IjU3Mzc2ODY4NjEyOTY0MzUzIiwibiI6IlBQIiwiYXVkIjoiQmx1ZXIiLCJzIjoiMTY3MjM3MzY1MSIsIm5iZiI6MTY3MjM3MzY1MSwidCI6IjRkWlVyMlZCdzFjIiwiZWFzIjoxNjgwMTQ5NjUxNzI1LCJleHAiOjE2ODAxNDk2NTEsImlhdCI6MTY3MjM3MzY1MSwianRpIjoiMVZVNkhVTGh2Mmg1eWtsTW1NbkNSR1FBdHVHSzRZWmoifQ.IDwFP9UUgW3uDWmYM8tq3Skf250ev_FIWYcfTfhhiFIZ9KS5kC_5lExUIHi-UPEauRks1FvXx4N6oi8QSJzEiA";

    private static final WebClient.RequestBodySpec REQUEST_BODY_SPEC = WEB_CLIENT.post()
            .uri(URI.create(URL))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, JWT_AUTH);

    public static void main(String[] args) {
        long stamp;
        for (int i = 0; i < INIT_SIZE; i++) {
            stamp = currentTimeMillis();
            REQUEST_BODY_SPEC.bodyValue(GSON.toJson(new ShineInsertParam(TITLE_PREFIX + stamp, CONTENT_PREFIX + stamp, DETAIL_PREFIX + stamp, CONTACT_PREFIX + stamp,
                            CONTACT_DETAIL_PREFIX + stamp, CITY_ID, ADDRESS_DETAIL_PREFIX + stamp, EXTRA_PREFIX + stamp, PRIORITY))).retrieve()
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
