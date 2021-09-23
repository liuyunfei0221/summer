package com.blue.file.service.impl;

import com.blue.file.repository.entity.DownloadHistory;
import com.blue.file.repository.mapper.DownloadHistoryMapper;
import com.blue.file.service.inter.DownloadHistoryService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * 文件下载历史业务实现
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Service
public class DownloadHistoryServiceImpl implements DownloadHistoryService {

    private static final Logger LOGGER = getLogger(DownloadHistoryServiceImpl.class);

    private final DownloadHistoryMapper downloadHistoryMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DownloadHistoryServiceImpl(DownloadHistoryMapper downloadHistoryMapper) {
        this.downloadHistoryMapper = downloadHistoryMapper;
    }

    /**
     * 新增文件下载历史
     *
     * @param downloadHistory
     * @return
     */
    @Override
    public void insert(DownloadHistory downloadHistory) {
        LOGGER.info("insert(DownloadHistory downloadHistory), downloadHistory = {}", downloadHistory);
        downloadHistoryMapper.insert(downloadHistory);
    }

}
