package com.blue.media.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.media.api.model.QrCodeConfigInfo;
import com.blue.media.api.model.QrCodeConfigManagerInfo;
import com.blue.media.model.QrCodeCondition;
import com.blue.media.model.QrCodeConfigInsertParam;
import com.blue.media.model.QrCodeConfigUpdateParam;
import com.blue.media.repository.entity.QrCodeConfig;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * qr code config service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface QrCodeConfigService {

    /**
     * insert qr code config
     *
     * @param qrCodeConfigInsertParam
     * @param operatorId
     * @return
     */
    QrCodeConfigInfo insertQrCodeConfig(QrCodeConfigInsertParam qrCodeConfigInsertParam, Long operatorId);

    /**
     * update qr code config
     *
     * @param qrCodeConfigUpdateParam
     * @param operatorId
     * @return
     */
    QrCodeConfigInfo updateQrCodeConfig(QrCodeConfigUpdateParam qrCodeConfigUpdateParam, Long operatorId);

    /**
     * delete config
     *
     * @param id
     * @return
     */
    QrCodeConfigInfo deleteQrCodeConfig(Long id);

    /**
     * get config by id
     *
     * @param id
     * @return
     */
    Optional<QrCodeConfig> getQrCodeConfig(Long id);

    /**
     * get config mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<QrCodeConfig>> getQrCodeConfigMono(Long id);

    /**
     * select all config
     *
     * @return
     */
    Mono<List<QrCodeConfig>> selectQrCodeConfig();

    /**
     * list config by type and active
     *
     * @param configType
     * @return
     */
    List<QrCodeConfig> selectQrCodeConfigByType(Integer configType);

    /**
     * get active config
     *
     * @param configType
     * @return
     */
    Mono<QrCodeConfigInfo> getQrCodeConfigInfoMonoByTypeWithCache(Integer configType);

    /**
     * select config by page and condition
     *
     * @param limit
     * @param rows
     * @param qrCodeCondition
     * @return
     */
    Mono<List<QrCodeConfig>> selectQrCodeConfigMonoByLimitAndCondition(Long limit, Long rows, QrCodeCondition qrCodeCondition);

    /**
     * count config by condition
     *
     * @param qrCodeCondition
     * @return
     */
    Mono<Long> countQrCodeConfigMonoByCondition(QrCodeCondition qrCodeCondition);

    /**
     * select config manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<QrCodeConfigManagerInfo>> selectQrCodeConfigManagerInfoPageMonoByPageAndCondition(PageModelRequest<QrCodeCondition> pageModelRequest);

}
