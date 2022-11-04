package com.blue.verify.component.sender;

import com.blue.basic.model.exps.BlueException;
import com.blue.verify.api.model.VerifyMessage;
import com.blue.verify.component.sender.inter.VerifyMessageSender;
import com.blue.verify.service.inter.VerifyTemplateService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyBusinessType;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyType;
import static com.blue.basic.constant.common.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * verify message send processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
@Order(HIGHEST_PRECEDENCE)
public class VerifyMessageSenderProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private final VerifyTemplateService verifyTemplateService;

    public VerifyMessageSenderProcessor(VerifyTemplateService verifyTemplateService) {
        this.verifyTemplateService = verifyTemplateService;
    }

    /**
     * verify message type ->verify message sender
     */
    private Map<String, VerifyMessageSender> verifyMessageSenders;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, VerifyMessageSender> beansOfType = applicationContext.getBeansOfType(VerifyMessageSender.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("qrCodeGenHandlers is empty");

        verifyMessageSenders = beansOfType.values().stream()
                .collect(toMap(s -> s.targetType().identity, s -> s, (a, b) -> a));
    }

    /**
     * send verify message
     *
     * @param verifyMessage
     * @return
     */
    public Mono<Boolean> send(VerifyMessage verifyMessage) {
        if (isNull(verifyMessage))
            return error(() -> new BlueException(EMPTY_PARAM));

        String verifyType = verifyMessage.getVerifyType();
        assertVerifyType(verifyType, false);
        String verifyBusinessType = verifyMessage.getBusinessType();
        assertVerifyBusinessType(verifyBusinessType, false);

        return verifyTemplateService.getVerifyTemplateInfoByTypesAndLanguages(verifyType, verifyBusinessType, verifyMessage.getLanguages())
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(templateInfo ->
                        ofNullable(verifyMessageSenders.get(verifyType))
                                .map(sender -> sender.send(verifyMessage, templateInfo))
                                .orElseThrow(() -> new BlueException(INVALID_IDENTITY)));
    }

}
