package com.blue.verify.api.inter;

import com.blue.base.constant.verify.VerifyType;
import com.blue.verify.api.model.VerifyPair;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * rpc verify interface
 *
 * @author DarkBlue
 * @date 2021/8/12
 * @apiNote
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
    CompletableFuture<VerifyPair> generate(VerifyType type, String key, Integer length, Duration expire);

    /**
     * validate pair
     *
     * @param type
     * @param verifyPair
     * @param repeatable
     * @return
     */
    CompletableFuture<Boolean> validate(VerifyType type, VerifyPair verifyPair, Boolean repeatable);

}
