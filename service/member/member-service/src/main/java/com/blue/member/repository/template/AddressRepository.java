package com.blue.member.repository.template;

import com.blue.member.repository.entity.Address;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * address repository
 *
 * @author liuyunfei
 */
public interface AddressRepository extends ReactiveMongoRepository<Address, Long> {
}