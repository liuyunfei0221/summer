package com.blue.verify.api.inter;

import com.blue.basic.constant.verify.VerifyType;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * rpc verify interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface RpcVerifyService {

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @param length
     * @param expire
     * @return
     */
    CompletableFuture<String> generate(VerifyType type, String key, Integer length, Duration expire);

    /**
     * validate pair
     *
     * @param type
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    CompletableFuture<Boolean> validate(VerifyType type, String key, String verify, Boolean repeatable);

}
