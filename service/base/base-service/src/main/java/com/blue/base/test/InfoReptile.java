package com.blue.base.test;

import com.blue.base.component.reactrest.api.conf.ReactRestConfParams;
import com.blue.base.component.reactrest.api.generator.BlueReactRestGenerator;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.List;

import static reactor.netty.http.HttpProtocol.*;

/**
 * @author liuyunfei
 * @date 2021/12/14
 * @apiNote
 */
public class InfoReptile {


    private static WebClient webClient;

    static {
        ReactRestConfParams params = new ReactRestConfParams();

        params.setMaxConnections(256);
        params.setWorkerCount(32);
        params.setUseGlobalResources(true);
        params.setConnectTimeoutMillis(10000);
        params.setUseTcpNodelay(false);
        params.setProtocols(List.of(HTTP11, H2, H2C));
        params.setResponseTimeoutMillis(10000);
        params.setWriteTimeoutMillis(10000);
        params.setReadTimeoutMillis(10000);
        params.setMaxByteInMemorySize(1024 * 1024 * 4);

        webClient = BlueReactRestGenerator.createWebClient(params);
    }

    private final static String url = "https://shop.bigticket.ae/index.php?controller=address";
    private final static String body = "id_country=105&UpdateType=fetchStates";

    private static void packageHeader(WebClient.RequestBodySpec spec) {
        spec

                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
    }


    public static void test1() {

        WebClient.RequestBodySpec spec = webClient.post().uri(URI.create(url));
        //spec.bodyValue(body)
        //        .exchangeToFlux(clientResponse -> {
        //            HttpStatus httpStatus = clientResponse.statusCode();
        //
        //
        //        })

    }


}
