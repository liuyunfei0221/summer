package com.blue.business.service.inter;

import com.blue.business.repository.entity.Link;

import java.util.List;
import java.util.Optional;

/**
 * 相关链接业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "UnnecessaryInterfaceModifier", "unused"})
public interface LinkService {

    /**
     * 根据主键查询相关链接信息
     *
     * @param id
     * @return
     */
    public Optional<Link> getByPrimaryKey(Long id);

    /**
     * 根据主键集批量查询相关链接信息
     *
     * @param ids
     * @return
     */
    public List<Link> listByIds(List<Long> ids);

    /**
     * 根据主题id和主题类型查询相关链接
     *
     * @param subId
     * @param subType
     * @return
     */
    public List<Link> listBySubIdAndSubType(Long subId, Integer subType);

    /**
     * 添加相关链接
     *
     * @param link
     */
    public void insert(Link link);

    /**
     * 批量添加相关链接
     *
     * @param links
     */
    public void insertBatch(List<Link> links);

    /**
     * 修改相关链接
     *
     * @param link
     */
    public void update(Link link);

}
