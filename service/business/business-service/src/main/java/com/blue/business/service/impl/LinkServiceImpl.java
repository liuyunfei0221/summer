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
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * 相关链接业务实现
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
     * 根据主键查询相关链接信息
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Link> getByPrimaryKey(Long id) {
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "主键不能为空或小于1");

        LOGGER.info("id = {}", id);
        return ofNullable(linkMapper.selectByPrimaryKey(id));
    }

    /**
     * 根据主键集批量查询相关链接信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<Link> listByIds(List<Long> ids) {
        return null;
    }

    /**
     * 根据主题id和主题类型查询相关链接
     *
     * @param subId
     * @param subType
     * @return
     */
    @Override
    public List<Link> listBySubIdAndSubType(Long subId, Integer subType) {
        if (subId == null || subId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "subId不能为空或小于1");

        if (subType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "subType不能为空或小于1");

        ConstantProcessor.assertSubjectType(subType);
        LOGGER.info("subId = {},subType = {}", subId, subType);

        return linkMapper.listBySubIdAndSubType(subId, subType);
    }

    /**
     * 添加相关链接
     *
     * @param link
     */
    @Override
    public void insert(Link link) {

    }

    /**
     * 批量添加相关链接
     *
     * @param links
     */
    @Override
    public void insertBatch(List<Link> links) {

    }

    /**
     * 修改相关链接
     *
     * @param link
     */
    @Override
    public void update(Link link) {

    }

}
