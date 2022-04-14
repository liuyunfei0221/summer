package com.blue.media.service.inter;

import com.blue.media.repository.entity.DownloadHistory;

/**
 * download history service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface DownloadHistoryService {

    /**
     * insert download history
     *
     * @param downloadHistory
     * @return
     */
    int insert(DownloadHistory downloadHistory);

}
