package com.blue.media.repository.template;

import com.blue.media.repository.entity.QrCodeConfig;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * qr code config repository
 *
 * @author liuyunfei
 */
public interface QrCodeConfigRepository extends ReactiveMongoRepository<QrCodeConfig, Long> {
}