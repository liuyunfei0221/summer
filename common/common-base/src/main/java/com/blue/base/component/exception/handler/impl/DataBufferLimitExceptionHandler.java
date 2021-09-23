package com.blue.base.component.exception.handler.impl;

import com.blue.base.component.exception.handler.inter.ExceptionHandler;
import com.blue.base.component.exception.handler.model.ExceptionHandleInfo;
import com.blue.base.model.base.BlueResult;
import org.springframework.core.io.buffer.DataBufferLimitException;
import reactor.util.Logger;

import java.util.Optional;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static org.apache.commons.lang3.StringUtils.indexOf;
import static reactor.util.Loggers.getLogger;


/**
 * DataBufferLimitException处理器
 *
 * @author liuyunfei
 * @date 2021/8/23
 * @apiNote
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class DataBufferLimitExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = getLogger(DataBufferLimitExceptionHandler.class);

    private static final String EXP_NAME = "org.springframework.core.io.buffer.DataBufferLimitException";

    private static final String TAR_WORD = "of";
    private static final int TAR_WORD_LEN = TAR_WORD.length();
    private static final String MSG_PRE = "文件大小不能超过";
    private static final String DEFAULT_MSG = "文件大小不合法";

    private static final Function<DataBufferLimitException, String> MESSAGE_CONVERTER = exception ->
            Optional.ofNullable(exception)
                    .map(DataBufferLimitException::getMessage)
                    .map(msg -> {
                        int idx = indexOf(msg, TAR_WORD);
                        if (idx != -1)
                            return MSG_PRE + msg.substring(idx + TAR_WORD_LEN);

                        return DEFAULT_MSG;
                    }).orElse(DEFAULT_MSG);


    @Override
    public String exceptionName() {
        return EXP_NAME;
    }

    @Override
    public ExceptionHandleInfo handle(Throwable throwable) {
        LOGGER.info("dataBufferLimitExceptionHandler -> handle(Throwable throwable), throwable = {0}", throwable);

        DataBufferLimitException exception = (DataBufferLimitException) throwable;
        return new ExceptionHandleInfo(BAD_REQUEST.status, new BlueResult<>(BAD_REQUEST.code, null, MESSAGE_CONVERTER.apply(exception)));
    }

}
