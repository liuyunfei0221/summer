package com.blue.media.service.impl;

import com.blue.media.repository.entity.DownloadHistory;
import com.blue.media.repository.mapper.DownloadHistoryMapper;
import com.blue.media.service.inter.DownloadHistoryService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * download history service impl
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
     * insert download history
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
