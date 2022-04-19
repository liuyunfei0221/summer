package com.blue.media.repository.template;

import com.blue.media.repository.entity.DownloadHistory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * download history repository
 *
 * @author liuyunfei
 */
public interface DownloadHistoryRepository extends ReactiveMongoRepository<DownloadHistory, Long> {
}