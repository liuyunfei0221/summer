package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.constant.base.BlueMediaType;
import com.blue.base.model.base.BlueResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.codec.DecodingException;
import reactor.util.Logger;

import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;
import static reactor.util.Loggers.getLogger;

/**
 * decoding exp handler
 *
 * @author liuyunfei
 * @date 2021/9/5
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaUndefineMagicConstant"})
public class DecodingExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(DecodingExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.core.codec.DecodingException";

    private static final Map<String, String> KNOWN_MESSAGE_MAPPING = Stream.of(BlueMediaType.values())
            .collect(Collectors.toMap(bmt -> "No multipart boundary found in Content-Type: \"" + bmt.identity + "\"",
                    bmt -> "<" + bmt.identity + ">get bound failed,Please specify <boundary> in <Content-Type>.", (a, b) -> a));

    private static final UnaryOperator<String> TOO_MANY_PARTS_MESSAGE_CONVERTER = message -> {
        int beginIndex = StringUtils.indexOf(message, "/");
        int endIndex = lastIndexOf(message, " ");

        return beginIndex != -1 && endIndex != -1 ? "file size can't be greater than" + substring(message, beginIndex + 1, endIndex) : message;
    };

    private static final UnaryOperator<String> NO_BOUNDARY_MESSAGE_CONVERTER = message ->
            ofNullable(KNOWN_MESSAGE_MAPPING.get(message)).orElse(message);


    /**
     * exp message converter
     * <p>
     * TODO By modifying this function to process all the information that may be encountered
     */
    private static final Function<DecodingException, String> MESSAGE_PARSER = decodingException -> {
        String originalMessage = ofNullable(decodingException.getMessage()).orElse(decodingException.getLocalizedMessage());

        String message;
        if (originalMessage.startsWith("Too many parts")) {
            message = TOO_MANY_PARTS_MESSAGE_CONVERTER.apply(originalMessage);
        } else if (originalMessage.startsWith("No multipart boundary")) {
            message = NO_BOUNDARY_MESSAGE_CONVERTER.apply(originalMessage);
        } else {
            message = originalMessage;
        }

        return message;
    };


    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("decodingExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);
        return new ExceptionHandleInfo(BAD_REQUEST.status, new BlueResponse<>(BAD_REQUEST.code, null, MESSAGE_PARSER.apply(((DecodingException) throwable))));
    }
}
