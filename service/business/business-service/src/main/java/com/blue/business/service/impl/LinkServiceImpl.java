package com.blue.business.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.business.repository.entity.Link;
import com.blue.business.repository.mapper.LinkMapper;
import com.blue.business.service.inter.LinkService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.common.base.Asserter.isInvalidIdentity;
import static com.blue.base.common.base.Asserter.isValidIdentity;
import static com.blue.base.common.base.ConstantProcessor.assertSubjectType;
import static com.blue.base.constant.base.ResponseElement.*;
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
        LOGGER.info("id = {}", id);
        if (isValidIdentity(id))
            return ofNullable(linkMapper.selectByPrimaryKey(id));

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * list link by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<Link> selectByIds(List<Long> ids) {
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
    public List<Link> selectBySubIdAndSubType(Long subId, Integer subType) {
        LOGGER.info("subId = {},subType = {}", subId, subType);
        if (isInvalidIdentity(subId))
            throw new BlueException(INVALID_IDENTITY);

        assertSubjectType(subType, false);
        return linkMapper.selectBySubIdAndSubType(subId, subType);
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
