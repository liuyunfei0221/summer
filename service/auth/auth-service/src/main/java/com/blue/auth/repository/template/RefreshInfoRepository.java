package com.blue.auth.repository.template;

import com.blue.auth.repository.entity.RefreshInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * refresh info repository
 *
 * @author liuyunfei
 */
public interface RefreshInfoRepository extends ReactiveMongoRepository<RefreshInfo, String> {
}