package com.blue.media.component.qr.impl;

import com.blue.base.constant.media.QrCodeGenType;
import com.blue.base.model.common.Access;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.QrCodeConfigInfo;
import com.blue.media.component.qr.inter.QrCodeGenHandler;
import com.blue.qr.component.QrCoder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.base.constant.common.Symbol.PATH_SEPARATOR;
import static com.blue.base.constant.media.QrCodeGenType.USER_INFO;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;

/**
 * qr code generate handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public class MemberInfoQrCodeGenHandler implements QrCodeGenHandler {

    private final QrCoder qrCoder;

    public MemberInfoQrCodeGenHandler(QrCoder qrCoder) {
        this.qrCoder = qrCoder;
    }

    /**
     * generate
     *
     * @param access
     * @param param
     * @param qrCodeConfigInfo
     * @return
     */
    @Override
    public Mono<byte[]> generate(Access access, Map<String, String> param, QrCodeConfigInfo qrCodeConfigInfo) {
        if (isNull(access))
            throw new BlueException(INVALID_PARAM);

        List<String> placeholders = Stream.of(String.valueOf(access.getId())).collect(toList());

        String pathToBeFilled = qrCodeConfigInfo.getPathToBeFilled();

        String path = String.format(pathToBeFilled, placeholders);
        String domain = qrCodeConfigInfo.getDomain();

        String content = domain + PATH_SEPARATOR.identity + path;

        return just(qrCoder.generateCodeWithoutLogo(content));
    }

    /**
     * target qr code gen type to process
     *
     * @return
     */
    @Override
    public QrCodeGenType targetType() {
        return USER_INFO;
    }

}
