package com.blue.media.component.qr;

import com.blue.basic.model.common.Access;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.api.model.QrCodeConfigInfo;
import com.blue.media.component.qr.inter.QrCodeGenerateHandler;
import com.blue.media.model.QrCodeGenerateParam;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.assertQrCodeType;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * qr code stream generate processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
@Order(HIGHEST_PRECEDENCE)
public class QrCodeGenerateProcessor implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * handler type -> handler
     */
    private Map<Integer, QrCodeGenerateHandler> qrCodeGenHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, QrCodeGenerateHandler> beansOfType = applicationContext.getBeansOfType(QrCodeGenerateHandler.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("qrCodeGenHandlers is empty");

        qrCodeGenHandlers = beansOfType.values().stream()
                .collect(toMap(gh -> gh.targetType().identity, gh -> gh, (a, b) -> a));
    }

    /**
     * generate qr code
     *
     * @param qrCodeConfigInfo
     * @param qrCodeGenerateParam
     * @param access
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> generateCode(QrCodeGenerateParam qrCodeGenerateParam, QrCodeConfigInfo qrCodeConfigInfo, Access access, ServerRequest serverRequest) {
        if (isNull(qrCodeConfigInfo))
            throw new BlueException(BAD_REQUEST);

        Integer type = qrCodeConfigInfo.getType();
        assertQrCodeType(type, false);

        return ofNullable(qrCodeGenHandlers.get(type))
                .map(gh -> gh.generateCode(qrCodeGenerateParam, qrCodeConfigInfo, access, serverRequest))
                .orElseThrow(() -> new BlueException(INVALID_PARAM));
    }

}