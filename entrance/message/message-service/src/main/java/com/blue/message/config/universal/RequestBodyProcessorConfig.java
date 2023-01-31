package com.blue.message.config.universal;

import com.blue.basic.common.base.XssAsserter;
import com.blue.basic.common.content.common.StringProcessor;
import com.blue.basic.common.content.handler.inter.StringHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * request body processor config
 *
 * @author liuyunfei
 */
@Configuration
public class RequestBodyProcessorConfig {

    @Bean
    StringProcessor stringProcessor(){

        /*
         * TODO Here is an extension point
         * Used to assert and process the request body. If the operation here is too time-consuming, it will affect the performance of the gateway.
         * So I don't recommend you to do anything here. The following only uses XSS filtering as an example. You can use bean, reflection, and spi according to your needs.
         * Wait for the configuration here, because the generalized configuration is too cumbersome and I don't think it is necessary, so I won't do anything here anymore. If you want to do it,
         * Maybe you can take a look at the global exception handling in common-basic and the configuration item handling in common-identity
         */
        StringHandler handler = requestBody -> {
            XssAsserter.check(requestBody);
            return requestBody;
        };

        List<StringHandler> stringHandlersChain = singletonList(handler);

        return new StringProcessor(stringHandlersChain);
    }

}
