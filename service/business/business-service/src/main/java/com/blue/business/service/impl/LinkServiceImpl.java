package com.blue.business.service.impl;

import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.model.exps.BlueException;
import com.blue.business.repository.entity.Link;
import com.blue.business.repository.mapper.LinkMapper;
import com.blue.business.service.inter.LinkService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * link service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class LinkServiceImpl implements LinkService {

    private static final Logger LOGGER = getLogger(LinkServiceImpl.class);

    private final LinkMapper linkMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public LinkServiceImpl(LinkMapper linkMapper) {
        this.linkMapper = linkMapper;
    }

    /**
     * get link by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Link> getByPrimaryKey(Long id) {
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        LOGGER.info("id = {}", id);
        return ofNullable(linkMapper.selectByPrimaryKey(id));
    }

    /**
     * list link by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<Link> listByIds(List<Long> ids) {
        return null;
    }

    /**
     * list link by subject id and subject type
     *
     * @param subId
     * @param subType
     * @return
     */
    @Override
    public List<Link> listBySubIdAndSubType(Long subId, Integer subType) {
        if (subId == null || subId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        if (subType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "subType不能为空或小于1");

        ConstantProcessor.assertSubjectType(subType);
        LOGGER.info("subId = {},subType = {}", subId, subType);

        return linkMapper.listBySubIdAndSubType(subId, subType);
    }

    /**
     * insert link
     *
     * @param link
     */
    @Override
    public void insert(Link link) {

    }

    /**
     * insert link batch
     *
     * @param links
     */
    @Override
    public void insertBatch(List<Link> links) {

    }

    /**
     * update link
     *
     * @param link
     */
    @Override
    public void update(Link link) {

    }

}
