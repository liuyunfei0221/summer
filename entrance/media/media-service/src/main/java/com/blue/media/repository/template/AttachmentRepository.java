package com.blue.media.repository.template;

import com.blue.media.repository.entity.Attachment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * attachment repository
 *
 * @author liuyunfei
 */
public interface AttachmentRepository extends ReactiveMongoRepository<Attachment, Long> {
}