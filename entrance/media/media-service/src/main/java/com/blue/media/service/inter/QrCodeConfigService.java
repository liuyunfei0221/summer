package com.blue.media.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.media.api.model.QrCodeConfigInfo;
import com.blue.media.model.QrCodeCondition;
import com.blue.media.model.QrCodeConfigInsertParam;
import com.blue.media.model.QrCodeConfigManagerInfo;
import com.blue.media.model.QrCodeConfigUpdateParam;
import com.blue.media.repository.entity.QrCodeConfig;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

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
    Mono<QrCodeConfigInfo> insertQrCodeConfig(QrCodeConfigInsertParam qrCodeConfigInsertParam, Long operatorId);

    /**
     * update qr code config
     *
     * @param qrCodeConfigUpdateParam
     * @param operatorId
     * @return
     */
    Mono<QrCodeConfigInfo> updateQrCodeConfig(QrCodeConfigUpdateParam qrCodeConfigUpdateParam, Long operatorId);

    /**
     * delete config
     *
     * @param id
     * @return
     */
    Mono<QrCodeConfigInfo> deleteQrCodeConfig(Long id);

    /**
     * get config mono by id
     *
     * @param id
     * @return
     */
    Mono<QrCodeConfig> getQrCodeConfigMono(Long id);

    /**
     * get config mono by type
     *
     * @param type
     * @return
     */
    Mono<QrCodeConfigInfo> getQrCodeConfigInfoMonoByType(Integer type);

    /**
     * select config by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<QrCodeConfig>> selectQrCodeConfigMonoByLimitAndCondition(Long limit, Long rows, Query query);

    /**
     * count config by query
     *
     * @param query
     * @return
     */
    Mono<Long> countQrCodeConfigMonoByCondition(Query query);

    /**
     * select config manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<QrCodeConfigManagerInfo>> selectQrCodeConfigManagerInfoPageMonoByPageAndCondition(PageModelRequest<QrCodeCondition> pageModelRequest);

}
