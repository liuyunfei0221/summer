package com.blue.member.repository.template;

import com.blue.member.repository.entity.MemberAddress;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * member address repository
 *
 * @author liuyunfei
 */
public interface MemberAddressRepository extends ReactiveMongoRepository<MemberAddress, Long> {
}