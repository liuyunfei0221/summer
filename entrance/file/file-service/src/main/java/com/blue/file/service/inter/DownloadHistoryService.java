package com.blue.file.service.inter;

import com.blue.file.repository.entity.DownloadHistory;

/**
 * download history service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface DownloadHistoryService {

    /**
     * insert download history
     *
     * @param downloadHistory
     * @return
     */
    void insert(DownloadHistory downloadHistory);

}
