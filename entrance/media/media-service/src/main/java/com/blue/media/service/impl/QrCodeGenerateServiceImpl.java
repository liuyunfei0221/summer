package com.blue.media.service.impl;

import com.blue.basic.model.common.Access;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.component.qr.QrCodeGenerateProcessor;
import com.blue.media.model.QrCodeGenerateParam;
import com.blue.media.service.inter.QrCodeConfigService;
import com.blue.media.service.inter.QrCodeGenerateService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.FORBIDDEN;
import static reactor.core.publisher.Mono.*;

/**
 * qr code generate service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class QrCodeGenerateServiceImpl implements QrCodeGenerateService {

    private static final Logger LOGGER = Loggers.getLogger(QrCodeGenerateServiceImpl.class);

    private QrCodeGenerateProcessor qrCodeGenerateProcessor;

    private QrCodeConfigService qrCodeConfigService;

    public QrCodeGenerateServiceImpl(QrCodeGenerateProcessor qrCodeGenerateProcessor, QrCodeConfigService qrCodeConfigService) {
        this.qrCodeGenerateProcessor = qrCodeGenerateProcessor;
        this.qrCodeConfigService = qrCodeConfigService;
    }

    private final Function<ServerRequest, Mono<ServerResponse>> generateQrCodeHandler = serverRequest ->
            zip(getAccessReact(serverRequest),
                    serverRequest.bodyToMono(QrCodeGenerateParam.class)
                            .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))))
                    .flatMap(tuple2 -> {
                        Access access = tuple2.getT1();

                        QrCodeGenerateParam qrCodeGenerateParam = tuple2.getT2();
                        qrCodeGenerateParam.asserts();

                        return qrCodeConfigService.getQrCodeConfigInfoMonoByType(qrCodeGenerateParam.getType())
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                                .flatMap(qrCodeConfigInfo -> {
                                    Set<Long> allowedRoleSet = new HashSet<>(qrCodeConfigInfo.getAllowedRoles());
                                    if (access.getRoleIds().stream().noneMatch(allowedRoleSet::contains))
                                        return error(() -> new BlueException(FORBIDDEN));

                                    return qrCodeGenerateProcessor.generateCode(qrCodeGenerateParam, qrCodeConfigInfo, access, serverRequest);
                                });
                    });


    /**
     * generate qr code
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> generateCode(ServerRequest serverRequest) {
        return generateQrCodeHandler.apply(serverRequest);
    }

}
