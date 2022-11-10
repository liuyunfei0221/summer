package com.blue.performance.service.shine;

import com.blue.basic.common.base.CommonFunctions;
import com.blue.basic.component.rest.api.conf.RestConfParams;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

import static com.blue.basic.component.rest.api.generator.BlueRestGenerator.generateWebClient;
import static java.lang.System.currentTimeMillis;

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

    private static final int INIT_SIZE = 10000;

    private static final RestConfParams CONF = new RestConfParams();

    private static final WebClient WEB_CLIENT = generateWebClient(CONF);

    private static final Gson GSON = CommonFunctions.GSON;

    private static final String URL = "http://127.0.0.1:11000/blue-shine/manager/shine";
    private static final String JWT_AUTH = "eyJraWQiOiJhNTBhZTc1MTJjMDU5YzBhOTdiODA3N2I0OWJhOTU3MzI4ZTBiN2FiM2QyOGIwODk0YjFlYjkyZjcwYTI3ZjQxOTQ2NWJiZmJhMDU4OTVlMjlhYjNmNWQ2ZDE2ZjkwNjI3YmI5MTYxYjFlOTc0YjQ3ZmRlYmFiMWVjZTAyYjYxNSIsImN0eSI6ImFwcGxpY2F0aW9uL2p3dCIsInR5cCI6IkpXVCIsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJIZWxsbyIsImciOiJNIiwiaCI6IkJfU046NTI5MjU0MzM1MTA4ODc0MjVfQ0xJX00iLCJpc3MiOiJCbHVlIiwiaSI6IjUyOTI1NDMzNTEwODg3NDI1IiwibiI6IlBWQVIiLCJhdWQiOiJCbHVlciIsInMiOiIxNjY4MDgxNDIxIiwibmJmIjoxNjY4MDgxNDIxLCJ0IjoibFMwWUd0U3RlUDUiLCJlYXMiOjE2NzU4NTc0MjExMDUsImV4cCI6MTY3NTg1NzQyMSwiaWF0IjoxNjY4MDgxNDIxLCJqdGkiOiJyQUlzTTNtY25RVFFmSWlRV0pQYzZZQkxXZHRyYlh6UCJ9.C4zbCI4pyAYbBKL7pqailHaruRxw5uBeFPT1-TseR8Yf4IOgKGWAe2eWAA3h1MEl4Zj4hKP5qZ1TpiECEi4q1Q";

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
                    .bodyToMono(String.class).subscribe(System.err::println);
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
