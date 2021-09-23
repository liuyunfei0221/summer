package com.blue.file.config.universal;

import com.blue.file.config.deploy.RequestAttributeDeploy;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static reactor.core.scheduler.Schedulers.single;


/**
 * web相关组件配置类
 *
 * @author DarkBlue
 */
@Configuration
public class WebConfig implements WebFluxConfigurer {

    private final RequestAttributeDeploy requestAttributeDeploy;

    public WebConfig(RequestAttributeDeploy requestAttributeDeploy) {
        this.requestAttributeDeploy = requestAttributeDeploy;
    }

    @Bean
    HttpMessageConverters httpMessageConverters() {
        return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();

        partReader.setStreaming(false);
        partReader.setHeadersCharset(UTF_8);
        partReader.setBlockingOperationScheduler(single());

        partReader.setMaxHeadersSize(requestAttributeDeploy.getMaxHeadersSize());
        partReader.setMaxInMemorySize(requestAttributeDeploy.getMaxInMemorySize());
        partReader.setMaxDiskUsagePerPart(requestAttributeDeploy.getMaxDiskUsagePerPart());
        partReader.setMaxParts(requestAttributeDeploy.getMaxParts());
        partReader.setEnableLoggingRequestDetails(requestAttributeDeploy.getEnableLoggingRequestDetails());

        MultipartHttpMessageReader multipartReader = new MultipartHttpMessageReader(partReader);
        multipartReader.setEnableLoggingRequestDetails(requestAttributeDeploy.getEnableLoggingRequestDetails());

        configurer.defaultCodecs().multipartReader(multipartReader);
        configurer.defaultCodecs().maxInMemorySize(requestAttributeDeploy.getMaxInMemorySize());

        WebFluxConfigurer.super.configureHttpMessageCodecs(configurer);
    }

}
