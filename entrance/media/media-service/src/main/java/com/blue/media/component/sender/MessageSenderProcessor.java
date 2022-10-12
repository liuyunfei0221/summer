package com.blue.media.component.sender;

import com.blue.basic.model.exps.BlueException;
import com.blue.media.api.model.Message;
import com.blue.media.component.sender.inter.MessageSender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.ConstantProcessor.assertMessageBusinessType;
import static com.blue.basic.common.base.ConstantProcessor.assertMessageType;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * message send processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
@Order(HIGHEST_PRECEDENCE)
public class MessageSenderProcessor implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * message type -> sender
     */
    private Map<Integer, MessageSender> messageSenders;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, MessageSender> beansOfType = applicationContext.getBeansOfType(MessageSender.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("qrCodeGenHandlers is empty");

        messageSenders = beansOfType.values().stream()
                .collect(toMap(s -> s.targetType().identity, s -> s, (a, b) -> a));
    }

    /**
     * send message
     *
     * @param message
     * @return
     */
    public Mono<Boolean> send(Message message) {
        Integer type = message.getType();

        assertMessageType(type, false);
        assertMessageBusinessType(message.getBusinessType(), false);

        return ofNullable(messageSenders.get(type))
                .map(s -> s.send(message))
                .orElseThrow(() -> new BlueException(INVALID_IDENTITY));
    }

}
