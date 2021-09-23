package com.blue.file.service.inter;

import com.blue.file.repository.entity.DownloadHistory;

/**
 * 文件下载历史业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface DownloadHistoryService {

    /**
     * 新增文件下载历史
     *
     * @param downloadHistory
     * @return
     */
    void insert(DownloadHistory downloadHistory);


}
