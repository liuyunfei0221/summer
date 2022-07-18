package com.blue.media.component.qr.impl;

import com.blue.basic.common.base.BlueRandomGenerator;
import com.blue.basic.constant.common.RandomType;
import com.blue.basic.constant.media.QrCodeGenType;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.api.model.QrCodeConfigInfo;
import com.blue.media.component.qr.inter.QrCodeGenHandler;
import com.blue.media.model.QrCodeGenerateParam;
import com.blue.media.service.inter.ByteOperateService;
import com.blue.qr.component.QrCoder;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.common.Symbol.*;
import static com.blue.basic.constant.media.AttachmentType.QR_CODE;
import static com.blue.basic.constant.media.QrCodeGenType.USER_INFO;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * qr code generate handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public class MemberInfoQrCodeGenHandler implements QrCodeGenHandler {

    private final ByteOperateService byteOperateService;

    private final QrCoder qrCoder;

    public MemberInfoQrCodeGenHandler(ByteOperateService byteOperateService, QrCoder qrCoder) {
        this.byteOperateService = byteOperateService;
        this.qrCoder = qrCoder;
    }

    /**
     * generate
     *
     * @param qrCodeGenerateParam
     * @param qrCodeConfigInfo
     * @param access
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> generateCode(QrCodeGenerateParam qrCodeGenerateParam, QrCodeConfigInfo qrCodeConfigInfo, Access access, ServerRequest serverRequest) {
        if (isNull(access))
            throw new BlueException(INVALID_PARAM);

        long memberId = access.getId();

        List<String> placeholders = Stream.of(String.valueOf(memberId)).collect(toList());

        String pathToBeFilled = qrCodeConfigInfo.getPathToBeFilled();

        String path = String.format(pathToBeFilled, placeholders);
        String domain = qrCodeConfigInfo.getDomain();

        String content = domain + PATH_SEPARATOR.identity + path;
        String descName = "E:\\tempFile\\disc\\qr\\" + memberId + "\\"
                + memberId + PAR_CONCATENATION_DATABASE_URL.identity + BlueRandomGenerator.generate(RandomType.ALPHABETIC, 6)
                + SCHEME_SEPARATOR.identity + qrCoder.getFileType();

        return byteOperateService.upload(qrCoder.generateCodeWithoutLogo(content), QR_CODE.identity, memberId, EMPTY_DATA.value, descName)
                .flatMap(aui ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(aui), BlueResponse.class));
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
