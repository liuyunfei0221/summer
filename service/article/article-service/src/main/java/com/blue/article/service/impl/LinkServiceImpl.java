package com.blue.article.service.impl;

import com.blue.article.repository.entity.Link;
import com.blue.article.repository.template.LinkRepository;
import com.blue.article.service.inter.LinkService;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.BlueChecker.isValidIdentity;
import static com.blue.basic.common.base.ConstantProcessor.assertSubjectType;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * link service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class LinkServiceImpl implements LinkService {

    private static final Logger LOGGER = getLogger(LinkServiceImpl.class);

    private final LinkRepository linkRepository;

    public LinkServiceImpl(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
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
            return ofNullable(linkRepository.findById(id).toFuture().join());

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
//        return linkMapper.selectBySubIdAndSubType(subId, subType);
        return null;
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
