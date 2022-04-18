package com.blue.article.service.inter;

import com.blue.article.repository.entity.Link;

import java.util.List;
import java.util.Optional;

/**
 * link service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnnecessaryInterfaceModifier", "unused"})
public interface LinkService {

    /**
     * get link by id
     *
     * @param id
     * @return
     */
    public Optional<Link> getByPrimaryKey(Long id);

    /**
     * list link by ids
     *
     * @param ids
     * @return
     */
    public List<Link> selectByIds(List<Long> ids);

    /**
     * list link by subject id and subject type
     *
     * @param subId
     * @param subType
     * @return
     */
    public List<Link> selectBySubIdAndSubType(Long subId, Integer subType);

    /**
     * insert link
     *
     * @param link
     */
    public void insert(Link link);

    /**
     * insert link batch
     *
     * @param links
     */
    public void insertBatch(List<Link> links);

    /**
     * update link
     *
     * @param link
     */
    public void update(Link link);

}
