package com.blue.file.config.universal;

import com.blue.base.common.content.common.RequestBodyProcessor;
import com.blue.base.common.content.handler.inter.RequestBodyHandler;
import com.blue.base.common.base.XssAsserter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * 请求体处理组件配置类
 *
 * @author DarkBlue
 */
@Configuration
public class RequestBodyProcessorConfig {

    @Bean
    RequestBodyProcessor requestBodyProcessor(){

        /**
         * TODO 这里是个扩展点
         * 用于对请求体断言和处理,如果这里的操作过于耗时的话会影响网关的性能,
         * 所以我并不建议你在这里做什么处理,下面仅以XSS过滤作为示例,你可以根据自己的需要使用bean,反射,spi
         * 等等用于此处的配置,因为通用化配置的话过于繁琐且我认为没什么必要,所以不再对此处做处理,如果你想做,
         * 或许你可以看一看common-base中的全局异常处理及common-identity中的配置项处理
         */

        RequestBodyHandler handler = requestBody -> {
            XssAsserter.check(requestBody);
            return requestBody;
        };

        List<RequestBodyHandler> requestBodyHandlersChain = singletonList(handler);

        return new RequestBodyProcessor(requestBodyHandlersChain);
    }

}
