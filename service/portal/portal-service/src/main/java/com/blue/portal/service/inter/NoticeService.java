package com.blue.portal.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.portal.api.model.NoticeInfo;
import com.blue.portal.model.NoticeCondition;
import com.blue.portal.model.NoticeInsertParam;
import com.blue.portal.model.NoticeManagerInfo;
import com.blue.portal.model.NoticeUpdateParam;
import com.blue.portal.repository.entity.Notice;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * notice service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface NoticeService {

    /**
     * insert notice
     *
     * @param noticeInsertParam
     * @param operatorId
     * @return
     */
    NoticeInfo insertNotice(NoticeInsertParam noticeInsertParam, Long operatorId);

    /**
     * update notice
     *
     * @param noticeUpdateParam
     * @param operatorId
     * @return
     */
    NoticeInfo updateNotice(NoticeUpdateParam noticeUpdateParam, Long operatorId);

    /**
     * delete notice
     *
     * @param id
     * @return
     */
    NoticeInfo deleteNotice(Long id);

    /**
     * expire notice info
     *
     * @return
     */
    void invalidNoticeInfosCache();

    /**
     * get notice mono by id
     *
     * @param id
     * @return
     */
    Mono<Notice> getNotice(Long id);

    /**
     * get notice by type
     *
     * @param noticeType
     * @return
     */
    Notice getNoticeByType(Integer noticeType);

    /**
     * get notice info
     *
     * @param noticeType
     * @return
     */
    Mono<NoticeInfo> getNoticeInfoByTypeWithCache(Integer noticeType);

    /**
     * select notice by page and condition
     *
     * @param limit
     * @param rows
     * @param noticeCondition
     * @return
     */
    Mono<List<Notice>> selectNoticeByLimitAndCondition(Long limit, Long rows, NoticeCondition noticeCondition);

    /**
     * count notice by condition
     *
     * @param noticeCondition
     * @return
     */
    Mono<Long> countNoticeByCondition(NoticeCondition noticeCondition);

    /**
     * select notice info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<NoticeManagerInfo>> selectNoticeManagerInfoPageByPageAndCondition(PageModelRequest<NoticeCondition> pageModelRequest);

}
