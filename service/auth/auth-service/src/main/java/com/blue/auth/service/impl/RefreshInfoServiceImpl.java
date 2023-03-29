package com.blue.auth.service.impl;

import com.blue.auth.repository.entity.RefreshInfo;
import com.blue.auth.repository.template.RefreshInfoRepository;
import com.blue.auth.service.inter.RefreshInfoService;
import com.blue.basic.model.exps.BlueException;
import org.slf4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * refresh info service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class RefreshInfoServiceImpl implements RefreshInfoService {

    private static final Logger LOGGER = getLogger(RefreshInfoServiceImpl.class);

    private final RefreshInfoRepository refreshInfoRepository;

    public RefreshInfoServiceImpl(RefreshInfoRepository refreshInfoRepository) {
        this.refreshInfoRepository = refreshInfoRepository;
    }

    /**
     * insert refresh info
     *
     * @param refreshInfo
     * @return
     */
    @Override
    public Mono<RefreshInfo> insertRefreshInfo(RefreshInfo refreshInfo) {
        LOGGER.info("refreshInfo = {}", refreshInfo);
        if (isNull(refreshInfo))
            throw new BlueException(EMPTY_PARAM);

        return refreshInfoRepository.save(refreshInfo);
    }

    /**
     * delete refresh info
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Void> deleteRefreshInfo(String id) {
        LOGGER.info("id = {}", id);
        if (isBlank(id))
            throw new BlueException(INVALID_IDENTITY);

        return refreshInfoRepository.deleteById(id);
    }

    /**
     * delete refresh info
     *
     * @param refreshInfos
     * @return
     */
    @Override
    public Mono<Void> deleteRefreshInfos(List<RefreshInfo> refreshInfos) {
        LOGGER.info("refreshInfos = {}", refreshInfos);
        if (isEmpty(refreshInfos))
            throw new BlueException(EMPTY_PARAM);

        return refreshInfoRepository.deleteAll(refreshInfos);
    }

    /**
     * get refresh info by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<RefreshInfo> getRefreshInfo(String id) {
        LOGGER.info("id = {}", id);
        if (isBlank(id))
            throw new BlueException(INVALID_IDENTITY);

        return refreshInfoRepository.findById(id);
    }

    /**
     * select refresh info mono by probe
     *
     * @param probe
     * @return
     */
    @Override
    public Mono<List<RefreshInfo>> selectRefreshInfoByProbe(RefreshInfo probe) {
        LOGGER.info("probe = {}", probe);
        if (isNull(probe))
            throw new BlueException(EMPTY_PARAM);

        return refreshInfoRepository.findAll(Example.of(probe)).collectList();
    }

}
