package com.blue.media.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.media.api.model.QrCodeConfigInfo;
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
    QrCodeConfigInfo insertStyle(QrCodeConfigInsertParam qrCodeConfigInsertParam, Long operatorId);

    /**
     * update qr code config
     *
     * @param qrCodeConfigUpdateParam
     * @param operatorId
     * @return
     */
    QrCodeConfigInfo updateStyle(QrCodeConfigUpdateParam qrCodeConfigUpdateParam, Long operatorId);

    /**
     * delete style
     *
     * @param id
     * @return
     */
    QrCodeConfigInfo deleteStyle(Long id);

    /**
     * update active style by id
     *
     * @param id
     * @param operatorId
     * @return
     */
    QrCodeConfigInfo updateActiveStyle(Long id, Long operatorId);

    /**
     * expire style info
     *
     * @return
     */
    void invalidStyleInfosCache();

    /**
     * get style by id
     *
     * @param id
     * @return
     */
    Optional<QrCodeConfig> getStyle(Long id);

    /**
     * get style mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<QrCodeConfig>> getStyleMono(Long id);

    /**
     * select all style
     *
     * @return
     */
    Mono<List<QrCodeConfig>> selectStyle();

    /**
     * list style by type and active
     *
     * @param styleType
     * @param isActive
     * @return
     */
    List<QrCodeConfig> selectByTypeAndActive(Integer styleType, Boolean isActive);

    /**
     * get active style
     *
     * @param styleType
     * @return
     */
    Mono<QrCodeConfigInfo> getActiveStyleInfoMonoByTypeWithCache(Integer styleType);

    /**
     * select style by page and condition
     *
     * @param limit
     * @param rows
     * @param qrCodeCondition
     * @return
     */
    Mono<List<QrCodeConfig>> selectStyleMonoByLimitAndCondition(Long limit, Long rows, QrCodeCondition qrCodeCondition);

    /**
     * count style by condition
     *
     * @param qrCodeCondition
     * @return
     */
    Mono<Long> countStyleMonoByCondition(QrCodeCondition qrCodeCondition);

    /**
     * select style manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<QrCodeConfigInfo>> selectStyleManagerInfoPageMonoByPageAndCondition(PageModelRequest<QrCodeCondition> pageModelRequest);

}
