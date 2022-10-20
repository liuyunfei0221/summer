package com.blue.media.config.universal;

import com.blue.media.config.deploy.RequestAttributeDeploy;
import com.blue.media.config.deploy.WebDeploy;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.List;

import static com.blue.basic.common.base.CommonFunctions.HTTP_MESSAGE_CONVERTERS;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static reactor.core.scheduler.Schedulers.single;


/**
 * web config
 *
 * @author liuyunfei
 */
@Configuration
public class WebConfig implements WebFluxConfigurer {

    private final WebDeploy webDeploy;

    private final RequestAttributeDeploy requestAttributeDeploy;

    public WebConfig(WebDeploy webDeploy, RequestAttributeDeploy requestAttributeDeploy) {
        this.webDeploy = webDeploy;
        this.requestAttributeDeploy = requestAttributeDeploy;
    }

    @Bean
    HttpMessageConverters httpMessageConverters() {
        return HTTP_MESSAGE_CONVERTERS;
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();

        partReader.setStreaming(false);
        partReader.setHeadersCharset(UTF_8);
        partReader.setBlockingOperationScheduler(single());

        ofNullable(requestAttributeDeploy.getMaxInMemorySize())
                .map(Long::intValue).ifPresent(partReader::setMaxInMemorySize);
        ofNullable(requestAttributeDeploy.getMaxDiskUsagePerPart())
                .ifPresent(partReader::setMaxDiskUsagePerPart);
        ofNullable(requestAttributeDeploy.getMaxParts())
                .map(Long::intValue).ifPresent(partReader::setMaxParts);
        ofNullable(requestAttributeDeploy.getEnableLoggingRequestDetails())
                .ifPresent(partReader::setEnableLoggingRequestDetails);

        MultipartHttpMessageReader multipartReader = new MultipartHttpMessageReader(partReader);
        multipartReader.setEnableLoggingRequestDetails(requestAttributeDeploy.getEnableLoggingRequestDetails());

        configurer.defaultCodecs().multipartReader(multipartReader);
        configurer.defaultCodecs().maxInMemorySize(webDeploy.getMaxInMemorySize());

        WebFluxConfigurer.super.configureHttpMessageCodecs(configurer);
    }

    @Bean
    List<HttpMessageReader<?>> httpMessageReaders(ServerCodecConfigurer configurer) {
        return configurer.getReaders();
    }

    @Bean
    List<HttpMessageWriter<?>> httpMessageWriters(ServerCodecConfigurer configurer) {
        return configurer.getWriters();
    }

}
