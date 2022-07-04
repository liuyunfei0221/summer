package com.blue.media.component.qr;

import com.blue.base.model.common.Access;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.QrCodeConfigInfo;
import com.blue.media.component.qr.inter.QrCodeGenHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.ConstantProcessor.assertQrCodeGenType;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.common.ResponseElement.INVALID_PARAM;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * verify processor
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
     * generate qr code stream
     *
     * @param qrCodeConfigInfo
     * @param access
     * @param param
     * @return
     */
    public Mono<byte[]> generate(QrCodeConfigInfo qrCodeConfigInfo, Access access, Map<String, String> param) {
        if (isNull(qrCodeConfigInfo))
            throw new BlueException(BAD_REQUEST);

        Integer genHandlerType = qrCodeConfigInfo.getGenHandlerType();
        assertQrCodeGenType(genHandlerType, false);

        return ofNullable(qrCodeGenHandlers.get(genHandlerType))
                .map(gh -> gh.generate(access, param, qrCodeConfigInfo))
                .orElseThrow(() -> new BlueException(INVALID_PARAM));
    }

}
