package com.blue.media.component.qr;

import com.blue.basic.model.common.Access;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.api.model.QrCodeConfigInfo;
import com.blue.media.component.qr.inter.QrCodeGenHandler;
import com.blue.media.model.QrCodeGenerateParam;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.assertQrCodeGenType;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * qr code stream generate processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public class QrCodeGenerateProcessor implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * handler type -> handler
     */
    private Map<Integer, QrCodeGenHandler> qrCodeGenHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, QrCodeGenHandler> beansOfType = applicationContext.getBeansOfType(QrCodeGenHandler.class);
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

        Integer genHandlerType = qrCodeConfigInfo.getGenHandlerType();
        assertQrCodeGenType(genHandlerType, false);

        return ofNullable(qrCodeGenHandlers.get(genHandlerType))
                .map(gh -> gh.generateCode(qrCodeGenerateParam, qrCodeConfigInfo, access, serverRequest))
                .orElseThrow(() -> new BlueException(INVALID_PARAM));
    }

}
