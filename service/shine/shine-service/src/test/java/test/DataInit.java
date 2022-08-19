package test;

import com.blue.basic.common.base.CommonFunctions;
import com.blue.basic.component.rest.api.conf.RestConfParams;
import com.blue.shine.model.ShineInsertParam;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.blue.basic.component.rest.api.generator.BlueRestGenerator.generateWebClient;

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

    private static final RestConfParams CONF = new RestConfParams();

    private static final WebClient WEB_CLIENT = generateWebClient(CONF);

    private static final Gson GSON = CommonFunctions.GSON;

    private static final String URL = "http://127.0.0.1:11000/blue-shine/manager/shine";
    private static final String JWT_AUTH = "eyJraWQiOiI4OTZjMmI3YmExODJlMTQ1NTA4ZDQwNTJlZDNlNmQ4NWNmMTQ0MWQwNGQ4NzQyMGY0NWVkNGY4MDNlMzdlYmY0YjM4MTgxOTkwNThmOTA0YzUxMzY1MjllZDdlNjIzZjMyNTQwNTQ0ZTUxZTgyNzcyNjkwYjBhYTA3NzcwNGUwMCIsImN0eSI6ImFwcGxpY2F0aW9uL2p3dCIsInR5cCI6IkpXVCIsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJIZWxsbyIsImciOiJNIiwiaCI6IkJfU046NDUxMDMxMzYyNzYyMjg4MzFfQ0xJX00iLCJpc3MiOiJCbHVlIiwiaSI6IjQ1MTAzMTM2Mjc2MjI4ODMxIiwibiI6IlBWQVIiLCJhdWQiOiJCbHVlciIsInMiOiIxNjYwNzk2MDIxIiwibmJmIjoxNjYwNzk2MDIxLCJ0IjoiT3g1MWxKbGdQNlQiLCJlYXMiOjE2Njg1NzIwMjE5MjQsImV4cCI6MTY2ODU3MjAyMSwiaWF0IjoxNjYwNzk2MDIxLCJqdGkiOiJvTklKNVpMRmd1U3JYQ28zN0w2dWhvNnFkZTBoNkNUUSJ9.y5QoMLUDBX1XkEvfjmKg2GjxAGOL1mqLrbnGtvcLLFhiragAIJZW6TWVzWuKbkBN4YcuIHicELU4eSOzpkWb2g";

    private static final WebClient.RequestBodySpec REQUEST_BODY_SPEC = WEB_CLIENT.post()
            .uri(URI.create(URL))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, JWT_AUTH);

    public static void main(String[] args) {
        long stamp;
        WebClient.ResponseSpec retrieve;

        List<CompletableFuture<String>> resultFutures = new ArrayList<>(3000);

        for (int i = 0; i < INIT_SIZE; i++) {
            stamp = System.currentTimeMillis();

            retrieve = REQUEST_BODY_SPEC.bodyValue(GSON.toJson(new ShineInsertParam(TITLE_PREFIX + stamp, CONTENT_PREFIX + stamp, DETAIL_PREFIX + stamp, CONTACT_PREFIX + stamp,
                    CONTACT_DETAIL_PREFIX + stamp, CITY_ID, ADDRESS_DETAIL_PREFIX + stamp, EXTRA_PREFIX + stamp, PRIORITY))).retrieve();

            resultFutures.add(retrieve.bodyToMono(String.class).toFuture());

            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        String result;
        for (CompletableFuture<String> future : resultFutures) {
            result = future.join();
            System.err.println(result);
        }

    }

}
