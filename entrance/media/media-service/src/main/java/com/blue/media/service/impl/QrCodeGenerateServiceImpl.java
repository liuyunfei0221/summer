package com.blue.media.service.impl;

import com.blue.base.model.common.Access;
import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.media.component.qr.QrCodeGenerateProcessor;
import com.blue.media.model.QrCodeGenerateParam;
import com.blue.media.service.inter.ByteOperateService;
import com.blue.media.service.inter.QrCodeConfigService;
import com.blue.media.service.inter.QrCodeGenerateService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.HashSet;
import java.util.Set;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.media.AttachmentType.QR_CODE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * qr code generate service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class QrCodeGenerateServiceImpl implements QrCodeGenerateService {

    private static final Logger LOGGER = Loggers.getLogger(QrCodeGenerateServiceImpl.class);

    private final QrCodeGenerateProcessor qrCodeGenerateProcessor;

    private final ByteOperateService byteOperateService;

    private final QrCodeConfigService qrCodeConfigService;

    public QrCodeGenerateServiceImpl(QrCodeGenerateProcessor qrCodeGenerateProcessor, ByteOperateService byteOperateService, QrCodeConfigService qrCodeConfigService) {
        this.qrCodeGenerateProcessor = qrCodeGenerateProcessor;
        this.byteOperateService = byteOperateService;
        this.qrCodeConfigService = qrCodeConfigService;
    }

    /**
     * generate qr code
     *
     * @param access
     * @param qrCodeGenerateParam
     * @return
     */
    @Override
    public Mono<ServerResponse> generateCode(Access access, QrCodeGenerateParam qrCodeGenerateParam) {
        if (isNull(access))
            throw new BlueException(UNAUTHORIZED);
        if (isNull(qrCodeGenerateParam))
            throw new BlueException(EMPTY_PARAM);
        qrCodeGenerateParam.asserts();

        return qrCodeConfigService.getQrCodeConfigInfoMonoByType(qrCodeGenerateParam.getType())
                .flatMap(qrCodeConfigInfo -> {
                    Set<Long> allowedRoleSet = new HashSet<>(qrCodeConfigInfo.getAllowedRoles());
                    if (access.getRoleIds().stream().noneMatch(allowedRoleSet::contains))
                        return error(() -> new BlueException(FORBIDDEN));

                    return qrCodeGenerateProcessor.generate(qrCodeConfigInfo, access, qrCodeGenerateParam.getElements());
                }).flatMap(bytes ->
                        byteOperateService.upload(bytes, QR_CODE.identity, access.getId(), EMPTY_DATA.value, "")
                                .flatMap(summary ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, summary), BlueResponse.class)
                                ));
    }

}
