package com.blue.verify.component.sender;

import com.blue.basic.model.exps.BlueException;
import com.blue.verify.api.model.VerifyMessage;
import com.blue.verify.component.sender.inter.VerifyMessageSender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyType;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.error;

/**
 * verify message send processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public class VerifyMessageSenderProcessor implements ApplicationListener<ContextRefreshedEvent> {

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

        return ofNullable(verifyMessageSenders.get(verifyType))
                .map(s -> s.send(verifyMessage))
                .orElseThrow(() -> new BlueException(INVALID_IDENTITY));
    }

}
