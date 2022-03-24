package com.blue.media.component.file;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.component.file.inter.ByteHandler;
import com.blue.media.config.deploy.FileDeploy;
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

import java.nio.file.Path;
import java.util.Map;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * byte processor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
@Component
public class ByteProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(ByteProcessor.class);

    /**
     * target byte handler
     */
    private ByteHandler byteHandler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, ByteHandler> beansOfType = applicationContext.getBeansOfType(ByteHandler.class);
        if (isEmpty(beansOfType))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "byteHandlers is empty");

        FileDeploy fileDeploy;
        try {
            fileDeploy = applicationContext.getBean(FileDeploy.class);
            LOGGER.info("ByteProcessor onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent), fileDeploy = {}", fileDeploy);
        } catch (BeansException e) {
            LOGGER.error("applicationContext.getBean(FileDeploy.class), e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "applicationContext.getBean(FileDeploy.class) failed");
        }

        String handlerType = ofNullable(fileDeploy.getHandlerType())
                .filter(BlueChecker::isNotBlank)
                .orElseThrow(() -> new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "handlerType is blank"));

        byteHandler = beansOfType.values().stream()
                .filter(bh -> handlerType.equals(bh.handlerType().identity))
                .findAny().orElseThrow(() -> new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "handler with target type " + handlerType + " not found"));
    }

    /**
     * write
     *
     * @param part
     * @return
     */
    public Mono<FileUploadResult> write(Part part) {
        return byteHandler.write(part);
    }

    /**
     * read
     *
     * @param path
     * @return
     */
    public Flux<DataBuffer> read(Path path) {
        return byteHandler.read(path);
    }

}
