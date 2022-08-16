//package com.blue.shine.demo;
//
//import com.blue.basic.common.base.CommonFunctions;
//import com.blue.basic.component.rest.api.conf.RestConfParams;
//import com.blue.shine.model.ShineInsertParam;
//import com.google.gson.Gson;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//import static com.blue.basic.component.rest.api.generator.BlueRestGenerator.generateWebClient;
//
//public class DataInit {
//
//    private static final String TITLE_PREFIX = "标题-";
//    private static final String CONTENT_PREFIX = "内容-";
//    private static final String DETAIL_PREFIX = "详情-";
//    private static final String CONTACT_PREFIX = "联系人-";
//    private static final String CONTACT_DETAIL_PREFIX = "联系方式详情-";
//    private static final long CITY_ID = 4816L;
//    private static final String ADDRESS_DETAIL_PREFIX = "海滩-";
//    private static final String EXTRA_PREFIX = "附加信息-";
//    private static final int PRIORITY = 0;
//
//    private static final int INIT_SIZE = 3000;
//
//    private static final RestConfParams CONF = new RestConfParams();
//
//    private static final WebClient WEB_CLIENT = generateWebClient(CONF);
//
//    private static final Gson GSON = CommonFunctions.GSON;
//
//    private static final String URL = "http://127.0.0.1:11000/blue-shine/manager/shine";
//    private static final String JWT_AUTH = "eyJraWQiOiIxZGMxOWRkZWUyMWNhZmY1NmQwNmRiYjRiMjM0ZjVlYWJmMzI2NzExNWY2NWIzYTExNWVmYzM0YzdmYmZjM2NhNzhlYmIwMTk0ZjlhMzliYmM5YWY1MWMxNTI1ZjM2ZDc3MGU4YTIxZDI3ZjA1OGE0NjNkNWVjOGRmOTQ1ZDFhOSIsImN0eSI6ImFwcGxpY2F0aW9uL2p3dCIsInR5cCI6IkpXVCIsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJIZWxsbyIsImciOiJNIiwiaCI6IkJfU046NDQ5NDI2OTQ1NTE2NTAzMDVfQ0xJX00iLCJpc3MiOiJCbHVlIiwiaSI6IjQ0OTQyNjk0NTUxNjUwMzA1IiwibiI6IlBWQVIiLCJhdWQiOiJCbHVlciIsInMiOiIxNjYwNjU2OTI1IiwibmJmIjoxNjYwNjU2OTI1LCJ0IjoicDZvaWxob3I0WTYiLCJlYXMiOjE2Njg0MzI5MjU0MDUsImV4cCI6MTY2ODQzMjkyNSwiaWF0IjoxNjYwNjU2OTI1LCJqdGkiOiJjeDlXSGN6Y3RJcU1Fejc1bmdmbTFLcTc3TzlhdkFmZSJ9.x0mHiiRTEY878LvtbBbQPTBRJfYqzPbzVxmcHp26UH-zeIKqXR7gXKawQCeM6ViN3hKUtm7KyVYvSf1s9cRipg";
//
//    private static final WebClient.RequestBodySpec REQUEST_BODY_SPEC = WEB_CLIENT.post()
//            .uri(URI.create(URL))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header(HttpHeaders.AUTHORIZATION, JWT_AUTH);
//
//    public static void main(String[] args) {
//        long stamp;
//        WebClient.ResponseSpec retrieve;
//
//        List<CompletableFuture<String>> resultFutures = new ArrayList<>(3000);
//
//        for (int i = 0; i < INIT_SIZE; i++) {
//            stamp = System.currentTimeMillis();
//
//            retrieve = REQUEST_BODY_SPEC.bodyValue(GSON.toJson(new ShineInsertParam(TITLE_PREFIX + stamp, CONTENT_PREFIX + stamp, DETAIL_PREFIX + stamp, CONTACT_PREFIX + stamp,
//                    CONTACT_DETAIL_PREFIX + stamp, CITY_ID, ADDRESS_DETAIL_PREFIX + stamp, EXTRA_PREFIX + stamp, PRIORITY))).retrieve();
//
//            resultFutures.add(retrieve.bodyToMono(String.class).toFuture());
//
//            try {
//                Thread.sleep(50L);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        String result;
//        for (CompletableFuture<String> future : resultFutures) {
//            result = future.join();
//            System.err.println(result);
//        }
//
//    }
//
//}
