package com.blue.media.component.file;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.component.file.preprocessor.inter.PreAndPostWriteProcessorHandler;
import com.blue.media.component.file.processor.inter.ByteHandler;
import com.blue.media.config.deploy.LocalDiskFileDeploy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Map;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static reactor.util.Loggers.getLogger;

/**
 * byte processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
@Component
public class ByteProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(ByteProcessor.class);

    /**
     * around handler
     */
    private Map<Integer, PreAndPostWriteProcessorHandler> preAndPostWriteProcessorHandlers;

    /**
     * target byte handler
     */
    private ByteHandler byteHandler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

        Map<String, PreAndPostWriteProcessorHandler> processorHandlerOfType = applicationContext.getBeansOfType(PreAndPostWriteProcessorHandler.class);
        if (isNotEmpty(processorHandlerOfType)) {
            preAndPostWriteProcessorHandlers = processorHandlerOfType.values().stream().collect(toMap(p -> p.handlerType().identity, p -> p, (a, b) -> a));
        } else {
            preAndPostWriteProcessorHandlers = emptyMap();
        }

        Map<String, ByteHandler> byteHandlerOfType = applicationContext.getBeansOfType(ByteHandler.class);
        if (isEmpty(byteHandlerOfType))
            throw new RuntimeException("byteHandlers is empty");

        LocalDiskFileDeploy localDiskFileDeploy;
        try {
            localDiskFileDeploy = applicationContext.getBean(LocalDiskFileDeploy.class);
            LOGGER.info("ByteProcessor onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent), fileDeploy = {}", localDiskFileDeploy);
        } catch (BeansException e) {
            LOGGER.error("applicationContext.getBean(FileDeploy.class), e = {}", e);
            throw new RuntimeException("applicationContext.getBean(FileDeploy.class) failed");
        }

        String handlerType = ofNullable(localDiskFileDeploy.getHandlerType())
                .filter(BlueChecker::isNotBlank)
                .orElseThrow(() -> new RuntimeException("handlerType is blank"));

        byteHandler = byteHandlerOfType.values().stream()
                .filter(bh -> handlerType.equals(bh.handlerType().identity))
                .findAny().orElseThrow(() -> new RuntimeException("handler with target type " + handlerType + " not found"));
    }

    /**
     * write
     *
     * @param part
     * @param type
     * @param memberId
     * @return
     */
    public Mono<FileUploadResult> write(Part part, Integer type, Long memberId) {
        LOGGER.info("Mono<FileUploadResult> write(Part part, Integer type, Long memberId), part = {}, type = {}, memberId = {}",
                part, type, memberId);
        if (isNull(part) || isNull(type) || isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return ofNullable(preAndPostWriteProcessorHandlers.get(type))
                .map(h -> h.preHandle(part, memberId)
                        .flatMap(p -> byteHandler.write(p, type, memberId))
                        .flatMap(fur -> h.postHandle(fur, memberId)))
                .orElseGet(() -> byteHandler.write(part, type, memberId));
    }

    /**
     * read
     *
     * @param link
     * @param memberId
     * @return
     */
    public Flux<DataBuffer> read(String link, Long memberId) {
        return byteHandler.read(link, memberId);
    }

}
